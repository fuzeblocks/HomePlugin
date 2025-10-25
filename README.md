# 🏡 HomePlugin (by fuzeblocks)

A lightweight, flexible home/spawn/teleport management plugin for Paper/Spigot servers. HomePlugin supports named homes, global spawn, TPA requests, random teleport (RTP), optional Redis caching, YAML or MySQL storage backends, multilingual support, economy integration, and a modular extension API for internal plugin modules.

<div align="center">
  <img src="https://img.icons8.com/fluency/96/home.png" width="80" alt="Home Icon" />
  <br/>
  <strong>Stable • Fast • Modular • API‑Driven</strong>
</div>

[![](https://jitpack.io/v/fuzeblocks/HomePlugin.svg)](https://jitpack.io/#fuzeblocks/HomePlugin)

---

## 📚 Table of Contents

- [✨ Features](#features)
- [🧾 Commands](#commands)
- [🔐 Permissions](#permissions)
- [🧩 PlaceholderAPI Integration](#placeholderapi-integration)
- [💰 Vault Integration](#vault-integration)
- [🌍 Languages](#languages)
- [⬇️ Installation](#installation)
- [⚙️ Configuration](#configuration)
- [🧱 Architecture & Performance](#architecture--performance)
- [🧑‍💻 Developer / Extension API](#developer--extension-api)
- [✅ Compatibility](#compatibility)
- [🗺️ Roadmap](#roadmap)
- [🤝 Support & Contributions](#support--contributions)
- [📄 License](#license)

---

## ✨ Features

| Category            | Highlights |
|---------------------|------------|
| Homes & Spawn       | Named homes, GUI/list access, global spawn set/remove |
| Teleport Systems    | TPA requests with timeout, RTP with cooldown & radius |
| Storage             | YAML (default) or MySQL (config‑selectable) |
| Caching             | Optional Redis layer (Jedis) when enabled |
| Limits              | Per‑player home limits via permissions `homeplugin.limit.<n>` |
| Validation          | Prevent unfair placements / block disabled worlds |
| Localization        | Built‑in language system (French, English, Spanish) + editable YAML |
| PlaceholderAPI      | Rich placeholders for homes, counts, and locations |
| Admin Tools         | Manage other players’ homes, spawn, cache, language files |
| Modular Loader      | Internal plugin loader to register HomePlugin modules |
| Tasks / Warmup      | Delayed teleports with titles/messages/particles |
| Economy             | Optional costs via Vault (create/teleport/TPA/RTP) |

---

## 🧾 Commands

| Command | Description | Notes |
|---------|-------------|-------|
| `/sethome [name] [info]` | Set (or overwrite) a home at your current position | `info` optional metadata |
| `/home [name]` | Teleport to a home; opens GUI if no name is provided | GUI depends on config |
| `/home` | Open homes GUI (if enabled) |  |
| `/delhome [name]` | Delete a named home |  |
| `/listhome` | List all your homes in chat | Text alternative to GUI |
| `/renamehome [name] [newname]` | Rename an existing home |  |
| `/relocatehome [name]` | Move an existing home to your current location |  |
| `/back` | Teleport back to your previous location |  |
| `/spawn` | Teleport to global spawn | Requires spawn set |
| `/setspawn` | Set global spawn at current location | Admin |
| `/delspawn` | Remove the current global spawn | Admin |
| `/tpa <player>` | Send a teleport request to a player | Times out via `Tpa-duration` |
| `/tpaccept [player]` | Accept pending TPA request | Player optional |
| `/tpdeny [player]` | Deny pending TPA request | Player optional |
| `/rtp` | Random teleport within configured radius | Cooldown applies |
| `/homeadmin <player>` | View/manage another player’s homes | Admin |
| `/cache view` | View cache status | Admin |
| `/cache clearall` | Clear all plugin caches | Admin |
| `/cache player <name>` | Clear cache for a specific player | Admin |
| `/plugins` | List loaded HomePlugin internal modules | Admin (not Bukkit `/plugins`) |
| `/lang update` | Update base language files (add‑only merge) | Admin |
| `/lang merge` | Merge new keys into language files | Admin |
| `/lang set <code>` | Switch active language (e.g., `FRENCH`) | Admin |

---

## 🔐 Permissions

| Permission | Purpose | Default |
|------------|---------|---------|
| `homeplugin.command.home` | Use `/home` (teleport/GUI) | true |
| `homeplugin.command.sethome` | Use `/sethome` | true |
| `homeplugin.command.delhome` | Use `/delhome` | true |
| `homeplugin.command.listhome` | Use `/listhome` | true |
| `homeplugin.command.spawn` | Use `/spawn` | true |
| `homeplugin.command.tpa` | Send/accept/deny TPA | true |
| `homeplugin.admin` | Admin features (spawn, cache, manage others) | op |
| `homeplugin.lang.update` | Update language files | op |
| `homeplugin.back.use` | Use `/back` | op |
| `homeplugin.limit.<n>` | Override max homes (1..100 scanned) | permission‑based |

Suggested extras (if implemented):
- `homeplugin.bypass.limit` — ignore base limit
- `homeplugin.bypass.validation` — ignore placement restrictions
- `homeplugin.bypass.cooldown` — ignore RTP/teleport cooldowns

Dynamic limits: the plugin scans `homeplugin.limit.1` … `homeplugin.limit.100` and applies the highest held value.

---

## 🧩 PlaceholderAPI Integration

Placeholders (via `HomePluginExpansion`):

| Placeholder | Description |
|-------------|-------------|
| `%homeplugin_homes%` | Comma‑separated home names (or fallback) |
| `%homeplugin_homes_numbers%` | Number of homes |
| `%homeplugin_has_homes%` | `true` if player has ≥ 1 home |
| `%homeplugin_home_location_<name>%` | Formatted location (language‑aware) |
| `%homeplugin_home_exists_<name>%` | `true` / `false` |
| `%homeplugin_home_world_<name>%` | World name |
| `%homeplugin_home_coordinates_<name>%` | Raw coordinates `X Y Z` |

Notes:
- `<name>` is case‑insensitive.
- The expansion registers automatically when PlaceholderAPI is present.

---

## 💰 Vault Integration

Economy features are handled by `EconomyManager` and use Vault to integrate with supported economy plugins. Configure costs per action (create home, teleport, TPA, RTP) in `config.yml`.

---

## 🌍 Languages

Configured via: `Config.Language`  
Built‑in:
- FRENCH
- ENGLISH
- SPANISH

Language loading uses an enum (`Language.valueOf(...)`) and falls back to FRENCH if invalid. Customize by editing the shipped YAML files; use `/lang update`, `/lang merge`, and `/lang set` to manage versions and switch locales.

---

## ⬇️ Installation

1. Download: place the plugin JAR into your server’s `plugins/` folder.  
   - Or build from source and place the resulting JAR into `plugins/`.
2. Start the server once to generate configuration and language files.
3. Adjust `plugins/HomePlugin/config.yml` (storage, Redis, economy, limits, etc.).
4. Reload/restart the server.

Quick check:
- `/sethome` and `/home` should work immediately on YAML storage.
- If using MySQL, verify credentials and connectivity before restarting.
- If using Redis, set `UseRedis: true` and ensure the host is reachable.

---

## ⚙️ Configuration

Example structure (see `src/main/resources/config.yml`):

```yaml
Config:

  # ----------------------------------------
  # Language / Localization
  # Available: FRENCH, ENGLISH, SPANISH
  # ----------------------------------------
  Language: FRENCH

  # ----------------------------------------
  # Storage Connector
  # TYPE can be "YAML" or "MYSQL"
  # MySQL settings are used only when TYPE="MYSQL"
  # ----------------------------------------
  Connector:
    TYPE: "YAML"  # MYSQL or YAML

    # Optional (for MYSQL)
    HOST: "localhost"
    PORT: 3306
    USERNAME: "root"
    PASSWORD: ""
    DATABASE: "HomePlugin"

  # ----------------------------------------
  # Redis Cache (Optional)
  # Enable only if you need cross-instance cache/sync
  # ----------------------------------------
  Redis:
    UseRedis: false
    HOST: "localhost"
    PORT: 6379
    SSL: false
    PASSWORD: ""

  # ----------------------------------------
  # Home Settings
  # Limits, validation, and disabled worlds
  # ----------------------------------------
  Home:
    DefaultHomeLimit: 3
    PreventUnfairLocation: true
    DisabledWorlds:
      - "world_nether"
      - "world_the_end"

  # ----------------------------------------
  # Teleport Task / Warmup & Effects
  # ----------------------------------------
  Task:
    Task-duration: 3  # seconds
    UseTitle: true
    UseMessage: false
    Add-particles-after-teleport: true

  # ----------------------------------------
  # TPA (Teleport Requests)
  # ----------------------------------------
  Tpa:
    Tpa-duration: 30  # seconds

  # ----------------------------------------
  # RTP (Random Teleport)
  # ----------------------------------------
  Rtp:
    cooldown-seconds: 1000  # seconds
    max-radius: 200

  # ----------------------------------------
  # Economy (Vault)
  # Toggle costs and set prices per action
  # ----------------------------------------
  Economy:
    UseEconomy: false
    HomeCreationCost: 100.0
    Home-Teleport-Price: 50.0
    Tpa-Request-Price: 20.0
    Rtp-Price: 150.0
```

Key behaviors:
- Base home limit via `Config.Home.DefaultHomeLimit` plus permission overrides.
- TPA timeout uses `Config.Tpa.Tpa-duration`.
- Redis initializes only when `UseRedis: true`.
- Teleport warmup via `Task-duration`; titles/messages/particles are optional.
- Worlds listed in `DisabledWorlds` cannot be used for homes/teleports (validation).

---

## 🧱 Architecture & Performance

- Unified managers with storage abstraction:
  - Homes: `HomeManager` → YAML (`HomeYMLManager`) or SQL (`HomeSQLManager`)
  - Spawns: `SpawnManager` → YAML (`SpawnYMLManager`) or SQL (`SpawnSQLManager`)
- Optional Redis (JedisPooled) caching/sync when enabled
- MySQL path enabled by `Config.Connector.TYPE=MYSQL`
- Extension system (`PluginLoader` / `PluginManager`) for internal modules
- PlaceholderAPI soft‑dependency
- Permission‑driven home limits (`homeplugin.limit.<n>`)
- Teleport warmups and TPA expiration via Bukkit scheduler

---

## 🧑‍💻 Developer / Extension API

- Events, managers, and language tools with documented APIs
- Detect active backend:
  - From events: `SyncMethod getType()`
  - Globally: `fr.fuzeblocks.homeplugin.HomePlugin.getRegistrationType()`

Docs:
- API Docs (Javadoc): [HomePlugin JavaDoc](https://fuzeblocks.github.io/HomePlugin/)
- Developer Docs: [Events](docs/EVENTS.md) • [Home Managers](docs/HOME_MANAGERS.md) • [Spawn Managers](docs/SPAWN_MANAGERS.md) • [Language Management](docs/LANGUAGE_MANAGEMENT.md)

Use JitPack to depend on the API (replace `Tag` with a release/tag):

Maven:
```xml
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>
<dependency>
  <groupId>com.github.fuzeblocks</groupId>
  <artifactId>HomePlugin</artifactId>
  <version>Tag</version>
</dependency>
```

Gradle (Groovy):
```groovy
repositories { maven { url 'https://jitpack.io' } }
dependencies { implementation 'com.github.fuzeblocks:HomePlugin:Tag' }
```

Gradle (Kotlin):
```kotlin
repositories { maven("https://jitpack.io") }
dependencies { implementation("com.github.fuzeblocks:HomePlugin:Tag") }
```

---

## ✅ Compatibility

- Declared `api-version: 1.14` (plugin.yml)
- Built for modern Paper/Spigot derivatives
- Soft‑dependency: PlaceholderAPI
- Storage: YAML (default) or MySQL (when enabled)

---

## 🗺️ Roadmap

| Feature                           | Status     |
|-----------------------------------|------------|
| Per‑home economy cost             | ✅ Complete |
| Offline player home editing       | 📝 Planned  |
| Edit existing homes               | ✅ Complete |
| Public homes                      | 📝 Planned  |
| `/back` command                   | ✅ Complete |
| Clickable chat messages           | 📝 Planned  |
| Warps                             | 📝 Planned  |
| UI/style refresh                  | 📝 Planned  |
| BlueMap and Dynmap integration    | 📝 Planned  |

Suggestions welcome via Discussions or Discord.

---

## 🤝 Support & Contributions

- Issues: Use GitHub Issues for bugs/feature requests
- Discord: [Join the server](https://discord.gg/5zJyKz6Nfm)
- Pull Requests: Use clear commit messages; discuss large changes beforehand

Contribution flow:
1. Fork
2. Create a feature branch
3. Commit changes
4. Open a PR referencing related issues

---

## 📄 License

[Apache‑2.0](https://github.com/fuzeblocks/HomePlugin?tab=Apache-2.0-1-ov-file)
