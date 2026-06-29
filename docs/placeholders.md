<div align="center">

# 🔣 占位符 · Placeholders

> **CrabPvPStats PlaceholderAPI 完整参考**  
> *Complete PlaceholderAPI reference for CrabPvPStats*

<p>
  <a href="../README.md">🏠 主页 · Home</a> &nbsp;·&nbsp;
  <a href="README.md">📖 指南 · Guide</a> &nbsp;·&nbsp;
  <a href="config.md">⚙️ 配置 · Config</a> &nbsp;·&nbsp;
  <a href="API.md">🧩 API</a>
</p>

<p>
  <img src="https://img.shields.io/badge/Plugin_ID-crabps-%23FF6F00?style=flat-square"/>
  <img src="https://img.shields.io/badge/Dependency-PlaceholderAPI_2.11%2B-blue?style=flat-square"/>
</p>

---

</div>

## 🇨🇳 中文

### ℹ️ 基本信息

| 项目 | 值 |
|------|-----|
| **插件 ID** | `crabps` |
| **前置依赖** | PlaceholderAPI 2.11.6+（可选） |
| **占位符数量** | 4 个人 + 4 排行榜 |
| **缓存机制** | 排行榜数据 30 秒缓存，减少数据库查询 |

> ⚠️ 占位符仅在服务器安装了 PlaceholderAPI 时生效。未找到的占位符返回空字符串。

### 👤 个人占位符

用于显示**当前玩家**的统计数据，直接在计分板或聊天中使用。

| 占位符 | 返回值 | 说明 |
|--------|--------|------|
| `%crabps_kills%` | `42` | 玩家击杀数 |
| `%crabps_deaths%` | `13` | 玩家死亡数 |
| `%crabps_kdr%` | `3.23` | 玩家 KDR（死亡为 0 时显示击杀数，如 `5`） |
| `%crabps_killstreak%` | `7` | 玩家当前连杀数 |

### 🏆 排行榜占位符

格式：`%crabps_{字段}_top_{排名}%` — 返回数值  
格式：`%crabps_{字段}_topplayer_{排名}%` — 返回玩家名

排名从 **1** 开始。

#### 数值排行 — `_top_`

| 占位符 | 说明 | 示例值 |
|--------|------|--------|
| `%crabps_kills_top_1%` | 🥇 击杀第一名 | `85` |
| `%crabps_kills_top_5%` | 击杀第五名 | `42` |
| `%crabps_deaths_top_1%` | 🥇 死亡第一名 | `99` |
| `%crabps_deaths_top_5%` | 死亡第五名 | `50` |
| `%crabps_kdr_top_1%` | 🥇 KDR 第一名 | `15.50` |
| `%crabps_kdr_top_5%` | KDR 第五名 | `3.23` |
| `%crabps_killstreak_top_1%` | 🥇 最佳连杀第一名 | `25` |
| `%crabps_killstreak_top_5%` | 最佳连杀第五名 | `12` |

#### 玩家名排行 — `_topplayer_`

| 占位符 | 说明 | 示例值 |
|--------|------|--------|
| `%crabps_kills_topplayer_1%` | 🥇 击杀第一名玩家名 | `Steve` |
| `%crabps_kills_topplayer_5%` | 击杀第五名玩家名 | `Alex` |
| `%crabps_deaths_topplayer_1%` | 🥇 死亡第一名玩家名 | `Notch` |
| `%crabps_kdr_topplayer_1%` | 🥇 KDR 第一名玩家名 | `Herobrine` |
| `%crabps_killstreak_topplayer_1%` | 🥇 连杀第一名玩家名 | `Dinnerbone` |

> ℹ️ 超出排行榜范围的排名返回 `-`（例如总共只有 3 个玩家数据时，`%crabps_kills_top_10%` 返回 `-`）。

### 📝 使用示例

适用于 **Scoreboard**、**TabList**、**聊天插件**等：

```
# 📊 个人数据面板
&7击杀: &a%crabps_kills%
&7死亡: &c%crabps_deaths%
&7KDR: &e%crabps_kdr%
&7连杀: &d%crabps_killstreak%

# 🏆 排行榜面板
&6#1 击杀: &f%crabps_kills_top_1%
&6#2 击杀: &f%crabps_kills_top_2%
&6#3 击杀: &f%crabps_kills_top_3%
&6#1 KDR: &f%crabps_kdr_top_1%
```

---

## 🇬🇧 English

### ℹ️ Basics

| Item | Value |
|------|-------|
| **Plugin ID** | `crabps` |
| **Dependency** | PlaceholderAPI 2.11.6+ (optional) |
| **Placeholders** | 4 personal + 4 leaderboard |
| **Cache** | 30-second TTL on leaderboard queries |

> ⚠️ Placeholders only work when PlaceholderAPI is installed. Unknown placeholders return empty string.

### 👤 Personal Placeholders

Display the **current player's** stats. Use in scoreboards, chat, or anywhere PAPI is supported.

| Placeholder | Returns | Description |
|-------------|---------|-------------|
| `%crabps_kills%` | `42` | Player's kill count |
| `%crabps_deaths%` | `13` | Player's death count |
| `%crabps_kdr%` | `3.23` | Player's KDR (shows kills when deaths=0, e.g. `5`) |
| `%crabps_killstreak%` | `7` | Player's current killstreak |

### 🏆 Leaderboard Placeholders

Format: `%crabps_{field}_top_{rank}%` — returns **value**  
Format: `%crabps_{field}_topplayer_{rank}%` — returns **player name**

Rank is **1-based**.

#### Value Ranking — `_top_`

| Placeholder | Description | Example |
|-------------|-------------|---------|
| `%crabps_kills_top_1%` | 🥇 #1 kills | `85` |
| `%crabps_kills_top_5%` | #5 kills | `42` |
| `%crabps_deaths_top_1%` | 🥇 #1 deaths | `99` |
| `%crabps_deaths_top_5%` | #5 deaths | `50` |
| `%crabps_kdr_top_1%` | 🥇 #1 KDR | `15.50` |
| `%crabps_kdr_top_5%` | #5 KDR | `3.23` |
| `%crabps_killstreak_top_1%` | 🥇 #1 best killstreak | `25` |
| `%crabps_killstreak_top_5%` | #5 best killstreak | `12` |

#### Player Name Ranking — `_topplayer_`

| Placeholder | Description | Example |
|-------------|-------------|---------|
| `%crabps_kills_topplayer_1%` | 🥇 #1 kills player name | `Steve` |
| `%crabps_kills_topplayer_5%` | #5 kills player name | `Alex` |
| `%crabps_deaths_topplayer_1%` | 🥇 #1 deaths player name | `Notch` |
| `%crabps_kdr_topplayer_1%` | 🥇 #1 KDR player name | `Herobrine` |
| `%crabps_killstreak_topplayer_1%` | 🥇 #1 killstreak player name | `Dinnerbone` |

> ℹ️ Out-of-range ranks return `-` (e.g., `%crabps_kills_top_10%` returns `-` if only 3 players have data).

### 📝 Usage Examples

Suitable for **Scoreboard**, **TabList**, **chat plugins**, etc.:

```
# 📊 Personal Stats Panel
&7Kills: &a%crabps_kills%
&7Deaths: &c%crabps_deaths%
&7KDR: &e%crabps_kdr%
&7Streak: &d%crabps_killstreak%

# 🏆 Leaderboard Panel
&6#1 Kills: &f%crabps_kills_top_1%
&6#2 Kills: &f%crabps_kills_top_2%
&6#3 Kills: &f%crabps_kills_top_3%
&6#1 KDR: &f%crabps_kdr_top_1%
```

---

<div align="center">

🏠 [返回主页 · Back to Home](../README.md) &nbsp;·&nbsp;
📖 [指南 · Guide](README.md) &nbsp;·&nbsp;
⚙️ [配置 · Config](config.md) &nbsp;·&nbsp;
🧩 [API](API.md)

</div>
