<div align="center">

# 📖 CrabPvPStats 完整指南 · Full Guide

> **Paper 1.21 轻量级 PvP 统计插件 · 功能详解、指令与权限**  
> *Paper 1.21 lightweight PvP statistics plugin — features, commands & permissions*

<p>
  <a href="https://github.com/ItzLittleCrab/PvPStats/blob/master/README.md">🏠 返回主页 · Back to Home</a> &nbsp;·&nbsp;
  <a href="config.md">⚙️ 配置 · Config</a> &nbsp;·&nbsp;
  <a href="placeholders.md">🔣 占位符 · Placeholders</a> &nbsp;·&nbsp;
  <a href="API.md">🧩 API</a>
</p>

---

</div>

## 🇨🇳 中文

### 📋 简介

CrabPvPStats 是一款专为 **Paper 1.21** 服务器设计的轻量级 PvP 统计插件，无需任何外部依赖即可运行。它自动追踪玩家间的击杀与死亡，提供直观的图形界面和丰富的占位符支持。

### ✨ 核心功能

| 功能模块 | 描述 |
|----------|------|
| 🎯 **自动 PvP 追踪** | 监听 `EntityDamageByEntityEvent` 记录伤害来源，在 `PlayerDeathEvent` 中自动记录击杀/死亡 |
| 📊 **KDR 计算** | 死亡为 0 时显示击杀数；数据库排序使用 `CAST(kills AS REAL) / (deaths + 1)` 确保公平排行 |
| 🖥️ **图形界面** | `/stats` 打开 27 格 ChestInventory，展示击杀、死亡、KDR、当前连杀、最佳连杀 |
| 🔣 **PlaceholderAPI** | 4 个个人占位符 + 4 个排行榜占位符（支持 `_top_{N}`），30 秒缓存防性能损耗 |
| 🌏 **多语言** | 内置中文 (`zh-cn`) 和英文 (`us-en`)，自动检测玩家客户端语言 |
| 🔌 **开发者 API** | `CrabPvPStatsAPI` 接口 + `StatsUpdateEvent` + `CrabPvPStatsProvider` 静态提供者 |
| 🗄️ **SQLite** | 数据存储在 `plugins/CrabPvPStats/data.db`，插件关闭时自动保存缓存 |

### 🎮 指令

| 指令 | 权限 | 描述 |
|------|------|------|
| `/stats` | `crabpvpstats.stats` | 查看自己的 PvP 统计数据 |
| `/stats <玩家名>` | `crabpvpstats.stats.others` | 查看其他玩家的统计 |
| `/crabpvpstats help` | `crabpvpstats.admin` | 查看管理指令帮助 |
| `/crabpvpstats reload` | `crabpvpstats.admin` | 重载配置和语言文件 |
| `/crabpvpstats <玩家> add <kills\|deaths\|killstreak> <数值>` | `crabpvpstats.admin` | 增加玩家某项数据 |
| `/crabpvpstats <玩家> set <kills\|deaths\|killstreak> <数值>` | `crabpvpstats.admin` | 设置玩家某项数据 |
| `/cps ...` | `crabpvpstats.admin` | `/crabpvpstats` 的简写别名 |

### 🔐 权限

| 权限节点 | 默认 | 描述 |
|----------|------|------|
| `crabpvpstats.stats` | `true`（所有玩家） | 允许使用 `/stats` 指令 |
| `crabpvpstats.stats.others` | `op`（管理员） | 允许查看其他玩家的数据 |
| `crabpvpstats.admin` | `op`（管理员） | 管理权限，允许使用 `/crabpvpstats` 和 `/cps` |

### 🏗️ 构建

```bash
# Maven 构建（推荐）
./mvnw clean package

# 或使用系统 Maven
mvn clean package
```

输出文件：`target/crabpvpstats-1.1.1.jar`（约 13 MB，已包含 sqlite-jdbc）

### 🖥️ GUI 界面预览

```
┌─────────────────────────────────────┐
│          §6我的 PvP 数据             │  ← 27 格 ChestInventory
├───┬───┬───┬───┬───┬───┬───┬───┬───┤
│ ⚔ │ 💀 │ 🏆 │   │ 🔥 │ ⭐ │   │   │   │  ← 第一行：统计项
│ [击杀] [死亡] [KDR]    [连杀] [最佳]  │
├───┼───┼───┼───┼───┼───┼───┼───┼───┤
│   │   │   │   │   │   │   │   │   │  ← 第二行：空
├───┼───┼───┼───┼───┼───┼───┼───┼───┤
│ ░ │ ░ │ ░ │ ░ │ ░ │ ░ │ ░ │ ░ │ ░ │  ← 第三行：玻璃板
└───┴───┴───┴───┴───┴───┴───┴───┴───┘
```

- **槽位 0**：⚔ 击杀数（钻石剑）
- **槽位 1**：💀 死亡数（骷髅头）
- **槽位 2**：🏆 KDR（金锭）
- **槽位 4**：🔥 当前连杀（烈焰粉）
- **槽位 5**：⭐ 最佳连杀（下界之星）

---

## 🇬🇧 English

### 📋 Overview

CrabPvPStats is a lightweight Paper 1.21 PvP statistics plugin with zero external runtime dependencies. It automatically tracks kills and deaths between players, provides an intuitive GUI, and offers rich PlaceholderAPI support.

### ✨ Features

| Module | Description |
|--------|-------------|
| 🎯 **PvP Tracking** | Listens to `EntityDamageByEntityEvent` for damage sources, records kills/deaths on `PlayerDeathEvent` |
| 📊 **KDR Calculation** | Displays kills count when deaths=0; SQL uses `CAST(kills AS REAL) / (deaths + 1)` for fair ranking |
| 🖥️ **GUI** | `/stats` opens a 27-slot ChestInventory showing kills, deaths, KDR, current and best killstreak |
| 🔣 **PlaceholderAPI** | 4 personal + 4 leaderboard placeholders (with `_top_{N}`), 30s cache for performance |
| 🌏 **i18n** | Built-in Chinese (`zh-cn`) and English (`us-en`), auto-detects player locale |
| 🔌 **Developer API** | `CrabPvPStatsAPI` interface + `StatsUpdateEvent` + `CrabPvPStatsProvider` |
| 🗄️ **SQLite** | Data stored in `plugins/CrabPvPStats/data.db`, auto-saved on plugin disable |

### 🎮 Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/stats` | `crabpvpstats.stats` | View your own PvP stats |
| `/stats <player>` | `crabpvpstats.stats.others` | View another player's stats |
| `/crabpvpstats help` | `crabpvpstats.admin` | Show admin command help |
| `/crabpvpstats reload` | `crabpvpstats.admin` | Reload config and language files |
| `/crabpvpstats <player> add <kills\|deaths\|killstreak> <amount>` | `crabpvpstats.admin` | Add to a player's stat |
| `/crabpvpstats <player> set <kills\|deaths\|killstreak> <value>` | `crabpvpstats.admin` | Set a player's stat |
| `/cps ...` | `crabpvpstats.admin` | Shorthand alias for `/crabpvpstats` |

### 🔐 Permissions

| Permission Node | Default | Description |
|----------------|---------|-------------|
| `crabpvpstats.stats` | `true` (all) | Allows use of `/stats` |
| `crabpvpstats.stats.others` | `op` | Allows viewing other players' data |
| `crabpvpstats.admin` | `op` | Admin access for `/crabpvpstats` and `/cps` |

### 🏗️ Building

```bash
# Maven build (recommended)
./mvnw clean package

# Or with system Maven
mvn clean package
```

Output: `target/crabpvpstats-1.1.1.jar` (~13 MB, sqlite-jdbc shaded)

---

<div align="center">

📕 [返回主页 · Back to Home](https://github.com/ItzLittleCrab/PvPStats/blob/master/README.md) &nbsp;·&nbsp;
⚙️ [配置 · Config](https://github.com/ItzLittleCrab/PvPStats/blob/master/docs/config.md) &nbsp;·&nbsp;
🔣 [占位符 · Placeholders](https://github.com/ItzLittleCrab/PvPStats/blob/master/docs/placeholders.md) &nbsp;·&nbsp;
🧩 [API](https://github.com/ItzLittleCrab/PvPStats/blob/master/docs/API.md)

</div>
