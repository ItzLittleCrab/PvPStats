package cn.ngranked.crabpvpstats.model;

import java.util.UUID;

/**
 * 玩家 PvP 统计数据模型
 */
public class PlayerStats {

    private UUID playerUuid;
    private String playerName;
    private int kills;
    private int deaths;
    private int killstreak;
    private int bestKillstreak;
    private long lastUpdated;

    public PlayerStats() {
    }

    public PlayerStats(UUID playerUuid, String playerName) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.kills = 0;
        this.deaths = 0;
        this.killstreak = 0;
        this.bestKillstreak = 0;
        this.lastUpdated = System.currentTimeMillis();
    }

    public PlayerStats(UUID playerUuid, String playerName, int kills, int deaths,
                       int killstreak, int bestKillstreak, long lastUpdated) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.kills = kills;
        this.deaths = deaths;
        this.killstreak = killstreak;
        this.bestKillstreak = bestKillstreak;
        this.lastUpdated = lastUpdated;
    }

    /**
     * 记录一次击杀：击杀数+1，连杀+1，更新最佳连杀
     */
    public void recordKill() {
        this.kills++;
        this.killstreak++;
        if (this.killstreak > this.bestKillstreak) {
            this.bestKillstreak = this.killstreak;
        }
        this.lastUpdated = System.currentTimeMillis();
    }

    /**
     * 记录一次死亡：死亡数+1，连杀归零
     */
    public void recordDeath() {
        this.deaths++;
        this.killstreak = 0;
        this.lastUpdated = System.currentTimeMillis();
    }

    /**
     * 获取 KDR 值。
     * 死亡数为 0 时返回击杀数（double 值）；
     * 死亡数大于 0 时返回击杀/死亡（保留两位小数）。
     */
    public double getKdr() {
        if (deaths == 0) {
            return kills;
        }
        return Math.round((double) kills / deaths * 100.0) / 100.0;
    }

    /**
     * 获取格式化的 KDR 字符串（用于显示）。
     * 死亡为 0 时显示击杀数，否则显示保留两位小数的 KDR。
     */
    public String getKdrFormatted() {
        if (deaths == 0) {
            return String.valueOf(kills);
        }
        return String.format("%.2f", (double) kills / deaths);
    }

    // Getters and Setters

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public void setPlayerUuid(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getKillstreak() {
        return killstreak;
    }

    public void setKillstreak(int killstreak) {
        this.killstreak = killstreak;
    }

    public int getBestKillstreak() {
        return bestKillstreak;
    }

    public void setBestKillstreak(int bestKillstreak) {
        this.bestKillstreak = bestKillstreak;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
