<div align="center">

# 🧩 开发者 API · Developer API

> **CrabPvPStats API 接入指南与接口参考**  
> *Integration guide & complete API reference*

<p>
  <a href="../README.md">🏠 主页 · Home</a> &nbsp;·&nbsp;
  <a href="README.md">📖 指南 · Guide</a> &nbsp;·&nbsp;
  <a href="config.md">⚙️ 配置 · Config</a> &nbsp;·&nbsp;
  <a href="placeholders.md">🔣 占位符 · Placeholders</a>
</p>

<p>
  <img src="https://img.shields.io/badge/API_Stable-v1.0.0-green?style=flat-square"/>
  <img src="https://img.shields.io/badge/Java-21+-blue?style=flat-square"/>
</p>

---

</div>

## 🇨🇳 中文

### 📦 添加依赖

在您的插件项目的 `pom.xml` 中添加：

```xml
<!-- JitPack 仓库 -->
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<!-- CrabPvPStats API -->
<dependency>
    <groupId>com.github.ItzLittleCrab</groupId>
    <artifactId>CrabPvPStats</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>
```

> 💡 `plugin.yml` 中添加 `depend: [CrabPvPStats]` 或 `softdepend: [CrabPvPStats]` 确保加载顺序。

### 🚀 快速开始

#### 获取 API 实例

```java
import cn.ngranked.crabpvpstats.CrabPvPStatsProvider;
import cn.ngranked.crabpvpstats.api.CrabPvPStatsAPI;

CrabPvPStatsAPI api = CrabPvPStatsProvider.getAPI();
if (api != null && api.isEnabled()) {
    // 插件已启用，安全使用 API
}
```

#### 读取玩家数据

```java
// 同步读取（主线程安全）
PlayerStats stats = api.getPlayerStats(player.getUniqueId());
int kills = stats.getKills();
int deaths = stats.getDeaths();
double kdr = stats.getKdr();
String kdrFormatted = stats.getKdrFormatted();  // "2.50" 或 "5"（无死亡）

// 异步读取（不影响主线程）
api.getPlayerStatsAsync(player.getUniqueId()).thenAccept(stats -> {
    // 处理统计数据
});
```

#### 修改玩家数据

```java
// 添加一次击杀
api.addKill(playerUuid);
// 添加一次死亡
api.addDeath(playerUuid);
// 重置所有数据为 0
api.resetStats(playerUuid);
// 直接设置任意数据
api.setStats(playerUuid, stats);
```

#### 排行榜查询

```java
List<PlayerStats> topKills = api.getTopKills(10);       // 击杀榜
List<PlayerStats> topDeaths = api.getTopDeaths(10);     // 死亡榜
List<PlayerStats> topKDR = api.getTopKDR(10);           // KDR 榜
List<PlayerStats> topKs = api.getTopKillstreak(10);     // 连杀榜
```

#### 监听统计更新事件

```java
import cn.ngranked.crabpvpstats.api.StatsUpdateEvent;

@EventHandler
public void onStatsUpdate(StatsUpdateEvent event) {
    UUID playerUuid = event.getPlayerUuid();
    PlayerStats stats = event.getStats();
    // 处理统计变更，例如更新计分板
}
```

### 📋 PlayerStats 数据模型

| 方法 | 返回类型 | 说明 |
|------|----------|------|
| `getKills()` | `int` | 击杀数 |
| `getDeaths()` | `int` | 死亡数 |
| `getKdr()` | `double` | KDR 值（死亡=0 时 = 击杀数） |
| `getKdrFormatted()` | `String` | 格式化 KDR 字符串 |
| `getKillstreak()` | `int` | 当前连杀 |
| `getBestKillstreak()` | `int` | 历史最佳连杀 |
| `getPlayerName()` | `String` | 玩家名 |
| `getPlayerUuid()` | `UUID` | 玩家 UUID |

### 📑 API 接口总览

| 方法 | 返回 | 说明 |
|------|------|------|
| `getPlayerStats(UUID)` | `PlayerStats` | 获取玩家统计（UUID） |
| `getPlayerStats(String)` | `PlayerStats` | 获取玩家统计（名称，离线查找） |
| `getPlayerStatsAsync(UUID)` | `CompletableFuture<PlayerStats>` | 异步获取统计 |
| `addKill(UUID)` | `void` | 增加一次击杀 |
| `addDeath(UUID)` | `void` | 增加一次死亡 |
| `resetStats(UUID)` | `void` | 重置所有统计为 0 |
| `setStats(UUID, PlayerStats)` | `void` | 设置任意统计数据 |
| `getTopKills(int)` | `List<PlayerStats>` | 击杀排行榜 |
| `getTopDeaths(int)` | `List<PlayerStats>` | 死亡排行榜 |
| `getTopKDR(int)` | `List<PlayerStats>` | KDR 排行榜 |
| `getTopKillstreak(int)` | `List<PlayerStats>` | 连杀排行榜 |
| `isEnabled()` | `boolean` | API 是否可用 |
| `reload()` | `void` | 触发重载 |

---

## 🇬🇧 English

### 📦 Add Dependency

Add to your `pom.xml`:

```xml
<!-- JitPack Repository -->
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<!-- CrabPvPStats API -->
<dependency>
    <groupId>com.github.ItzLittleCrab</groupId>
    <artifactId>CrabPvPStats</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>
```

> 💡 Add `depend: [CrabPvPStats]` or `softdepend: [CrabPvPStats]` to your `plugin.yml` to ensure load order.

### 🚀 Quick Start

#### Get API Instance

```java
import cn.ngranked.crabpvpstats.CrabPvPStatsProvider;
import cn.ngranked.crabpvpstats.api.CrabPvPStatsAPI;

CrabPvPStatsAPI api = CrabPvPStatsProvider.getAPI();
if (api != null && api.isEnabled()) {
    // Plugin is enabled, safe to use API
}
```

#### Read Player Stats

```java
// Sync read (main thread safe)
PlayerStats stats = api.getPlayerStats(player.getUniqueId());
int kills = stats.getKills();
int deaths = stats.getDeaths();
double kdr = stats.getKdr();
String kdrFormatted = stats.getKdrFormatted();  // "2.50" or "5" (no deaths)

// Async read
api.getPlayerStatsAsync(player.getUniqueId()).thenAccept(stats -> {
    // handle stats
});
```

#### Modify Stats

```java
// Add one kill
api.addKill(playerUuid);
// Add one death
api.addDeath(playerUuid);
// Reset all stats to zero
api.resetStats(playerUuid);
// Set arbitrary stats
api.setStats(playerUuid, stats);
```

#### Leaderboard Queries

```java
List<PlayerStats> topKills = api.getTopKills(10);       // Kills leaderboard
List<PlayerStats> topDeaths = api.getTopDeaths(10);     // Deaths leaderboard
List<PlayerStats> topKDR = api.getTopKDR(10);           // KDR leaderboard
List<PlayerStats> topKs = api.getTopKillstreak(10);     // Killstreak leaderboard
```

#### Listen for Stats Update Events

```java
import cn.ngranked.crabpvpstats.api.StatsUpdateEvent;

@EventHandler
public void onStatsUpdate(StatsUpdateEvent event) {
    UUID playerUuid = event.getPlayerUuid();
    PlayerStats stats = event.getStats();
    // React to stats change, e.g. update scoreboard
}
```

### 📋 PlayerStats Model

| Method | Return | Description |
|--------|--------|-------------|
| `getKills()` | `int` | Kill count |
| `getDeaths()` | `int` | Death count |
| `getKdr()` | `double` | KDR value (= kills when deaths=0) |
| `getKdrFormatted()` | `String` | Formatted KDR string |
| `getKillstreak()` | `int` | Current killstreak |
| `getBestKillstreak()` | `int` | Best killstreak |
| `getPlayerName()` | `String` | Player name |
| `getPlayerUuid()` | `UUID` | Player UUID |

### 📑 API Interface Summary

| Method | Return | Description |
|--------|--------|-------------|
| `getPlayerStats(UUID)` | `PlayerStats` | Get stats by UUID |
| `getPlayerStats(String)` | `PlayerStats` | Get stats by name (offline lookup) |
| `getPlayerStatsAsync(UUID)` | `CompletableFuture<PlayerStats>` | Async stats lookup |
| `addKill(UUID)` | `void` | Increment kill count |
| `addDeath(UUID)` | `void` | Increment death count |
| `resetStats(UUID)` | `void` | Reset all stats to zero |
| `setStats(UUID, PlayerStats)` | `void` | Set arbitrary stats |
| `getTopKills(int)` | `List<PlayerStats>` | Kills leaderboard |
| `getTopDeaths(int)` | `List<PlayerStats>` | Deaths leaderboard |
| `getTopKDR(int)` | `List<PlayerStats>` | KDR leaderboard |
| `getTopKillstreak(int)` | `List<PlayerStats>` | Killstreak leaderboard |
| `isEnabled()` | `boolean` | Whether API is available |
| `reload()` | `void` | Trigger reload |

---

<div align="center">

🏠 [返回主页 · Back to Home](../README.md) &nbsp;·&nbsp;
📖 [指南 · Guide](README.md) &nbsp;·&nbsp;
⚙️ [配置 · Config](config.md) &nbsp;·&nbsp;
🔣 [占位符 · Placeholders](placeholders.md)

</div>
