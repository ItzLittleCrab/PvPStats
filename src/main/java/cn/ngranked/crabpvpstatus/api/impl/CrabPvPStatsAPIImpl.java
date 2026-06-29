package cn.ngranked.crabpvpstatus.api.impl;

import cn.ngranked.crabpvpstatus.api.CrabPvPStatsAPI;
import cn.ngranked.crabpvpstatus.api.StatsUpdateEvent;
import cn.ngranked.crabpvpstatus.database.DatabaseManager;
import cn.ngranked.crabpvpstatus.model.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * CrabPvPStats API 实现
 */
public class CrabPvPStatsAPIImpl implements CrabPvPStatsAPI {

    private final JavaPlugin plugin;
    private final DatabaseManager databaseManager;
    private boolean enabled;

    public CrabPvPStatsAPIImpl(JavaPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.enabled = true;
    }

    @Override
    public PlayerStats getPlayerStats(UUID playerUuid) {
        return databaseManager.getPlayerStats(playerUuid);
    }

    @Override
    public PlayerStats getPlayerStats(String playerName) {
        // Try to look up offline player by name
        @SuppressWarnings("deprecation")
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        if (offlinePlayer.hasPlayedBefore() || offlinePlayer.isOnline()) {
            PlayerStats stats = databaseManager.getPlayerStats(offlinePlayer.getUniqueId());
            // Update name if empty
            if (stats.getPlayerName() == null || stats.getPlayerName().isEmpty()) {
                stats.setPlayerName(playerName);
            }
            return stats;
        }
        // Try direct database lookup by name
        // This is a simplified approach - in production, maintain a UUID cache
        return new PlayerStats(UUID.randomUUID(), playerName);
    }

    @Override
    public CompletableFuture<PlayerStats> getPlayerStatsAsync(UUID playerUuid) {
        CompletableFuture<PlayerStats> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            PlayerStats stats = databaseManager.getPlayerStats(playerUuid);
            future.complete(stats);
        });
        return future;
    }

    @Override
    public void addKill(UUID playerUuid) {
        PlayerStats stats = databaseManager.getPlayerStats(playerUuid);
        stats.recordKill();
        databaseManager.savePlayerStats(stats);
        Bukkit.getPluginManager().callEvent(new StatsUpdateEvent(playerUuid, stats));
    }

    @Override
    public void addDeath(UUID playerUuid) {
        PlayerStats stats = databaseManager.getPlayerStats(playerUuid);
        stats.recordDeath();
        databaseManager.savePlayerStats(stats);
        Bukkit.getPluginManager().callEvent(new StatsUpdateEvent(playerUuid, stats));
    }

    @Override
    public void resetStats(UUID playerUuid) {
        PlayerStats stats = databaseManager.getPlayerStats(playerUuid);
        stats.setKills(0);
        stats.setDeaths(0);
        stats.setKillstreak(0);
        stats.setBestKillstreak(0);
        stats.setLastUpdated(System.currentTimeMillis());
        databaseManager.savePlayerStats(stats);
        Bukkit.getPluginManager().callEvent(new StatsUpdateEvent(playerUuid, stats));
    }

    @Override
    public void setStats(UUID playerUuid, PlayerStats stats) {
        databaseManager.savePlayerStats(stats);
        Bukkit.getPluginManager().callEvent(new StatsUpdateEvent(playerUuid, stats));
    }

    @Override
    public List<PlayerStats> getTopKills(int limit) {
        return databaseManager.getTopKills(limit);
    }

    @Override
    public List<PlayerStats> getTopDeaths(int limit) {
        return databaseManager.getTopDeaths(limit);
    }

    @Override
    public List<PlayerStats> getTopKDR(int limit) {
        return databaseManager.getTopKDR(limit);
    }

    @Override
    public List<PlayerStats> getTopKillstreak(int limit) {
        return databaseManager.getTopKillstreak(limit);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void reload() {
        // Reload managed by main plugin class
    }
}
