package cn.ngranked.crabpvpstatus.command;

import cn.ngranked.crabpvpstatus.config.ConfigManager;
import cn.ngranked.crabpvpstatus.config.MessageManager;
import cn.ngranked.crabpvpstatus.gui.StatsGUI;
import cn.ngranked.crabpvpstatus.manager.StatsManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * /stats 指令处理器
 */
public class StatsCommand implements CommandExecutor {

    private final StatsManager statsManager;
    private final ConfigManager configManager;
    private final MessageManager messageManager;
    private final StatsGUI statsGUI;

    public StatsCommand(StatsManager statsManager, ConfigManager configManager,
                        MessageManager messageManager, StatsGUI statsGUI) {
        this.statsManager = statsManager;
        this.configManager = configManager;
        this.messageManager = messageManager;
        this.statsGUI = statsGUI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // /stats — 查看自己的数据
        if (args.length == 0) {
            if (!configManager.isAllowSelfStats()) {
                sendMessage(player, "command.no-permission");
                return true;
            }
            statsGUI.openStatsMenu(player, player.getUniqueId());
            return true;
        }

        // /stats <player> — 查看其他玩家的数据
        if (args.length == 1) {
            if (!configManager.isAllowOtherStats()) {
                sendMessage(player, "command.no-permission");
                return true;
            }
            if (!player.hasPermission("crabpvpstats.stats.others")) {
                sendMessage(player, "command.no-permission");
                return true;
            }

            String targetName = args[0];
            Player target = Bukkit.getPlayerExact(targetName);

            UUID targetUuid;
            if (target != null && target.isOnline()) {
                targetUuid = target.getUniqueId();
                targetName = target.getName();
            } else {
                // 尝试离线玩家查找
                @SuppressWarnings("deprecation")
                OfflinePlayer offline = Bukkit.getOfflinePlayer(targetName);
                if (offline.hasPlayedBefore() || offline.isOnline()) {
                    targetUuid = offline.getUniqueId();
                } else {
                    sendMessage(player, "command.player-not-found", targetName);
                    return true;
                }
            }

            statsGUI.openStatsMenu(player, targetUuid, targetName);
            return true;
        }

        // 参数错误
        sendMessage(player, "command.usage");
        return true;
    }

    private void sendMessage(Player player, String key) {
        player.sendMessage(messageManager.getMessage(key));
    }

    private void sendMessage(Player player, String key, Object... args) {
        player.sendMessage(messageManager.getMessage(key, args));
    }
}
