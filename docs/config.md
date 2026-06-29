<div align="center">

# ⚙️ 配置参考 · Configuration Reference

> **config.yml 配置项完整说明**  
> *Complete reference for all config.yml options*

<p>
  <a href="https://github.com/ItzLittleCrab/PvPStats/blob/master/README.md">🏠 主页 · Home</a> &nbsp;·&nbsp;
  <a href="README.md">📖 指南 · Guide</a> &nbsp;·&nbsp;
  <a href="placeholders.md">🔣 占位符 · Placeholders</a> &nbsp;·&nbsp;
  <a href="API.md">🧩 API</a>
</p>

---

</div>

## 🇨🇳 中文

### 📄 文件位置

```
plugins/CrabPvPStats/config.yml
```

插件首次启动时自动生成此文件。

### 🔧 默认配置

```yaml
# 语言设置
# 可选值: "auto" (自动检测), "zh-cn" (中文), "us-en" (英文)
language: auto

# 是否允许玩家查看自己的数据
allow-self-stats: true

# 是否允许玩家查看其他玩家的数据
allow-other-stats: true

# 排行榜返回的最大玩家数量
top-players-count: 10
```

### 📋 配置项说明

<details open>
<summary><b><code>language</code></b> — 语言设置</summary>

| 值 | 效果 |
|----|------|
| `auto` | 服务端默认加载 `us-en.yml`；玩家 GUI 语言根据客户端 locale 自动切换 |
| `zh-cn` | 强制使用中文消息 |
| `us-en` | 强制使用英文消息 |

> 💡 提示：`auto` 模式下服务端日志为英文，玩家看到的 UI 文字根据其 Minecraft 客户端语言自动匹配。
</details>

<details open>
<summary><b><code>allow-self-stats</code></b> — 自行查看权限</summary>

| 值 | 效果 |
|----|------|
| `true` | 玩家可以使用 `/stats` 查看自己的数据 |
| `false` | 禁用 `/stats` 指令的个人查看功能 |

> 💡 设为 `false` 后可通过命令方块或未来的管理指令实现管理员专用查看。
</details>

<details open>
<summary><b><code>allow-other-stats</code></b> — 查看他人权限</summary>

| 值 | 效果 |
|----|------|
| `true` | 拥有 `crabpvpstats.stats.others` 权限的玩家可使用 `/stats <玩家名>` |
| `false` | 禁止查看其他玩家的数据 |

> 💡 该选项独立于 `allow-self-stats`，两者可分别控制。
</details>

<details open>
<summary><b><code>top-players-count</code></b> — 排行榜数量</summary>

- **类型**: 整数
- **默认值**: `10`
- **范围**: 1 ~ 100

> 控制 PlaceholderAPI 排行榜占位符和数据查询中返回的最大玩家数量。值越大查询耗时略增。
</details>

### 🔄 重载配置

修改 `config.yml` 后需要重启服务器或重载插件：

```bash
# 使用 PlugMan（如已安装）
/plugman reload CrabPvPStats

# 或直接重启服务器
/stop && 启动脚本
```

---

## 🇬🇧 English

### 📄 File Location

```
plugins/CrabPvPStats/config.yml
```

Auto-generated on first plugin startup.

### 🔧 Default Configuration

```yaml
# Language setting
# Options: "auto" (detect from client), "zh-cn", "us-en"
language: auto

# Allow players to view their own stats via /stats
allow-self-stats: true

# Allow players to view other players' stats via /stats <player>
allow-other-stats: true

# Number of top players to return in leaderboard queries
top-players-count: 10
```

### 📋 Field Reference

<details open>
<summary><b><code>language</code></b> — Language Setting</summary>

| Value | Effect |
|-------|--------|
| `auto` | Server loads `us-en.yml` by default; GUI language matches player's Minecraft client locale |
| `zh-cn` | Force Chinese messages |
| `us-en` | Force English messages |

> 💡 In `auto` mode, server logs stay in English while players see UI text matching their client language.
</details>

<details open>
<summary><b><code>allow-self-stats</code></b> — Self-View Permission</summary>

| Value | Effect |
|-------|--------|
| `true` | Players can use `/stats` to see their own data |
| `false` | Disables self-stat viewing via `/stats` |

> 💡 When disabled, stats can still be accessed via command blocks or future admin commands.
</details>

<details open>
<summary><b><code>allow-other-stats</code></b> — Other-View Permission</summary>

| Value | Effect |
|-------|--------|
| `true` | Players with `crabpvpstats.stats.others` can use `/stats <player>` |
| `false` | Disables viewing other players' stats entirely |

> 💡 This is independent from `allow-self-stats` — both can be toggled separately.
</details>

<details open>
<summary><b><code>top-players-count</code></b> — Leaderboard Size</summary>

- **Type**: Integer
- **Default**: `10`
- **Range**: 1 ~ 100

> Controls the max number of players returned in PlaceholderAPI leaderboard queries. Higher values result in slightly slower queries.
</details>

### 🔄 Reloading

After editing `config.yml`, restart the server or reload the plugin:

```bash
# With PlugMan (if installed)
/plugman reload CrabPvPStats

# Or simply restart
/stop && start script
```

---

<div align="center">

🏠 [返回主页 · Back to Home](https://github.com/ItzLittleCrab/PvPStats/blob/master/README.md) &nbsp;·&nbsp;
📖 [指南 · Guide](https://github.com/ItzLittleCrab/PvPStats/blob/master/README.md) &nbsp;·&nbsp;
🔣 [占位符 · Placeholders](https://github.com/ItzLittleCrab/PvPStats/blob/master/docs/placeholders.md) &nbsp;·&nbsp;
🧩 [API](https://github.com/ItzLittleCrab/PvPStats/blob/master/docs/API.md)

</div>
