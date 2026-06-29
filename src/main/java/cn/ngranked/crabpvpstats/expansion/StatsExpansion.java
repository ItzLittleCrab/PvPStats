package cn.ngranked.crabpvpstats.expansion;

import cn.ngranked.crabpvpstats.manager.StatsManager;
import cn.ngranked.crabpvpstats.model.PlayerStats;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

/**
 * CrabPvPStats PlaceholderAPI 扩展
 * <p>
 * 占位符列表:
 * - %crabps_kills% — 当前玩家击杀数
 * - %crabps_deaths% — 当前玩家死亡数
 * - %crabps_kdr% — 当前玩家 KDR
 * - %crabps_killstreak% — 当前连杀
 * - %crabps_kills_top_{N}% — 第 N 名击杀数
 * - %crabps_deaths_top_{N}% — 第 N 名死亡数
 * - %crabps_kdr_top_{N}% — 第 N 名 KDR
 * - %crabps_killstreak_top_{N}% — 第 N 名连杀
 * - %crabps_kills_topplayer_{N}% — 第 N 名击杀玩家名
 * - %crabps_deaths_topplayer_{N}% — 第 N 名死亡玩家名
 * - %crabps_kdr_topplayer_{N}% — 第 N 名 KDR 玩家名
 * - %crabps_killstreak_topplayer_{N}% — 第 N 名连杀玩家名
 */
public class StatsExpansion extends PlaceholderExpansion {

    private final StatsManager statsManager;
    private final Map<String, CachedTop> topCache = new HashMap<>();
    private static final long CACHE_TTL_MS = 30_000L; // 30 秒缓存

    public StatsExpansion(StatsManager statsManager) {
        this.statsManager = statsManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "crabps";
    }

    @Override
    public @NotNull String getAuthor() {
        return "ItzLittleCrab";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.1.1";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params == null || params.isEmpty()) {
            return null;
        }

        String lower = params.toLowerCase();

        // 个人占位符（不含 _top_ 也不含 _topplayer_）
        if (!(lower.contains("_top_") || lower.contains("_topplayer_"))) {
            if (player == null) return null;

            PlayerStats stats = statsManager.getStats(player.getUniqueId());

            switch (lower) {
                case "kills":
                    return String.valueOf(stats.getKills());
                case "deaths":
                    return String.valueOf(stats.getDeaths());
                case "kdr":
                    return stats.getKdrFormatted();
                case "killstreak":
                    return String.valueOf(stats.getKillstreak());
                default:
                    return null;
            }
        }

        // 排行榜占位符: {field}_top_{number} 或 {field}_topplayer_{number}
        boolean showPlayerName = lower.contains("_topplayer_");
        String delimiter = showPlayerName ? "_topplayer_" : "_top_";

        String[] parts = lower.split(delimiter);
        if (parts.length != 2) return null;

        String field = parts[0];
        int rank;
        try {
            rank = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return null;
        }
        if (rank < 1) return null;

        int index = rank - 1; // 转为 0-based

        List<PlayerStats> topList;
        switch (field) {
            case "kills":
                topList = getCachedTop("kills", () -> statsManager.getTopKills(rank));
                break;
            case "deaths":
                topList = getCachedTop("deaths", () -> statsManager.getTopDeaths(rank));
                break;
            case "kdr":
                topList = getCachedTop("kdr", () -> statsManager.getTopKDR(rank));
                break;
            case "killstreak":
                topList = getCachedTop("killstreak", () -> statsManager.getTopKillstreak(rank));
                break;
            default:
                return null;
        }

        if (index >= topList.size()) {
            return "-";
        }

        PlayerStats entry = topList.get(index);

        // _topplayer_ → 返回玩家名
        if (showPlayerName) {
            String name = entry.getPlayerName();
            return (name != null && !name.isEmpty()) ? name : "-";
        }

        // _top_ → 返回数值
        switch (field) {
            case "kills":
                return String.valueOf(entry.getKills());
            case "deaths":
                return String.valueOf(entry.getDeaths());
            case "kdr":
                return entry.getKdrFormatted();
            case "killstreak":
                return String.valueOf(entry.getBestKillstreak());
            default:
                return null;
        }
    }

    /**
     * 获取缓存的排行榜数据（30 秒 TTL）
     */
    private List<PlayerStats> getCachedTop(String key, Supplier<List<PlayerStats>> fetcher) {
        long now = System.currentTimeMillis();
        CachedTop cached = topCache.get(key);

        if (cached != null && (now - cached.timestamp) < CACHE_TTL_MS) {
            return cached.data;
        }

        List<PlayerStats> data = fetcher.get();
        topCache.put(key, new CachedTop(data, now));
        return data;
    }

    private static class CachedTop {
        final List<PlayerStats> data;
        final long timestamp;

        CachedTop(List<PlayerStats> data, long timestamp) {
            this.data = data;
            this.timestamp = timestamp;
        }
    }
}
