package cn.ngranked.crabpvpstatus;

import cn.ngranked.crabpvpstatus.api.CrabPvPStatsAPI;

/**
 * CrabPvPStats API 提供者。
 * <p>
 * 其他插件通过此类获取 API 实例：
 * <pre>
 * CrabPvPStatsAPI api = CrabPvPStatsProvider.getAPI();
 * if (api != null) {
 *     PlayerStats stats = api.getPlayerStats(player.getUniqueId());
 * }
 * </pre>
 */
public class CrabPvPStatsProvider {

    private static CrabPvPStatsAPI instance;

    /**
     * 注册 API 实例
     */
    public static void register(CrabPvPStatsAPI api) {
        instance = api;
    }

    /**
     * 注销 API 实例
     */
    public static void unregister() {
        instance = null;
    }

    /**
     * 获取 CrabPvPStats API 实例
     *
     * @return API 实例，如果插件未启用则返回 null
     */
    public static CrabPvPStatsAPI getAPI() {
        return instance;
    }
}
