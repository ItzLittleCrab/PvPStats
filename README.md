<div align="center">


# 🦀 CrabPvPStats

> **轻量级 Paper 1.21 PvP 统计插件**  
> *Lightweight Paper 1.21 PvP Statistics Plugin*

<p>
  <img src="https://img.shields.io/badge/Paper-1.21-%23F7DF1E?style=flat-square&logo=paper&logoColor=white" alt="Paper 1.21"/>
  <img src="https://img.shields.io/badge/Java-21-%23ED8B00?style=flat-square&logo=openjdk&logoColor=white" alt="Java 21"/>
  <img src="https://img.shields.io/badge/SQLite-003B57?style=flat-square&logo=sqlite&logoColor=white" alt="SQLite"/>
  <img src="https://img.shields.io/badge/PAPI-2.11%2B-%23FF6F00?style=flat-square" alt="PlaceholderAPI"/>
  <img src="https://img.shields.io/badge/License-MIT-green?style=flat-square" alt="License"/>
  <img src="https://img.shields.io/badge/version-1.0.0-blue?style=flat-square" alt="Version"/>
</p>


**中文** · [English](#english)


## <span id="chinese">🇨🇳 中文</span>

### 📋 简介

CrabPvPStats 是一款为 **Paper 1.21** 服务器打造的轻量级 PvP 统计插件。  
使用 **SQLite** 存储数据，支持 **PlaceholderAPI**，自带 **开发者 API** 和 **多语言**。

<table>
<tr>
<td align="center">⚡ <b>零依赖</b><br/><small>仅需 sqlite-jdbc（已内置）</small></td>
<td align="center">🌐 <b>双语言</b><br/><small>中文 + English 自动切换</small></td>
<td align="center">📊 <b>GUI</b><br/><small>/stats 图形界面</small></td>
<td align="center">🔌 <b>PAPI</b><br/><small>8 个占位符</small></td>
<td align="center">🧩 <b>API</b><br/><small>开发者集成接口</small></td>
</tr>
</table>

### ✨ 功能特性

| 功能                 | 说明                                                         |
| -------------------- | ------------------------------------------------------------ |
| 🎯 **PvP 追踪**       | `EntityDamageByEntityEvent` → `PlayerDeathEvent` 自动记录击杀/死亡 |
| 📈 **KDR 排行**       | 死亡为 0 时显示击杀数，SQL 排序确保 0 死亡排在前列           |
| 🖥️ **图形界面**       | 27 格 ChestInventory，击杀/死亡/KDR/连杀/最佳连杀一目了然    |
| 🔣 **PlaceholderAPI** | 4 种个人占位符 + 4 种排行榜占位符，30 秒缓存                 |
| 🌏 **国际化**         | 自动检测玩家客户端语言，内置中文和英文                       |
| 🔌 **开发者 API**     | `CrabPvPStatsAPI` 接口 + `StatsUpdateEvent` 事件             |
| 🗄️ **SQLite 存储**    | 无需外部数据库，`plugins/CrabPvPStats/data.db`               |

### 📖 文档导航

| 文档                                     | 说明                             |
| ---------------------------------------- | -------------------------------- |
| 📕 [**完整介绍**](docs/README.md)         | 功能详解、指令、权限、构建方式   |
| 🔧 [**配置参考**](docs/config.md)         | `config.yml` 配置项说明          |
| 🔣 [**占位符列表**](docs/placeholders.md) | PlaceholderAPI 占位符完整列表    |
| 🧩 [**开发者 API**](docs/API.md)          | API 接入指南、接口方法、事件监听 |

### 🚀 快速开始

```bash
# 构建插件
./mvnw clean package

# 输出文件
target/crabpvpstatus-1.0.0.jar → 放入 plugins/ 目录
```

首次启动后编辑 `plugins/CrabPvPStats/config.yml` 配置语言和权限。

---

## <span id="english">🇬🇧 English</span>

### 📋 Overview

CrabPvPStats is a lightweight **Paper 1.21** PvP statistics plugin.  
It uses **SQLite** for storage, supports **PlaceholderAPI**, and ships with a **developer API** and **built-in i18n**.

<table>
<tr>
<td align="center">⚡ <b>Zero Deps</b><br/><small>sqlite-jdbc shaded</small></td>
<td align="center">🌐 <b>Bilingual</b><br/><small>中文 + English</small></td>
<td align="center">📊 <b>GUI</b><br/><small>/stats inventory</small></td>
<td align="center">🔌 <b>PAPI</b><br/><small>8 placeholders</small></td>
<td align="center">🧩 <b>API</b><br/><small>Developer interface</small></td>
</tr>
</table>

### ✨ Features

| Feature              | Description                                                  |
| -------------------- | ------------------------------------------------------------ |
| 🎯 **PvP Tracking**   | Auto-records kills/deaths via entity damage → death chain    |
| 📈 **KDR Ranking**    | Deaths=0 displays kills count; SQL sort ensures fair ranking |
| 🖥️ **GUI**            | 27-slot ChestInventory with kills, deaths, KDR, killstreaks  |
| 🔣 **PlaceholderAPI** | 4 personal + 4 leaderboard placeholders with 30s cache       |
| 🌏 **i18n**           | Auto-detects player locale; Chinese and English built-in     |
| 🔌 **Developer API**  | `CrabPvPStatsAPI` interface + `StatsUpdateEvent`             |
| 🗄️ **SQLite**         | Zero external databases — stores in `plugins/CrabPvPStats/data.db` |

### 📖 Documentation

| Doc                                        | Description                               |
| ------------------------------------------ | ----------------------------------------- |
| 📕 [**Full Guide**](docs/README.md)         | Features, commands, permissions, building |
| 🔧 [**Configuration**](docs/config.md)      | `config.yml` reference                    |
| 🔣 [**Placeholders**](docs/placeholders.md) | Complete PAPI placeholder list            |
| 🧩 [**Developer API**](docs/API.md)         | Integration guide & API reference         |

### 🚀 Quick Start

```bash
# Build
./mvnw clean package

# Output
target/crabpvpstatus-1.0.0.jar → place in plugins/
```

Edit `plugins/CrabPvPStats/config.yml` after first run to configure language and permissions.

---

<div align="center">

🦀 **CrabPvPStats** — Made with ♥ for Minecraft Paper servers

</div>