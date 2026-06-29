package cn.ngranked.crabpvpstats.gui;

import cn.ngranked.crabpvpstats.config.MessageManager;
import cn.ngranked.crabpvpstats.manager.StatsManager;
import cn.ngranked.crabpvpstats.model.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 统计信息 GUI — 27 格 ChestInventory 展示玩家 PvP 数据
 */
public class StatsGUI implements Listener {

    private final StatsManager statsManager;
    private final MessageManager messageManager;

    public StatsGUI(StatsManager statsManager, MessageManager messageManager) {
        this.statsManager = statsManager;
        this.messageManager = messageManager;
    }

    /**
     * 打开统计菜单（查看自己的数据）
     */
    public void openStatsMenu(Player viewer, UUID targetUuid) {
        openStatsMenu(viewer, targetUuid, null);
    }

    /**
     * 打开统计菜单（可指定目标玩家名称）
     */
    public void openStatsMenu(Player viewer, UUID targetUuid, String targetName) {
        PlayerStats stats = statsManager.getStats(targetUuid);

        // 确定标题
        String title;
        boolean isSelf = viewer.getUniqueId().equals(targetUuid);
        if (isSelf) {
            title = messageManager.getMessage("gui.title");
        } else {
            String name = (targetName != null) ? targetName : stats.getPlayerName();
            if (name == null || name.isEmpty()) {
                name = targetUuid.toString().substring(0, 8);
            }
            title = messageManager.getMessage("gui.title-other", name);
        }

        Inventory inv = Bukkit.createInventory(null, 27, title);

        // 填充玻璃板（第1行和第3行）
        ItemStack glassPane = createGlassPane();

        for (int i = 0; i < 9; i++) {
            inv.setItem(i, glassPane);
        }
        for (int i = 18; i < 27; i++) {
            inv.setItem(i, glassPane);
        }

        // 击杀展示 — 位置 0（第1行第1列）
        inv.setItem(0, createInfoItem(
                Material.DIAMOND_SWORD,
                messageManager.getMessage("gui.kills"),
                messageManager.getMessage("gui.kills") + ": " + stats.getKills()
        ));

        // 死亡展示 — 位置 1（第1行第2列）
        inv.setItem(1, createInfoItem(
                Material.SKELETON_SKULL,
                messageManager.getMessage("gui.deaths"),
                messageManager.getMessage("gui.deaths") + ": " + stats.getDeaths()
        ));

        // KDR 展示 — 位置 2（第1行第3列）
        inv.setItem(2, createInfoItem(
                Material.GOLD_INGOT,
                messageManager.getMessage("gui.kdr"),
                messageManager.getMessage("gui.kdr") + ": " + stats.getKdrFormatted()
        ));

        // 当前连杀 — 位置 4（第1行第5列）
        inv.setItem(4, createInfoItem(
                Material.BLAZE_POWDER,
                messageManager.getMessage("gui.killstreak"),
                messageManager.getMessage("gui.killstreak") + ": " + stats.getKillstreak()
        ));

        // 最佳连杀 — 位置 5（第1行第6列）
        inv.setItem(5, createInfoItem(
                Material.NETHER_STAR,
                messageManager.getMessage("gui.best-killstreak"),
                messageManager.getMessage("gui.best-killstreak") + ": " + stats.getBestKillstreak()
        ));

        viewer.openInventory(inv);
    }

    private ItemStack createGlassPane() {
        ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = pane.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" ");
        }
        pane.setItemMeta(meta);
        return pane;
    }

    private ItemStack createInfoItem(Material material, String displayName, String loreLine) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(Arrays.asList(loreLine));
            // 隐藏物品属性标签
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE);
        }
        item.setItemMeta(meta);
        return item;
    }

    /**
     * 防止玩家取走 GUI 中的物品
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().contains("PvP")) {
            event.setCancelled(true);
        }
    }
}
