<div align="center">

# 🏡 HomePlugin

**A lightweight, flexible home & teleport management plugin for Paper/Spigot servers**

[![](https://jitpack.io/v/fuzeblocks/HomePlugin.svg)](https://jitpack.io/#fuzeblocks/HomePlugin)
[![sponsor](https://img.shields.io/badge/Sponsor-Support%20Development-blue)](https://client.pristis.fr/aff.php?aff=2)
[![Discord](https://img.shields.io/discord/1394947383560900618?color=5865F2&logo=discord&logoColor=white&label=Discord)](https://discord.gg/5zJyKz6Nfm)
[![License](https://img.shields.io/badge/license-Apache--2.0-orange.svg)](https://github.com/fuzeblocks/HomePlugin?tab=Apache-2.0-1-ov-file)

**Stable • Fast • Modular • API-Driven**

[Features](#-features) • [Installation](#%EF%B8%8F-installation) • [Commands](#-commands) • [Configuration](#%EF%B8%8F-configuration) • [API Documentation](https://fuzeblocks.github.io/HomePlugin/)

![Usage](https://bstats.org/signatures/bukkit/HomePlugin.svg)

</div>

---

## ✨ Features

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

## ⬇️ Installation

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

## 🧾 Commands

### Player Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/sethome [name] [info]` | Create or update a home | `homeplugin.command.sethome` |
| `/home [name]` | Teleport to a home (opens GUI if no name) | `homeplugin.command.home` |
| `/delhome <name>` | Delete a home | `homeplugin.command.delhome` |
| `/listhome` | List all your homes | `homeplugin.command.listhome` |
| `/renamehome <old> <new>` | Rename an existing home | `homeplugin.command.home` |
| `/relocatehome <name>` | Move a home to your current location | `homeplugin.command.home` |
| `/back` | Return to your previous location | `homeplugin.back.use` |
| `/spawn` | Teleport to server spawn | `homeplugin.command.spawn` |
| `/tpa <player>` | Request to teleport to a player | `homeplugin.command.tpa` |
| `/tpaccept [player]` | Accept a teleport request | `homeplugin.command.tpa` |
| `/tpdeny [player]` | Deny a teleport request | `homeplugin.command.tpa` |
| `/rtp` | Random teleport | `homeplugin.command.rtp` |

### Admin Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/setspawn` | Set the global spawn point | `homeplugin.admin` |
| `/delspawn` | Remove the global spawn point | `homeplugin.admin` |
| `/homeadmin <player>` | Manage another player's homes | `homeplugin.admin` |
| `/cache view` | View cache statistics | `homeplugin.admin` |
| `/cache clearall` | Clear all plugin caches | `homeplugin.admin` |
| `/cache player <name>` | Clear cache for a specific player | `homeplugin.admin` |
| `/lang set <code>` | Set the active language | `homeplugin.lang.update` |
| `/lang update` | Update language files (merge new keys) | `homeplugin.lang.update` |
| `/plugins` | List loaded HomePlugin modules | `homeplugin.admin` |

---

## 🔐 Permissions

### Basic Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `homeplugin.command.home` | Use home commands | `true` |
| `homeplugin.command.sethome` | Create homes | `true` |
| `homeplugin.command.delhome` | Delete homes | `true` |
| `homeplugin.command.listhome` | List homes | `true` |
| `homeplugin.command.spawn` | Use spawn | `true` |
| `homeplugin.command.tpa` | Use TPA system | `true` |
| `homeplugin.command.rtp` | Use random teleport | `true` |
| `homeplugin.back.use` | Use /back command | `op` |
| `homeplugin.admin` | Admin features | `op` |
| `homeplugin.lang.update` | Manage languages | `op` |

### Dynamic Home Limits

Set custom home limits using `homeplugin.limit.<number>`:
- `homeplugin.limit.5` - 5 homes
- `homeplugin.limit.10` - 10 homes
- `homeplugin.limit.unlimited` - No limit

The plugin scans from 1-100 and applies the highest value held by the player.

### Bypass Permissions

| Permission | Description |
|------------|-------------|
| `homeplugin.bypass.limit` | Ignore home limits |
| `homeplugin.bypass.validation` | Bypass location restrictions |
| `homeplugin.bypass.cooldown` | Ignore teleport cooldowns |

---

## ⚙️ Configuration

### 📄 Configuration File

The plugin's configuration is located at `plugins/HomePlugin/config.yml` after the first server start.

**[📖 View Full config.yml](https://github.com/fuzeblocks/HomePlugin/blob/main/src/main/resources/config.yml)**

### 🔧 Configuration Overview

#### Language & Localization
Set your preferred language from: `FRENCH`, `ENGLISH`, `SPANISH`, `RUSSIAN`, `UKRAINIAN`, `GERMAN`, `TURKISH`

#### Storage Backend
Choose between **YAML** (simple file-based) or **MySQL** (database) storage:
- `TYPE: "YAML"` - Default, no additional setup required
- `TYPE: "MYSQL"` - Configure `HOST`, `PORT`, `USERNAME`, `PASSWORD`, `DATABASE`

#### Redis Cache (Optional)
Enable `Use-Redis: true` for cross-instance synchronization. Configure host, port, SSL, and password as needed.

#### Home Settings
- `Default-Home-Limit` - Base home limit (override with permissions)
- `Prevent-Unfair-Location` - Block unsafe home placements
- `Disabled-Worlds` - List of worlds where homes cannot be created

#### Teleport Warmup & Effects
- `Task-Duration` - Delay before teleport (seconds)
- `Use-Title` / `Use-Message` - Show teleport notifications
- `Particles-After-Teleport` - Spawn particles on arrival
- `Skip-If-Op` - Instant teleport for operators

#### TPA System
- `Tpa-Duration` - Request expiration time (seconds)

#### Random Teleport (RTP)
- `Enabled` - Enable/disable RTP feature
- `Cooldown-Seconds` - Cooldown between uses
- `Max-Radius` - Maximum teleport distance from spawn

#### Economy (Vault Integration)
- `UseEconomy` - Enable economy features
- `Home-Creation-Price` - Cost to create a home
- `Home-Teleport-Price` - Cost to teleport to a home
- `Tpa-Request-Price` - Cost to send TPA request
- `RTP-Price` - Cost to use random teleport

#### Feature Toggles
Enable or disable specific features:
- `Enable-TPA` - TPA system
- `Enable-Spawn` - Spawn teleportation
- `Enable-Home` - Home system

---

## 🧩 Integrations

### PlaceholderAPI

HomePlugin provides rich placeholders for use in other plugins:

| Placeholder | Description |
|-------------|-------------|
| `%homeplugin_homes%` | Comma-separated list of home names |
| `%homeplugin_homes_numbers%` | Total number of homes |
| `%homeplugin_has_homes%` | `true` if player has homes |
| `%homeplugin_home_location_<name>%` | Formatted location |
| `%homeplugin_home_exists_<name>%` | Check if home exists |
| `%homeplugin_home_world_<name>%` | Home world name |
| `%homeplugin_home_coordinates_<name>%` | Raw coordinates (X Y Z) |
| `%homeplugin_home_teleport_price%` | Teleport cost |
| `%homeplugin_home_creation_price%` | Creation cost |
| `%homeplugin_tpa_request_price%` | TPA request cost |
| `%homeplugin_rtp_price%` | RTP cost |

### Vault Economy

Configure optional costs for various actions through Vault integration. Supports any Vault-compatible economy plugin.

---

## 🌍 Supported Languages

Built-in language support with fully customizable YAML files:

- 🇫🇷 **French** (FRENCH)
- 🇬🇧 **English** (ENGLISH)
- 🇪🇸 **Spanish** (SPANISH) - _by Henri Topper_
- 🇷🇺 **Russian** (RUSSIAN)
- 🇺🇦 **Ukrainian** (UKRAINIAN)
- 🇩🇪 **German** (GERMAN)
- 🇹🇷 **Turkish** (TURKISH) - _by Xenetotyp3_

Set your language in `config.yml` or use `/lang set <LANGUAGE>` in-game.

---

## 🧑‍💻 Developer API

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

## 🧱 Architecture

HomePlugin features a modular, performance-focused architecture:

- **Storage Abstraction** - Unified interface for YAML and MySQL backends
- **Optional Redis Layer** - Cross-instance caching and synchronization
- **Event System** - Comprehensive events for all plugin actions
- **Extension System** - Internal module loader for custom functionality
- **Async Operations** - Database queries run asynchronously
- **Permission-Driven** - Flexible limits and restrictions via permissions

---

## ✅ Compatibility

- **Minecraft:** 1.14+ (declared `api-version: 1.14`)
- **Server:** Paper, Spigot, and derivatives
- **Java:** Java 8+
- **Soft Dependencies:** PlaceholderAPI, Vault

---

## 🗺️ Roadmap

| Feature | Status |
|---------|--------|
| Per-home economy costs | ✅ Complete |
| Offline player home editing | ✅ Complete |
| Edit existing homes | ✅ Complete |
| `/back` command | ✅ Complete |
| Clickable chat messages | ✅ Complete |
| Public homes | 📝 Planned |
| Warps | 📝 On the way |
| UI/style refresh | 📝 Planned |
| BlueMap and Dynmap integration | 📝 Planned |

Have a suggestion? Open a [Discussion](https://github.com/fuzeblocks/HomePlugin/discussions) or join our [Discord](https://discord.gg/5zJyKz6Nfm)!

---

## 🤝 Contributing

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

## 💬 Support

Need help? Have questions?

- 📖 **[Wiki](https://github.com/fuzeblocks/HomePlugin/wiki)** - Documentation and guides
- 🐛 **[Issues](https://github.com/fuzeblocks/HomePlugin/issues)** - Bug reports and feature requests
- 💬 **[Discord](https://discord.gg/5zJyKz6Nfm)** - Community support and discussion
- 📧 **[Discussions](https://github.com/fuzeblocks/HomePlugin/discussions)** - General questions and ideas

---

## 📄 License

HomePlugin is licensed under the [Apache License 2.0](https://github.com/fuzeblocks/HomePlugin?tab=Apache-2.0-1-ov-file).

---

<div align="center">

**Made with ❤️ by [fuzeblocks](https://github.com/fuzeblocks)**

If you find this plugin useful, consider [sponsoring](https://client.pristis.fr/aff.php?aff=2) to support development!

</div>



