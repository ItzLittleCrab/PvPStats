package cn.ngranked.crabpvpstats.database;

import cn.ngranked.crabpvpstats.model.PlayerStats;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * SQLite 数据库管理器 — 处理 player_stats 表的 CRUD 和排行查询
 */
public class DatabaseManager {

    private final JavaPlugin plugin;
    private Connection connection;

    public DatabaseManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * 初始化数据库连接并创建表结构
     */
    public void init() {
        try {
            // 确保 data 文件夹存在
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }

            Class.forName("org.sqlite.JDBC");
            String dbPath = plugin.getDataFolder().getAbsolutePath() + "/data.db";
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

            createTable();
            plugin.getLogger().info("Database initialized at " + dbPath);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize database", e);
        }
    }

    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS player_stats (" +
                "player_uuid VARCHAR(36) PRIMARY KEY," +
                "player_name VARCHAR(16) NOT NULL," +
                "kills INT DEFAULT 0," +
                "deaths INT DEFAULT 0," +
                "killstreak INT DEFAULT 0," +
                "best_killstreak INT DEFAULT 0," +
                "last_updated BIGINT NOT NULL" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    /**
     * 获取指定玩家的统计数据
     */
    public PlayerStats getPlayerStats(UUID uuid) {
        String sql = "SELECT * FROM player_stats WHERE player_uuid = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to get stats for " + uuid, e);
        }
        return new PlayerStats(uuid, "");
    }

    /**
     * 保存玩家的统计数据（INSERT OR REPLACE）
     */
    public void savePlayerStats(PlayerStats stats) {
        String sql = "INSERT OR REPLACE INTO player_stats " +
                "(player_uuid, player_name, kills, deaths, killstreak, best_killstreak, last_updated) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, stats.getPlayerUuid().toString());
            ps.setString(2, stats.getPlayerName());
            ps.setInt(3, stats.getKills());
            ps.setInt(4, stats.getDeaths());
            ps.setInt(5, stats.getKillstreak());
            ps.setInt(6, stats.getBestKillstreak());
            ps.setLong(7, stats.getLastUpdated());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to save stats for " + stats.getPlayerName(), e);
        }
    }

    /**
     * 获取击杀排行榜
     */
    public List<PlayerStats> getTopKills(int limit) {
        return queryTop("SELECT * FROM player_stats ORDER BY kills DESC LIMIT ?", limit);
    }

    /**
     * 获取死亡排行榜
     */
    public List<PlayerStats> getTopDeaths(int limit) {
        return queryTop("SELECT * FROM player_stats ORDER BY deaths DESC LIMIT ?", limit);
    }

    /**
     * 获取 KDR 排行榜
     * 死亡为 0 时按击杀数/(死亡+1) 排序，确保 0 死亡的玩家排在前列
     */
    public List<PlayerStats> getTopKDR(int limit) {
        return queryTop("SELECT * FROM player_stats ORDER BY CAST(kills AS REAL) / (deaths + 1) DESC LIMIT ?", limit);
    }

    /**
     * 获取连杀排行榜（按最佳连杀降序）
     */
    public List<PlayerStats> getTopKillstreak(int limit) {
        return queryTop("SELECT * FROM player_stats ORDER BY best_killstreak DESC LIMIT ?", limit);
    }

    private List<PlayerStats> queryTop(String sql, int limit) {
        List<PlayerStats> results = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to query top players", e);
        }
        return results;
    }

    private PlayerStats mapResultSet(ResultSet rs) throws SQLException {
        return new PlayerStats(
                UUID.fromString(rs.getString("player_uuid")),
                rs.getString("player_name"),
                rs.getInt("kills"),
                rs.getInt("deaths"),
                rs.getInt("killstreak"),
                rs.getInt("best_killstreak"),
                rs.getLong("last_updated")
        );
    }

    /**
     * 关闭数据库连接
     */
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to close database connection", e);
            }
        }
    }
}
