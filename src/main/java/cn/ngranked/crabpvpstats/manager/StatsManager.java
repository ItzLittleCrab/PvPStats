package cn.ngranked.crabpvpstats.manager;

import cn.ngranked.crabpvpstats.api.StatsUpdateEvent;
import cn.ngranked.crabpvpstats.database.DatabaseManager;
import cn.ngranked.crabpvpstats.model.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 统计管理器 — 核心业务逻辑层，提供缓存和击杀/死亡处理
 */
public class StatsManager {

    private final JavaPlugin plugin;
    private final DatabaseManager databaseManager;
    private final Map<UUID, PlayerStats> cache = new ConcurrentHashMap<>();

    public StatsManager(JavaPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    /**
     * 获取玩家统计数据（优先使用缓存）
     */
    public PlayerStats getStats(UUID uuid) {
        return cache.computeIfAbsent(uuid, id -> {
            PlayerStats stats = databaseManager.getPlayerStats(id);
            // Cache the result even if it's a new default entry
            return stats;
        });
    }

    /**
     * 保存玩家统计数据（更新缓存 + 写入数据库）
     */
    public void saveStats(PlayerStats stats) {
        cache.put(stats.getPlayerUuid(), stats);
        databaseManager.savePlayerStats(stats);
    }

    /**
     * 处理一次 PvP 击杀事件
     *
     * @param damager 击杀者
     * @param victim  被击杀者
     */
    public void handleKill(Player damager, Player victim) {
        // 更新击杀者数据
        PlayerStats damagerStats = getStats(damager.getUniqueId());
        if (damagerStats.getPlayerName() == null || damagerStats.getPlayerName().isEmpty()) {
            damagerStats.setPlayerName(damager.getName());
        } else if (!damagerStats.getPlayerName().equals(damager.getName())) {
            damagerStats.setPlayerName(damager.getName());
        }
        damagerStats.recordKill();
        saveStats(damagerStats);
        Bukkit.getPluginManager().callEvent(new StatsUpdateEvent(damager.getUniqueId(), damagerStats));

        // 更新被击杀者数据
        PlayerStats victimStats = getStats(victim.getUniqueId());
        if (victimStats.getPlayerName() == null || victimStats.getPlayerName().isEmpty()) {
            victimStats.setPlayerName(victim.getName());
        } else if (!victimStats.getPlayerName().equals(victim.getName())) {
            victimStats.setPlayerName(victim.getName());
        }
        victimStats.recordDeath();
        saveStats(victimStats);
        Bukkit.getPluginManager().callEvent(new StatsUpdateEvent(victim.getUniqueId(), victimStats));
    }

    /**
     * 玩家退出时保存缓存数据
     */
    public void handlePlayerQuit(UUID playerUuid) {
        PlayerStats stats = cache.remove(playerUuid);
        if (stats != null) {
            databaseManager.savePlayerStats(stats);
        }
    }

    /**
     * 保存所有缓存数据到数据库（插件关闭时调用）
     */
    public void saveAll() {
        for (Map.Entry<UUID, PlayerStats> entry : cache.entrySet()) {
            databaseManager.savePlayerStats(entry.getValue());
        }
        cache.clear();
        plugin.getLogger().info("Saved all cached stats to database.");
    }

    // 排行榜查询 — 委托给 DatabaseManager

    public List<PlayerStats> getTopKills(int limit) {
        return databaseManager.getTopKills(limit);
    }

    public List<PlayerStats> getTopDeaths(int limit) {
        return databaseManager.getTopDeaths(limit);
    }

    public List<PlayerStats> getTopKDR(int limit) {
        return databaseManager.getTopKDR(limit);
    }

    public List<PlayerStats> getTopKillstreak(int limit) {
        return databaseManager.getTopKillstreak(limit);
    }

    /**
     * 获取缓存中的玩家名称映射
     */
    public Map<UUID, String> getCachedPlayerNames() {
        Map<UUID, String> names = new ConcurrentHashMap<>();
        for (Map.Entry<UUID, PlayerStats> entry : cache.entrySet()) {
            String name = entry.getValue().getPlayerName();
            if (name != null && !name.isEmpty()) {
                names.put(entry.getKey(), name);
            }
        }
        return names;
    }

    /**
     * 清空缓存并重新加载
     */
    public void reload() {
        cache.clear();
        plugin.getLogger().info("Stats cache cleared.");
    }
}
