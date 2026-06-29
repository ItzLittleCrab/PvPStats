package cn.ngranked.crabpvpstats.command;

import cn.ngranked.crabpvpstats.config.ConfigManager;
import cn.ngranked.crabpvpstats.config.MessageManager;
import cn.ngranked.crabpvpstats.database.DatabaseManager;
import cn.ngranked.crabpvpstats.manager.StatsManager;
import cn.ngranked.crabpvpstats.model.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * /crabpvpstats 管理指令处理器
 * <p>
 * 子指令:
 * - /crabpvpstats reload              — 重载配置和语言文件
 * - /crabpvpstats <player> add <stat> <value>  — 增加玩家某项数据
 * - /crabpvpstats <player> set <stat> <value>  — 设置玩家某项数据
 */
public class AdminCommand implements CommandExecutor {

    private final StatsManager statsManager;
    private final ConfigManager configManager;
    private final MessageManager messageManager;
    private final DatabaseManager databaseManager;

    public AdminCommand(StatsManager statsManager, ConfigManager configManager,
                        MessageManager messageManager, DatabaseManager databaseManager) {
        this.statsManager = statsManager;
        this.configManager = configManager;
        this.messageManager = messageManager;
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("crabpvpstats.admin")) {
            sender.sendMessage("§c你没有权限使用此指令。");
            return true;
        }

        if (args.length == 0) {
            sendUsage(sender, label);
            return true;
        }

        // /crabpvpstats help
        if (args[0].equalsIgnoreCase("help")) {
            sendUsage(sender, label);
            return true;
        }

        // /crabpvpstats reload
        if (args[0].equalsIgnoreCase("reload")) {
            return handleReload(sender);
        }

        // /crabpvpstats <player> add|set <stat> <value>
        if (args.length < 4) {
            sendUsage(sender, label);
            return true;
        }

        String targetName = args[0];
        String operation = args[1].toLowerCase();
        String statField = args[2].toLowerCase();
        int value;

        try {
            value = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§c数值格式错误: " + args[3]);
            return true;
        }

        if (value < 0) {
            sender.sendMessage("§c数值不能为负数。");
            return true;
        }

        if (!operation.equals("add") && !operation.equals("set")) {
            sender.sendMessage("§c操作类型错误，请使用 add 或 set。");
            return true;
        }

        if (!statField.equals("kills") && !statField.equals("deaths") && !statField.equals("killstreak")) {
            sender.sendMessage("§c统计数据字段错误，请使用 kills、deaths 或 killstreak。");
            return true;
        }

        return handleModify(sender, targetName, operation, statField, value);
    }

    private boolean handleReload(CommandSender sender) {
        configManager.load();
        messageManager.reload();
        statsManager.reload();
        sender.sendMessage("§a配置已重载。");
        return true;
    }

    private boolean handleModify(CommandSender sender, String targetName,
                                  String operation, String statField, int value) {
        // 1. 查找目标玩家
        UUID targetUuid = null;
        String resolvedName = targetName;

        Player onlinePlayer = Bukkit.getPlayerExact(targetName);
        if (onlinePlayer != null && onlinePlayer.isOnline()) {
            targetUuid = onlinePlayer.getUniqueId();
            resolvedName = onlinePlayer.getName();
        } else {
            // 尝试数据库查找
            PlayerStats dbStats = databaseManager.getPlayerStatsByName(targetName);
            if (dbStats != null && dbStats.getPlayerUuid() != null) {
                targetUuid = dbStats.getPlayerUuid();
                resolvedName = dbStats.getPlayerName();
            } else {
                // 离线玩家回退
                @SuppressWarnings("deprecation")
                OfflinePlayer offline = Bukkit.getOfflinePlayer(targetName);
                if (offline.hasPlayedBefore() || offline.isOnline()) {
                    targetUuid = offline.getUniqueId();
                } else {
                    sender.sendMessage("§c未找到玩家: " + targetName);
                    return true;
                }
            }
        }

        // 2. 获取当前统计数据
        PlayerStats stats = statsManager.getStats(targetUuid);
        if (stats.getPlayerName() == null || stats.getPlayerName().isEmpty()) {
            stats.setPlayerName(resolvedName);
        } else if (!stats.getPlayerName().equals(resolvedName)) {
            stats.setPlayerName(resolvedName);
        }

        // 3. 执行修改
        int oldValue = getStatValue(stats, statField);
        int newValue;

        if (operation.equals("add")) {
            newValue = oldValue + value;
        } else {
            newValue = value;
        }

        setStatValue(stats, statField, newValue);

        // 如果 killstreak 被修改，同步更新 best_killstreak
        if (statField.equals("killstreak") && stats.getKillstreak() > stats.getBestKillstreak()) {
            stats.setBestKillstreak(stats.getKillstreak());
        }

        // 4. 保存
        statsManager.saveStats(stats);

        sender.sendMessage("§a已" + (operation.equals("add") ? "增加" : "设置")
                + " §e" + resolvedName + "§a 的 §6" + statField
                + "§a: §e" + oldValue + " §a→ §e" + newValue);
        return true;
    }

    private int getStatValue(PlayerStats stats, String field) {
        switch (field) {
            case "kills":
                return stats.getKills();
            case "deaths":
                return stats.getDeaths();
            case "killstreak":
                return stats.getKillstreak();
            default:
                return 0;
        }
    }

    private void setStatValue(PlayerStats stats, String field, int value) {
        switch (field) {
            case "kills":
                stats.setKills(value);
                break;
            case "deaths":
                stats.setDeaths(value);
                break;
            case "killstreak":
                stats.setKillstreak(value);
                break;
        }
    }

    private void sendUsage(CommandSender sender, String label) {
        sender.sendMessage("§6===== CrabPvPStats 管理指令 =====");
        sender.sendMessage("§e/" + label + " reload §7- 重载配置");
        sender.sendMessage("§e/" + label + " <玩家> add <kills|deaths|killstreak> <数值> §7- 增加数据");
        sender.sendMessage("§e/" + label + " <玩家> set <kills|deaths|killstreak> <数值> §7- 设置数据");
    }
}
