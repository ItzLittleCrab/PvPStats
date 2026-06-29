package cn.ngranked.crabpvpstatus.listener;

import cn.ngranked.crabpvpstatus.manager.StatsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * PvP 事件监听器 — 追踪玩家间的击杀和死亡
 */
public class PvPListener implements Listener {

    private final StatsManager statsManager;
    private final Map<UUID, UUID> lastDamager = new HashMap<>();

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
     * 处理玩家死亡事件 — 识别 PvP 击杀并更新统计
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        UUID damagerUuid = lastDamager.remove(victim.getUniqueId());

        if (damagerUuid != null) {
            Player damager = Bukkit.getPlayer(damagerUuid);
            if (damager != null && damager.isOnline()) {
                statsManager.handleKill(damager, victim);
            }
        }
        // 非 PvP 死亡 — 忽略，不记录
    }

    /**
     * 玩家退出时保存缓存数据并清理伤害追踪
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerUuid = event.getPlayer().getUniqueId();
        statsManager.handlePlayerQuit(playerUuid);
        lastDamager.remove(playerUuid);
    }
}
