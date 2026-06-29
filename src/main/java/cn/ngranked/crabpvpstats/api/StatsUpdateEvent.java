package cn.ngranked.crabpvpstats.api;

import cn.ngranked.crabpvpstats.model.PlayerStats;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * 当玩家统计数据更新时触发的事件。
 * <p>
 * 其他插件可以实现此事件的监听器来获取统计数据变更通知。
 */
public class StatsUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final UUID playerUuid;
    private final PlayerStats stats;

    public StatsUpdateEvent(UUID playerUuid, PlayerStats stats) {
        this.playerUuid = playerUuid;
        this.stats = stats;
    }

    /**
     * 获取统计数据发生变更的玩家 UUID
     */
    public UUID getPlayerUuid() {
        return playerUuid;
    }

    /**
     * 获取更新后的玩家统计数据
     */
    public PlayerStats getStats() {
        return stats;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
