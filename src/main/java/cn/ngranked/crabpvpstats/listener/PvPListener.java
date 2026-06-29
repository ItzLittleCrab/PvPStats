package cn.ngranked.crabpvpstats.listener;

import cn.ngranked.crabpvpstats.manager.StatsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * PvP 事件监听器 — 追踪玩家间的击杀和死亡
 */
public class PvPListener implements Listener {

    private final StatsManager statsManager;
    private final Map<UUID, UUID> lastDamager = new HashMap<>();
    private final Map<UUID, UUID> anchorDamager = new HashMap<>();
    private static final double ANCHOR_BLAST_RADIUS = 5.0;

    public PvPListener(StatsManager statsManager) {
        this.statsManager = statsManager;
    }

    /**
     * 记录玩家间的伤害事件，追踪最后一次攻击者
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();
            lastDamager.put(victim.getUniqueId(), damager.getUniqueId());
        }
    }

    /**
     * 检测玩家右键重生锚 — 若在主世界右键已充能的重生锚（且不手持萤石），
     * 会触发生成爆炸，预注册爆炸范围内的潜在受害者
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null) return;
        if (!(block.getBlockData() instanceof RespawnAnchor anchor)) return;
        if (anchor.getCharges() == 0) return;
        // 地狱中右键是设置重生点，不会爆炸
        if (event.getPlayer().getWorld().getEnvironment() == World.Environment.NETHER) return;

        // 手持萤石是添加充能，不会爆炸
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.GLOWSTONE) return;

        // 以上条件均满足 → 重生锚会爆炸，预注册附近玩家
        Player triggerer = event.getPlayer();
        Location center = block.getLocation();

        for (Entity entity : center.getWorld().getNearbyEntities(center, ANCHOR_BLAST_RADIUS, ANCHOR_BLAST_RADIUS, ANCHOR_BLAST_RADIUS)) {
            if (entity instanceof Player victim && !victim.getUniqueId().equals(triggerer.getUniqueId())) {
                anchorDamager.putIfAbsent(victim.getUniqueId(), triggerer.getUniqueId());
            }
        }
    }

    /**
     * 处理玩家死亡事件 — 识别 PvP/重生锚击杀并更新统计
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        UUID damagerUuid = lastDamager.remove(victim.getUniqueId());

        if (damagerUuid == null) {
            damagerUuid = anchorDamager.remove(victim.getUniqueId());
        } else {
            anchorDamager.remove(victim.getUniqueId());
        }

        if (damagerUuid != null) {
            Player damager = Bukkit.getPlayer(damagerUuid);
            if (damager != null && damager.isOnline()) {
                statsManager.handleKill(damager, victim);
            }
        }
        // 非 PvP/重生锚死亡 — 忽略，不记录
    }

    /**
     * 玩家退出时保存缓存数据并清理伤害追踪
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerUuid = event.getPlayer().getUniqueId();
        statsManager.handlePlayerQuit(playerUuid);
        lastDamager.remove(playerUuid);
        anchorDamager.remove(playerUuid);
    }
}
