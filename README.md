<div align="center">


<img width="1021" height="55" alt="homeplugin_author" src="https://github.com/user-attachments/assets/31d00352-ad6c-42e0-8901-8e07f6088153" />

[![](https://jitpack.io/v/fuzeblocks/HomePlugin.svg)](https://jitpack.io/#fuzeblocks/HomePlugin)
[![Discord](https://img.shields.io/discord/1394947383560900618?color=5865F2&logo=discord&logoColor=white&label=Discord)](https://discord.gg/5zJyKz6Nfm)
[![License](https://img.shields.io/badge/license-Apache--2.0-orange.svg)](https://github.com/fuzeblocks/HomePlugin?tab=Apache-2.0-1-ov-file)
[![Maven test](https://github.com/fuzeblocks/HomePlugin/actions/workflows/maven-test.yml/badge.svg?branch=main)](https://github.com/fuzeblocks/HomePlugin/actions/workflows/maven-test.yml)
[![Maven Package and publish](https://github.com/fuzeblocks/HomePlugin/actions/workflows/maven-publish.yml/badge.svg?event=release)](https://github.com/fuzeblocks/HomePlugin/actions/workflows/maven-publish.yml)
[![Deploy Javadoc to Pages](https://github.com/fuzeblocks/HomePlugin/actions/workflows/javadoc.yml/badge.svg?branch=main)](https://github.com/fuzeblocks/HomePlugin/actions/workflows/javadoc.yml)


**A lightweight, flexible home & teleport management plugin for Paper/Spigot servers**

**Stable • Fast • Modular • API-Driven**

[Features](#-features) • [Installation](#%EF%B8%8F-installation) • [Commands & Permissions](#-commands--permissions) • [Configuration](#%EF%B8%8F-configuration) • [API Documentation](https://github.com/fuzeblocks/HomePlugin/wiki)

![Usage](https://bstats.org/signatures/bukkit/HomePlugin.svg)

</div>

---

<img width="250" height="50" alt="homeplugin_features" src="https://github.com/user-attachments/assets/5e770d8e-1dea-45f5-b58f-f1a77713fb90" />


### 🏠 **Core Functionality**
- **Named Homes** - Set multiple homes with custom names and optional metadata
- **Global Spawn** - Server-wide spawn point management
- **Teleport Requests (TPA)** - Request to teleport to other players with configurable timeout
- **Random Teleport (RTP)** - Random teleport with cooldown and radius controls
- **Back Command** - Return to your previous location

### 🗄️ **Flexible Storage**
- **YAML** (default) - Simple file-based storage
- **MySQL** - Full database support for larger servers
- **Redis Caching** - Optional cross-instance cache synchronization

### 🎮 **Player Experience**
- **Interactive GUI** - Visual home management interface
- **Multiple Languages** - French, English, Spanish, Russian, Ukrainian, German, Turkish
- **Teleport Warmup** - Configurable delays with titles, messages, and particles
- **Permission-Based Limits** - Dynamic home limits via permissions

### 🔌 **Integrations**
- **PlaceholderAPI** - Rich placeholders for homes, counts, and locations
- **Vault Economy** - Optional costs for home creation, teleportation, TPA, and RTP

### 🛠️ **Administration**
- **Admin Tools** - Manage other players' homes, spawn points, and cache
- **World Restrictions** - Block home creation in specific worlds
- **Location Validation** - Prevent unfair placements
- **Modular Architecture** - Extensible plugin loader for custom modules

---

<img width="270" height="50" alt="minecraft_installation" src="https://github.com/user-attachments/assets/9b10527b-9826-4ba1-90c8-721fda75eba7" />


1. **Download** the latest release from [Releases](https://github.com/fuzeblocks/HomePlugin/releases) or build from source
2. **Place** the JAR file into your server's `plugins/` folder
3. **Start** your server to generate default configuration files
4. **Configure** `plugins/HomePlugin/config.yml` to your preferences
5. **Restart** or reload your server

### Quick Configuration Tips
- ✅ Works out-of-the-box with YAML storage
- 🗄️ For MySQL: Configure credentials in `config.yml` before restarting
- 🚀 For Redis: Set `Use-Redis: true` and ensure Redis server is accessible

---

<img width="250" height="50" alt="minecraft_commands" src="https://github.com/user-attachments/assets/cf985ede-73b7-4e8a-a3e0-5d499e929ade" />


### Commands & Permissions

Below is the full list of plugin commands (as registered in plugin.yml / code) and the permissions used by the plugin. Defaults are taken from `plugin.yml` where present.

Player commands

| Command | Description | Permission | Default |
|---|---:|---|---:|
| `/sethome [name] [info]` | Create or update a named home (default name: `home`) | `homeplugin.command.sethome` | true |
| `/home [name]` | Teleport to a home or open the home GUI | `homeplugin.command.home` | true |
| `/delhome <name>` | Delete a named home | `homeplugin.command.delhome` | true |
| `/listhome` or `/homes` | List your homes | `homeplugin.command.listhome` | true |
| `/renamehome <old> <new>` | Rename an existing home | `homeplugin.command.renamehome` | true |
| `/relocatehome <name>` | Move a home to your current location | `homeplugin.command.relocatehome` | true |
| `/back` | Return to your previous location | `homeplugin.command.back.use` | op |
| `/spawn` | Teleport to the global spawn | `homeplugin.command.spawn` | true |
| `/tpa <player>` | Send a teleport request to another player | `homeplugin.command.tpa.use` | true |
| `/tpaccept <player>` | Accept a teleport request | `homeplugin.command.tpa.use` (tpaccept) | true |
| `/tpdeny <player>` | Deny a teleport request | `homeplugin.command.tpa.use` (tpdeny) | true |
| `/rtp` | Random teleport (RTP) | `homeplugin.command.rtp` | true (when enabled) |
| `/warp <name>` | Teleport to a predefined warp | `homeplugin.command.warp.use` | true |

Admin / utility commands

| Command | Description | Permission |
|---|---:|---|
| `/setspawn` | Set the global spawn for the current world | `homeplugin.admin` or `homeplugin.admin.spawn.set` |
| `/delspawn` | Remove the global spawn for the current world | `homeplugin.admin` or `homeplugin.admin.spawn.delete` |
| `/homeadmin <player>` | Manage another player's homes (create/delete/edit) | `homeplugin.admin` or `homeplugin.admin.home.manage` |
| `/cache view` | View cache statistics | `homeplugin.admin` or `homeplugin.admin.cache.view` |
| `/cache clearall` | Clear all plugin caches | `homeplugin.admin` or `homeplugin.admin.cache.clear` |
| `/cache player <name>` | Clear cache for a specific player | `homeplugin.admin` or `homeplugin.admin.cache.player` |
| `/lang set <code>` | Set server language (e.g. `en`, `fr`) | `homeplugin.admin` or `homeplugin.command.lang` |
| `/lang update` | Update/merge language files | `homeplugin.admin` or `homeplugin.command.lang` |
| `/plugins` | List loaded HomePlugin modules/extensions | `homeplugin.admin` |
| `/update` | Trigger plugin update (shuts down server to replace jar) | `homeplugin.admin` |

Notes:
- Some permissions are grouped (e.g. `homeplugin.admin` is commonly used to gate admin commands).
- The plugin also recognizes dynamic limit permissions of the form `homeplugin.limit.<N>` (e.g. `homeplugin.limit.5`). The highest matching value between 1 and 100 is applied.
- Bypass permissions used: `homeplugin.home.bypass` (ignore limits), `homeplugin.spawn.bypassworlds` (allow /spawn in disabled worlds).

---

## 🔐 Permissions (details)

| Permission | Description | Default |
|---|---:|---:|
| `homeplugin.command.home` | Use `/home` | true |
| `homeplugin.command.sethome` | Create/modify homes | true |
| `homeplugin.command.delhome` | Delete homes | true |
| `homeplugin.command.listhome` | List homes | true |
| `homeplugin.command.renamehome` | Rename homes | true |
| `homeplugin.command.relocatehome` | Relocate homes | true |
| `homeplugin.command.spawn` | Use `/spawn` | true |
| `homeplugin.command.tpa.use` | Send/accept/deny TPA requests | true |
| `homeplugin.command.rtp` | Use RTP (if enabled) | true |
| `homeplugin.command.back.use` | Use `/back` | op |
| `homeplugin.admin` | Access admin features (cache, manage others) | op |
| `homeplugin.command.lang` | Manage language files | op |
| `homeplugin.command.warp.modify` | Create/modify warps | op |

Dynamic limits:
- `homeplugin.limit.<N>` — grants N homes. Range scanned 1..100; highest value applies.

Bypass:
- `homeplugin.home.bypass` — bypass home limits and some placement checks.
- `homeplugin.spawn.bypassworlds` — allow `/spawn` even in disabled worlds.

---

<img width="270" height="50" alt="homeplugin_config" src="https://github.com/user-attachments/assets/8f51b1c3-2c5b-4c16-91de-328482b45a39" />


### 📄 Configuration (config.yml)

The default configuration file is created at `plugins/HomePlugin/config.yml` on first start.

**[📖 View full config.yml](https://github.com/fuzeblocks/HomePlugin/blob/main/src/main/resources/config.yml)**

Example (excerpt):

```yaml
# Simplified configuration — see the full file for all options
General:
  Language: "ENGLISH" # Options: FRENCH, ENGLISH, SPANISH, RUSSIAN, UKRAINIAN, GERMAN, TURKISH
  Default-Home-Limit: 3
  Disabled-Worlds: []

Storage:
  TYPE: "YAML" # YAML or MYSQL
  MYSQL:
    HOST: "127.0.0.1"
    PORT: 3306
    USERNAME: "user"
    PASSWORD: "password"
    DATABASE: "homeplugin"

Redis:
  Use-Redis: false
  Host: "127.0.0.1"
  Port: 6379
  UseSSL: false
  Password: ""

Teleport:
  Task-Duration: 3
  Use-Title: true
  Use-Message: true
  Particles-After-Teleport: true
  Skip-If-Op: true

TPA:
  Enabled: true
  Tpa-Duration: 45

RTP:
  Enabled: true
  Cooldown-Seconds: 300
  Max-Radius: 1000

Economy:
  UseEconomy: false
  Home-Creation-Price: 0.0
  Home-Teleport-Price: 0.0
  Tpa-Request-Price: 0.0
  RTP-Price: 0.0

Features:
  Enable-Home: true
  Enable-TPA: true
  Enable-Spawn: true
  Enable-Warp: true
```

Key notes:
- Set `Storage.TYPE` to `MYSQL` and fill credentials when using a database backend.
- Enable `Use-Redis: true` for cross-instance cache synchronization.
- Adjust `Task-Duration` (teleport warmup) and RTP limits to suit gameplay balance.

---

<img width="260" height="50" alt="homeplugin_integration" src="https://github.com/user-attachments/assets/553fad7a-6d23-4e4c-881e-b9a7e4372177" />


### PlaceholderAPI

HomePlugin provides placeholders usable in other plugins:

| Placeholder | Description |
|---|---|
| `%homeplugin_homes%` | Comma-separated list of home names |
| `%homeplugin_homes_numbers%` | Total number of homes |
| `%homeplugin_has_homes%` | `true` if player has homes |
| `%homeplugin_home_location_<name>%` | Formatted location |
| `%homeplugin_home_exists_<name>%` | Check if a home exists |
| `%homeplugin_home_world_<name>%` | Home world name |
| `%homeplugin_home_coordinates_<name>%` | Raw coordinates (X Y Z) |
| `%homeplugin_home_teleport_price%` | Teleport cost |
| `%homeplugin_home_creation_price%` | Creation cost |
| `%homeplugin_tpa_request_price%` | TPA request cost |
| `%homeplugin_rtp_price%` | RTP cost |

### Vault Economy

Configure optional costs via Vault. Compatible with any Vault-compatible economy plugin.

---

<img width="250" height="50" alt="homeplugin_languages" src="https://github.com/user-attachments/assets/b2046b7e-35f1-4a8a-bfd4-376f3350096f" />

Built-in language support and customizable YAML files:

- 🇫🇷 French — code: `FRENCH` (or `fr` for `/lang set`)
- 🇬🇧 English — code: `ENGLISH` (or `en`)
- 🇪🇸 Spanish — code: `SPANISH` (or `es`) — translation by Henri Topper
- 🇷🇺 Russian — code: `RUSSIAN` (or `ru`)
- 🇺🇦 Ukrainian — code: `UKRAINIAN` (or `uk`)
- 🇩🇪 German — code: `GERMAN` (or `de`)
- 🇹🇷 Turkish — code: `TURKISH` (or `tr`) — translation by Xenetotyp3

How to add/update a language:
1. Copy an existing language file from `plugins/HomePlugin/lang/` or `src/main/resources/lang/`.
2. Translate the YAML keys while keeping the structure.
3. Reload or merge via `/lang update` (or restart the server).

---

<img width="270" height="50" alt="homeplugin_dev" src="https://github.com/user-attachments/assets/59a564f0-8254-431b-867d-7a7982bd32ea" />

HomePlugin provides a comprehensive API for developers to integrate and extend functionality.

### 📚 Documentation

- **[JavaDocs](https://fuzeblocks.github.io/HomePlugin/)** - Complete API reference
- **[Wiki](https://github.com/fuzeblocks/HomePlugin/wiki)** - Usage guides and examples
  - [Events](https://github.com/fuzeblocks/HomePlugin/wiki/Events)
  - [Home Managers](https://github.com/fuzeblocks/HomePlugin/wiki/Home-API-usage)
  - [Spawn Managers](https://github.com/fuzeblocks/HomePlugin/wiki/Spawn-API-usage)

### 📦 Dependency Management

Add HomePlugin as a dependency via JitPack:

**Maven:**
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

**Gradle (Groovy):**
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.fuzeblocks:HomePlugin:Tag'
}
```

**Gradle (Kotlin DSL):**
```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.fuzeblocks:HomePlugin:Tag")
}
```

Replace `Tag` with the desired version/release tag.

---

<img width="270" height="50" alt="minecraft_architecture" src="https://github.com/user-attachments/assets/38fa8062-0509-4ca5-8ba3-2cbc048ae26e" />


HomePlugin features a modular, performance-focused architecture:

- **Storage Abstraction** - Unified interface for YAML and MySQL backends
- **Optional Redis Layer** - Cross-instance caching and synchronization
- **Event System** - Comprehensive events for all plugin actions
- **Extension System** - Internal module loader for custom functionality
- **Permission-Driven** - Flexible limits and restrictions via permissions

---

<img width="270" height="50" alt="minecraft_compatibility" src="https://github.com/user-attachments/assets/2b8f8dc2-d722-40e6-9eb5-d009330cf8c9" />


- **Minecraft:** 1.14+ (declared `api-version: 1.14`)
- **Server:** Paper, Spigot, and derivatives
- **Java:** Java 8+
- **Soft Dependencies:** PlaceholderAPI, Vault

---

<img width="250" height="50" alt="minecraft_roadmap" src="https://github.com/user-attachments/assets/e90b5113-37ca-4f76-a887-29e1b5bad413" />


| Feature | Status |
|---------|--------|
| Per-home economy costs | ✅ Complete |
| Offline player home editing | ✅ Complete |
| Edit existing homes | ✅ Complete |
| `/back` command | ✅ Complete |
| Clickable chat messages | ✅ Complete |
| Public homes | 📝 Planned |
| Warps | ✅ Complete |
| UI/style refresh | 📝 Planned |
| BlueMap and Dynmap integration | 📝 Planned |

Have a suggestion? Open a [Discussion](https://github.com/fuzeblocks/HomePlugin/discussions) or join our [Discord](https://discord.gg/5zJyKz6Nfm)!

---

<img width="270" height="50" alt="minecraft_contributing" src="https://github.com/user-attachments/assets/8e6d9f33-35ba-4bbd-80f0-b7ac36fc33f1" />


Contributions are welcome! Here's how you can help:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Guidelines
- Use clear, descriptive commit messages
- Follow existing code style and conventions
- Test your changes thoroughly
- Update documentation as needed
- Discuss major changes in Issues or Discord first

---

<img width="250" height="50" alt="minecraft_support" src="https://github.com/user-attachments/assets/d93ca611-eeee-4afb-97b3-f7a6b2966719" />


Need help? Have questions?

- 📖 **[Wiki](https://github.com/fuzeblocks/HomePlugin/wiki)** - Documentation and guides
- 🐛 **[Issues](https://github.com/fuzeblocks/HomePlugin/issues)** - Bug reports and feature requests
- 💬 **[Discord](https://discord.gg/5zJyKz6Nfm)** - Community support and discussion
- 📧 **[Discussions](https://github.com/fuzeblocks/HomePlugin/discussions)** - General questions and ideas

---

<img width="250" height="50" alt="minecraft_license" src="https://github.com/user-attachments/assets/f0424130-cd3e-4c26-b9e0-544eb60d6ac1" />

HomePlugin is licensed under the [Apache License 2.0](https://github.com/fuzeblocks/HomePlugin?tab=Apache-2.0-1-ov-file).

---

<div align="center">

**Made with ❤️ by [fuzeblocks](https://github.com/fuzeblocks)**

</div>
