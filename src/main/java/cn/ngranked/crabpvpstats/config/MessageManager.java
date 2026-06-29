package cn.ngranked.crabpvpstats.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息管理器 — 处理多语言消息加载、缓存和获取
 */
public class MessageManager {

    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final Map<String, String> messages = new HashMap<>();
    private String currentLanguage;

    public MessageManager(JavaPlugin plugin, ConfigManager configManager, String languageCode) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.currentLanguage = languageCode;
        load();
    }

    /**
     * 加载指定语言的消息文件
     */
    public void load() {
        messages.clear();

        String langFile = resolveLanguageFile(currentLanguage);
        InputStream input = plugin.getResource("lang/" + langFile);

        if (input == null) {
            plugin.getLogger().warning("Language file 'lang/" + langFile + "' not found. Falling back to us-en.yml");
            input = plugin.getResource("lang/us-en.yml");
            if (input == null) {
                plugin.getLogger().severe("Fallback language file 'lang/us-en.yml' also not found!");
                return;
            }
        }

        YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(
                new InputStreamReader(input, StandardCharsets.UTF_8)
        );

        for (String key : langConfig.getKeys(true)) {
            if (langConfig.isString(key)) {
                messages.put(key, langConfig.getString(key));
            }
        }

        plugin.getLogger().info("Loaded language: " + langFile);
    }

    /**
     * 根据语言代码解析对应的语言文件名
     */
    private String resolveLanguageFile(String code) {
        if (code == null || code.equalsIgnoreCase("auto") || code.isEmpty()) {
            return "us-en.yml";
        }
        if (code.startsWith("zh")) {
            return "zh-cn.yml";
        }
        return "us-en.yml";
    }

    /**
     * 检测玩家客户端的语言设置，返回对应的语言代码
     */
    public String detectLocale(Player player) {
        String locale = player.getLocale(); // e.g. "zh_cn", "en_us"
        if (locale == null) return "us-en";
        if (locale.toLowerCase().startsWith("zh")) return "zh-cn";
        return "us-en";
    }

    /**
     * 获取消息（已翻译颜色码）
     */
    public String getMessage(String key) {
        String msg = messages.get(key);
        if (msg == null) {
            return "&cMissing message: " + key;
        }
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    /**
     * 获取格式化后的消息（支持 %s 占位符）
     */
    public String getMessage(String key, Object... args) {
        String msg = messages.get(key);
        if (msg == null) {
            return "&cMissing message: " + key;
        }
        msg = String.format(msg, args);
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    /**
     * 重载语言文件
     */
    public void reload() {
        load();
    }
}
