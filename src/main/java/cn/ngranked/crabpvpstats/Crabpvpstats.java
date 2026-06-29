package cn.ngranked.crabpvpstats;

import cn.ngranked.crabpvpstats.api.impl.CrabPvPStatsAPIImpl;
import cn.ngranked.crabpvpstats.command.AdminCommand;
import cn.ngranked.crabpvpstats.command.AdminTabCompleter;
import cn.ngranked.crabpvpstats.command.StatsCommand;
import cn.ngranked.crabpvpstats.config.ConfigManager;
import cn.ngranked.crabpvpstats.config.MessageManager;
import cn.ngranked.crabpvpstats.database.DatabaseManager;
import cn.ngranked.crabpvpstats.expansion.StatsExpansion;
import cn.ngranked.crabpvpstats.gui.StatsGUI;
import cn.ngranked.crabpvpstats.listener.PvPListener;
import cn.ngranked.crabpvpstats.manager.StatsManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Crabpvpstats extends JavaPlugin {

    private ConfigManager configManager;
    private MessageManager messageManager;
    private DatabaseManager databaseManager;
    private StatsManager statsManager;
    private StatsGUI statsGUI;

    @Override
    public void onEnable() {
        // 1. 配置（构造函数中自动加载）
        this.configManager = new ConfigManager(this);
        getLogger().info("Config loaded.");

        // 2. 消息 — 使用配置中的语言设置
        String langCode = configManager.getLanguage();
        if (langCode.equals("auto")) {
            langCode = "us-en";
        }
        this.messageManager = new MessageManager(this, configManager, langCode);
        getLogger().info("Messages loaded.");

        // 3. 数据库
        this.databaseManager = new DatabaseManager(this);
        databaseManager.init();
        getLogger().info("Database initialized.");

        // 4. 统计管理器
        this.statsManager = new StatsManager(this, databaseManager);
        getLogger().info("Stats manager ready.");

        // 5. GUI
        this.statsGUI = new StatsGUI(statsManager, messageManager);

        // 6. 注册事件监听
        Bukkit.getPluginManager().registerEvents(new PvPListener(statsManager), this);
        Bukkit.getPluginManager().registerEvents(statsGUI, this);
        getLogger().info("Listeners registered.");

        // 7. 注册指令
        StatsCommand statsCommand = new StatsCommand(statsManager, configManager, messageManager, statsGUI);
        getCommand("stats").setExecutor(statsCommand);
        AdminCommand adminCommand = new AdminCommand(statsManager, configManager, messageManager, databaseManager);
        getCommand("crabpvpstats").setExecutor(adminCommand);
        getCommand("crabpvpstats").setTabCompleter(new AdminTabCompleter());

        // 8. 注册 PlaceholderAPI 扩展
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new StatsExpansion(statsManager).register();
            getLogger().info("PAPI expansion registered.");
        } else {
            getLogger().warning("PlaceholderAPI not found; placeholders unavailable.");
        }

        // 9. 注册 API 提供者
        CrabPvPStatsAPIImpl api = new CrabPvPStatsAPIImpl(this, databaseManager);
        CrabPvPStatsProvider.register(api);
        getLogger().info("API provider registered.");

        getLogger().info("CrabPvPStats enabled.");
    }

    @Override
    public void onDisable() {
        if (statsManager != null) {
            statsManager.saveAll();
            getLogger().info("Stats saved.");
        }

        if (databaseManager != null) {
            databaseManager.close();
            getLogger().info("Database closed.");
        }

        CrabPvPStatsProvider.unregister();
        getLogger().info("CrabPvPStats disabled.");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }
}
