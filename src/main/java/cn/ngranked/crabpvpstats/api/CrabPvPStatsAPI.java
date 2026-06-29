package cn.ngranked.crabpvpstats.api;

import cn.ngranked.crabpvpstats.model.PlayerStats;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * CrabPvPStats 公开 API 接口。
 * <p>
 * 其他插件可通过 {@link cn.ngranked.crabpvpstats.CrabPvPStatsProvider#getAPI()} 获取此接口实例。
 */
public interface CrabPvPStatsAPI {

    /**
     * 获取指定玩家的 PvP 统计数据
     *
     * @param playerUuid 玩家的 UUID
     * @return 玩家的统计数据，如果玩家不存在则返回包含默认值的 PlayerStats
     */
    PlayerStats getPlayerStats(UUID playerUuid);

    /**
     * 通过玩家名称获取统计数据
     *
     * @param playerName 玩家名称
     * @return 玩家的统计数据
     */
    PlayerStats getPlayerStats(String playerName);

    /**
     * 异步获取指定玩家的统计数据
     *
     * @param playerUuid 玩家的 UUID
     * @return 包含玩家统计数据的 CompletableFuture
     */
    CompletableFuture<PlayerStats> getPlayerStatsAsync(UUID playerUuid);

    /**
     * 为指定玩家增加一次击杀记录
     *
     * @param playerUuid 击杀者的 UUID
     */
    void addKill(UUID playerUuid);

    /**
     * 为指定玩家增加一次死亡记录
     *
     * @param playerUuid 死亡者的 UUID
     */
    void addDeath(UUID playerUuid);

    /**
     * 重置指定玩家的所有统计数据
     *
     * @param playerUuid 玩家的 UUID
     */
    void resetStats(UUID playerUuid);

    /**
     * 直接设置指定玩家的统计数据（覆盖）
     *
     * @param playerUuid 玩家的 UUID
     * @param stats      要设置的统计数据
     */
    void setStats(UUID playerUuid, PlayerStats stats);

    /**
     * 获取击杀排行榜
     *
     * @param limit 返回的玩家数量上限
     * @return 按击杀数降序排列的玩家列表
     */
    List<PlayerStats> getTopKills(int limit);

    /**
     * 获取死亡排行榜
     *
     * @param limit 返回的玩家数量上限
     * @return 按死亡数降序排列的玩家列表
     */
    List<PlayerStats> getTopDeaths(int limit);

    /**
     * 获取 KDR 排行榜
     *
     * @param limit 返回的玩家数量上限
     * @return 按 KDR 降序排列的玩家列表
     */
    List<PlayerStats> getTopKDR(int limit);

    /**
     * 获取连杀排行榜
     *
     * @param limit 返回的玩家数量上限
     * @return 按最佳连杀降序排列的玩家列表
     */
    List<PlayerStats> getTopKillstreak(int limit);

    /**
     * 检查 API 是否可用（插件是否已启用）
     *
     * @return 如果插件已启用则返回 true
     */
    boolean isEnabled();

    /**
     * 重载插件的配置和数据
     */
    void reload();
}
