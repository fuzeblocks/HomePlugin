# 🏡 fuzeblocks HomePlugin [![wakatime](https://wakatime.com/badge/user/d9b73520-93d2-4dc7-91b6-1ff3d66bd0ec/project/018c3577-83a6-4b99-9ba5-4a8b4709921e.svg)](https://wakatime.com/badge/user/d9b73520-93d2-4dc7-91b6-1ff3d66bd0ec/project/018c3577-83a6-4b99-9ba5-4a8b4709921e)

> 🧩 *A simple plugin to manage homes and spawn with cache and a multitude of synchronization options.*

<div align="center">
  <img src="https://img.icons8.com/fluency/96/home.png" width="80" />
</div>

---

## 📚 Table of Contents

- [✨ Features](#-features)
- [🧾 Commands](#-commands)
- [🧩 PlaceholderAPI Integration](#-placeholderapi-integration)
- [📥 Installation](#-installation)
- [🧑‍💻 Developer Resources](#-developer-resources)
- [✅ Compatibility](#-compatibility)
- [🤝 Support & Contributions](#-support--contributions)

---

## ✨ Features

- 🔧 **Flexible Storage** – MySQL & YAML support  
- ⚡ **Optional Redis Caching** – Boost performance  
- 📦 **Public API** – Fully documented and easy to use  
- 🏠 **Simple Home System** – Set, teleport, delete, and list homes  
- 🧭 **Spawn Management** – Set/delete world spawn locations  
- 🧩 **Plugin Loader** – Modular integration for developers  
- 🎛️ **Custom Home Limits** – Configure max homes per player  
- 💬 **PlaceholderAPI Support** – Custom placeholders in GUIs/chat  
- 🛠️ **Admin Tools** – Manage other players’ homes easily

---

## 🧾 Commands

| ⚙️ Command | 📝 Description |
|------------|----------------|
| `/sethome [name, info]` | Set a home at your current location. |
| `/home [name]` | Teleport to a specific home. |
| `/home` | Open the GUI listing all your homes. |
| `/delhome [name]` | Delete a home by name. |
| `/listhome` | View a list of your homes. |
| `/spawn` | Teleport to the global spawn. |
| `/setspawn` | Define the spawn location. |
| `/delspawn` | Remove the current spawn. |
| `/cache [player, clearall, view]` | Manage internal cache. |
| `/homeadmin [player]` | Admin view of another player's homes. |

---

## 🧩 PlaceholderAPI Integration

Use these placeholders with [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/):

| 🔤 Placeholder | 📋 Description |
|---------------|----------------|
| `%homeplugin_homes%` | Comma-separated list of home names. |
| `%homeplugin_homes_numbers%` | Total number of homes. |
| `%homeplugin_has_homes%` | `true` if player has at least one home. |
| `%homeplugin_home_location_<name>%` | Location of the home. |
| `%homeplugin_home_exists_<name>%` | `true` if the home exists. |
| `%homeplugin_home_world_<name>%` | World name of the home. |
| `%homeplugin_home_coordinates_<name>%` | Coordinates: `X Y Z`. |

> Replace `<name>` with the home name (case-insensitive).

---

## 📥 Installation

1. ⬇️ Download from [Modrinth](https://modrinth.com/plugin/homeplugin/versions)  
2. 📁 Place the `.jar` in the `plugins/` folder  
3. 🔁 Restart your server  
4. ⚙️ Edit the config in `config.yml` as needed

---

## 🧑‍💻 Developer Resources

- 📘 API Docs: [GitHub Wiki - API](https://github.com/fuzeblocks/HomePlugin/wiki)  
- 🔌 Plugin Loader Guide: [GitHub Wiki - Plugin API](https://github.com/fuzeblocks/HomePlugin/wiki/Plugin-API)

---

## ✅ Compatibility

- 🧩 Minecraft **1.21.6** to **1.14.x**
- Requires: [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)  
- API versions: [GitHub Releases](https://github.com/fuzeblocks/HomePlugin/releases)

---

## 🤝 Support & Contributions

Found a bug? Have a suggestion? Want to contribute?  
Join the GitHub repository:  
[🔗 HomePlugin Repository](https://github.com/fuzeblocks/HomePlugin)

---

> 🛠️ **FuzeBlocks HomePlugin** — Stable, fast, and extensible home/spawn management.
