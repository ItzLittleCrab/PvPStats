package cn.ngranked.crabpvpstats.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 配置文件管理器 — 管理 config.yml 的加载、读取和重载
 */
public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;

    // 配置项默认值
    private String language;
    private boolean allowSelfStats;
    private boolean allowOtherStats;
    private int topPlayersCount;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    /**
     * 加载/重载配置文件
     */
    public void load() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();

        this.language = config.getString("language", "auto");
        this.allowSelfStats = config.getBoolean("allow-self-stats", true);
        this.allowOtherStats = config.getBoolean("allow-other-stats", true);
        this.topPlayersCount = config.getInt("top-players-count", 10);
    }

    public String getLanguage() {
        return language;
    }

    public boolean isAllowSelfStats() {
        return allowSelfStats;
    }

    public boolean isAllowOtherStats() {
        return allowOtherStats;
    }

    public int getTopPlayersCount() {
        return topPlayersCount;
    }
}
