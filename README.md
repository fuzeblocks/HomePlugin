# 🏡 HomePlugin (by fuzeblocks)

A lightweight, flexible home/spawn/teleport management plugin for Paper/Spigot servers. HomePlugin supports named homes, global spawn, TPA requests, random teleport (RTP), optional Redis caching, YAML or MySQL storage backends, multilingual support, economy integration, and a modular extension API for adding internal plugin modules.

<div align="center">
  <img src="https://img.icons8.com/fluency/96/home.png" width="80" alt="Home Icon" />
  <br/>
  <strong>Stable • Fast • Modular • API-Driven</strong>
</div>

---

## 📚 Table of Contents
- [✨ Features](#-features)
- [🧾 Commands](#-commands)
- [🔐 Permissions](#-permissions)
- [🧩 PlaceholderAPI Integration](#-placeholderapi-integration)
- [🧩 Vault Integration](#-vault-integration)
- [🌍 Languages](#-languages)
- [⚙️ Configuration](#️-configuration)
- [🧱 Architecture & Performance](#-architecture--performance)
- [🧑‍💻 Developer / Extension API](#-developer--extension-api)
- [✅ Compatibility](#-compatibility)
- [🗺️ Roadmap](#-roadmap)
- [🤝 Support & Contributions](#-support--contributions)
- [📄 License](#-license)

---

## ✨ Features

| Category            | Highlights |
|---------------------|-----------|
| Homes & Spawn       | Named homes, GUI / list access, global spawn set/remove |
| Teleport Systems    | TPA requests with timeout, RTP with cooldown & radius |
| Storage             | YAML (default) or MySQL (async registration path) |
| Caching             | Optional Redis layer (Jedis) if enabled in config |
| Limits              | Dynamic per-player home limits via permissions `homeplugin.limit.<n>` |
| Validation          | Prevent unfair placements / block disabled worlds |
| Localization        | Built-in language system (French, English, Spanish) + external files |
| PlaceholderAPI      | Rich placeholders for homes, counts, locations |
| Admin Tools         | Manage other players’ homes, spawn, cache, language files |
| Modular Loader      | Internal plugin loader to register additional HomePlugin modules |
| Tasks / Warmup      | Configurable teleport warmup with titles/messages/particles |
| Economy             | Configurable economy, using Vault |

---

## 🧾 Commands

| Command | Description | Notes |
|---------|-------------|-------|
| `/sethome [name] [info]` | Set (or overwrite) a home at your current position. | `info` optional metadata (if used) |
| `/home [name]` | Teleport to a home; GUI opens if no name is provided. | GUI behavior depends on config/implementation |
| `/home` | Open homes GUI (if enabled). |  |
| `/delhome [name]` | Delete a named home. |  |
| `/listhome` | List all your homes in chat. | Text alternative to GUI |
| `/spawn` | Teleport to global spawn. | Requires spawn set |
| `/setspawn` | Set global spawn at current location. | Admin |
| `/delspawn` | Remove current global spawn. | Admin |
| `/tpa <player>` | Send teleport request to a player. | Times out (`Tpa-duration`) |
| `/tpaccept [player]` | Accept pending TPA request. | Player optional |
| `/tpdeny [player]` | Deny pending TPA request. | Player optional |
| `/rtp` | Random teleport within configured radius. | Cooldown applies |
| `/homeadmin <player>` | View/manage another player’s homes. | Admin |
| `/cache view` | View cache status (if provided). | Admin |
| `/cache clearall` | Clear all plugin caches. | Admin |
| `/cache player <name>` | Clear cache for a specific player. | Admin |
| `/plugins` | List loaded HomePlugin internal modules. | Admin (not Bukkit `/plugins`) |
| `/lang update` | Update base language files. | Admin |
| `/lang merge` | Merge new keys into langs. | Admin |
| `/lang set <code>` | Switch active language (e.g. `FRENCH`). | Admin |
| `/renamehome [name] [newname]`| Rename an existing home | |
| `/relocatehome [name]`| Change the location of an existing home | |

---

## 🔐 Permissions

(Exact nodes from `plugin.yml` plus observed patterns.)

| Permission | Purpose | Default |
|------------|---------|---------|
| `homeplugin.command.home` | Use `/home` (teleport / GUI) | true |
| `homeplugin.command.sethome` | Use `/sethome` | true |
| `homeplugin.command.delhome` | Use `/delhome` | true |
| `homeplugin.command.listhome` | Use `/listhome` | true |
| `homeplugin.command.spawn` | Use `/spawn` | true |
| `homeplugin.command.tpa` | Send / accept / deny TPA requests | true |
| `homeplugin.admin` | Admin set/del spawn, cache, view others’ homes | op |
| `homeplugin.lang.update` | Update language files | op |
| `homeplugin.limit.<n>` | Override max homes (dynamic scan 1..100) | (permission-based) |

Additional (implicit / suggested):
| Node | Purpose |
|------|---------|
| `homeplugin.bypass.limit` | If implemented: ignore base limit (else rely on limit.<n>) |
| `homeplugin.bypass.validation` | Ignore unfair location restrictions |
| `homeplugin.bypass.cooldown` | Ignore RTP / teleport cooldowns (if implemented) |

Dynamic limits: The code iterates `homeplugin.limit.1` through `homeplugin.limit.100`, applying the highest value the player possesses.

---

## 🧩 PlaceholderAPI Integration

Implemented in `HomePluginExpansion`:

| Placeholder | Description |
|-------------|-------------|
| `%homeplugin_homes%` | Comma-separated home names or fallback (no homes) |
| `%homeplugin_homes_numbers%` | Number of homes |
| `%homeplugin_has_homes%` | `true` if player has ≥ 1 home |
| `%homeplugin_home_location_<name>%` | Formatted location (uses language format) |
| `%homeplugin_home_exists_<name>%` | `true` / `false` |
| `%homeplugin_home_world_<name>%` | World name |
| `%homeplugin_home_coordinates_<name>%` | Raw coordinates `X Y Z` |

Note: `<name>` is case-insensitive.

---

## Vault Integration

Economy features are implemented in `EconomyManager` and use Vault for compatibility with supported economy plugins. See Vault documentation for setup and provider requirements.

---

## 🌍 Languages

Configured via: `Config.Language`  
Available (built-in):
- FRENCH
- ENGLISH
- SPANISH

Language loading uses an enum (`Language.valueOf(...)`) and falls back to FRENCH if an invalid value is provided. Customization is supported through shipped language YAML files; use the `/lang` commands to update/merge or switch languages.

---

## ⚙️ Configuration

Exact structure (from `src/main/resources/config.yml`):

```yaml
Config:
  Language: FRENCH
  Connector:
    TYPE: "YAML" # MYSQL or YAML
    # Optional (for MYSQL)
    HOST: "localhost"
    PORT: 3306
    USERNAME: "root"
    PASSWORD: ""
    DATABASE: "HomePlugin"
  Redis:
    UseRedis: false
    # Optional if enabled
    HOST: "localhost"
    PORT: 6379
    SSL: false
    PASSWORD: ""
  Home:
    DefaultHomeLimit: 3
    PreventUnfairLocation: true
    DisabledWorlds:
      - "world_nether"
      - "world_the_end"
  Task:
    Task-duration: 3 # second
    UseTitle: true
    UseMessage: false
    Add-particles-after-teleport: true
  Tpa:
    Tpa-duration: 30 # seconds
  Rtp:
    cooldown-seconds: 1000 # seconds
    max-radius: 200
  Economy:
    UseEconomy: false            # Enable or disable economy features (default: false)
    HomeCreationCost: 100.0      # Cost to create a new home (0 to disable)
    Home-Teleport-Price: 50.0    # Cost to teleport to a home (0 to disable)
    Tpa-Request-Price: 20.0      # Cost to send a /tpa request (0 to disable)
    Rtp-Price: 150.0             # Cost to use /rtp command (0 to disable)
```

Key behaviors:
- `Config.Home.DefaultHomeLimit` is used as the base home limit.
- TPA timeout uses `Config.Tpa.Tpa-duration` (defaults to 30 if missing).
- Redis only initializes if `UseRedis: true`; otherwise it's skipped.
- Validation uses `PreventUnfairLocation` and `DisabledWorlds`.
- Teleport warmup (`Task-duration`) controls delayed teleports; titles/messages/particles are optional.

---

## 🧱 Architecture & Performance

- Managers use static access patterns from the main plugin instance after initialization.
- Redis (JedisPooled) is initialized only when enabled; plugin gracefully falls back if unavailable.
- MySQL support is available when `Config.Connector.TYPE == MYSQL`.
- Dynamic plugin extension system: `PluginLoader` / `PluginManager` supports loading internal HomePlugin components.
- PlaceholderAPI integration is a soft dependency — the plugin registers expansions only if PlaceholderAPI is present.
- Home limit resolution scans permission nodes (`homeplugin.limit.<n>`) to determine the highest allowed value.
- Teleport warmups and expiration tasks (e.g., TPA) use the Bukkit scheduler.

---

## 🧑‍💻 Developer / Extension API

Extension points exist even without a unified external facade:

### Plugin Loader Interfaces
```java
public interface HomePlugin {
    String getName();
    String getVersion();
    String getAuthor();
    String[] getAuthors();
    void initialize();
    void stop();
    boolean isSqlStorageEnabled();
    boolean isPlaceholderApiHooked();
    boolean isCacheEnabled();
}

public interface PluginLoader {
    void loadPlugin(HomePlugin homePlugin);
    List<HomePlugin> getHomePlugin();
    void unregisterPlugin(HomePlugin homePlugin);
}
```

`PluginManager.getInstance()` maintains the internal list of loaded HomePlugin modules.

### Example (Registering an Internal Module)
```java
PluginManager pm = PluginManager.getInstance();
pm.loadPlugin(new MyExtensionPlugin()); // implements fr.fuzeblocks.homeplugin.plugin.HomePlugin
```

### Example (Accessing Home Limits)
```java
int max = HomePermissionManager.getMaxHomes(player);
boolean canSet = HomePermissionManager.canSetHome(player);
```

---

## ✅ Compatibility

- Declared `api-version: 1.14` (plugin.yml)
- Designed for modern Paper/Spigot derivatives (later versions generally maintain 1.14 API compatibility)
- Soft-dependency: PlaceholderAPI (plugin still loads without it, with a warning)
- Storage: YAML (default) or MySQL (when enabled)

---

## 🗺️ Roadmap

| Feature                            | Status     |
|-----------------------------------|------------|
| Economy cost per home             | Good       |
| Offline player home edit          | Planned    |
| Edit existing homes               | Good       |
| Add public homes                  | Planned    |

Suggestions welcomed via Issues or Discord.

---

## 🤝 Support & Contributions

- Issues: Use GitHub Issues for bugs / feature requests
- Discord: [Join](https://discord.gg/5zJyKz6Nfm)
- Pull Requests: Follow clear commit messages; discuss large changes beforehand

### Contribution Flow
1. Fork
2. Create feature branch
3. Commit changes
4. Open PR referencing any related Issue

---

## 📄 License

[Apache-2.0](https://github.com/fuzeblocks/HomePlugin?tab=Apache-2.0-1-ov-file)

---

> 🛠️ HomePlugin — Stable, fast, and extensible home/spawn/teleport management for modern servers.
