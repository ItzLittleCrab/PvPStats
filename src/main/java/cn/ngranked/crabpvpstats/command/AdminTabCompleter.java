package cn.ngranked.crabpvpstats.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * /crabpvpstats 和 /cps 的 Tab 补全
 */
public class AdminTabCompleter implements TabCompleter {

    private static final List<String> SUBCOMMANDS = Arrays.asList("reload", "help");
    private static final List<String> OPERATIONS = Arrays.asList("add", "set");
    private static final List<String> STAT_FIELDS = Arrays.asList("kills", "deaths", "killstreak");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("crabpvpstats.admin")) {
            return new ArrayList<>();
        }

        if (args.length == 1) {
            // 补全: 在线玩家名 + reload + help
            List<String> suggestions = new ArrayList<>(SUBCOMMANDS);
            suggestions.addAll(Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList()));
            return filter(suggestions, args[0]);
        }

        if (args.length == 2) {
            // 第一个参数是玩家名 → 补全 add/set
            return filter(OPERATIONS, args[1]);
        }

        if (args.length == 3) {
            // 补全 kills/deaths/killstreak
            return filter(STAT_FIELDS, args[2]);
        }

        // args.length >= 4 → 无补全
        return new ArrayList<>();
    }

    private List<String> filter(List<String> list, String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return list;
        }
        String lower = prefix.toLowerCase();
        return list.stream()
                .filter(s -> s.toLowerCase().startsWith(lower))
                .collect(Collectors.toList());
    }
}
