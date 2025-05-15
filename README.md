# 🏠 Welcome to FuzeBlocks HomePlugin! 🏠  
**A powerful and flexible home management system for Spigot servers, developed by FuzeBlocks.**

Simplify and enhance your players' experience with intuitive home commands, developer-friendly APIs, and seamless performance features.

---

## ✨ Key Features

- 🔹 **Multiple Storage Options**  
  Support for both **MySQL** and **YAML**, offering flexibility depending on your server needs.

- 🔹 **Redis Caching (Optional)**  
  Boost performance with Redis, or stick to the default caching method.

- 🔹 **Developer API**  
  Easily extend functionality with our **public API**. Full documentation available.

- 🔹 **Spawn Management**  
  Set, delete, and teleport to spawn points using simple commands.

- 🔹 **Plugin Loader for Devs**  
  Includes a modular plugin loader to streamline development and integration.

- 🔹 **PlaceholderAPI Support**  
  Display home data in chat, GUIs, and more with custom placeholders.

- 🔹 **Simple Home Management**  
  - `/sethome [name]` — Define homes with ease.  
  - `/homes` — View, rename, or delete saved homes.

- 🔹 **Custom Home Limits**  
  Set limits via the configuration file.

- 🔹 **Comprehensive Commands**  
  Includes:  
  `/home`, `/spawn`, `/setspawn`, `/delspawn`, `/delhome`, `/listhome`, `/cache`, `/homeadmin`.

- 🔹 **Admin Tools**  
  Use `/homeadmin [player]` to view and manage homes for any player.

---

## ⚙️ Commands Overview

| Command | Description |
|--------|-------------|
| `/sethome [name]` | Set a home at your current location. |
| `/home [name]` | Teleport to a saved home. |
| `/homes` | List, rename, or delete your homes. |
| `/delhome [name]` | Remove a specific home. |
| `/spawn` | Teleport to the spawn point. |
| `/setspawn` | Define the world’s spawn location. |
| `/delspawn` | Remove the current spawn. |
| `/cache [player,clearall,view]` | Manage player cache. |
| `/homeadmin [player]` | Admin command to view a player's homes. |
| `/listhome` | View all your homes. |

---

## 🔧 Installation

1. Download the JAR from the [plugin page](https://modrinth.com/plugin/homeplugin/versions).  
2. Place it in your server's `plugins` folder.  
3. Restart your server.  
4. Customize the configuration via `config.yml`.

---

## 📦 PlaceholderAPI Integration

Use these placeholders with PlaceholderAPI:

| Placeholder | Description |
|------------|-------------|
| `%homeplugin_homes%` | Retrieves the list of a player's homes. |
| `%homeplugin_homes_numbers%` | Returns the total number of homes a player has. |
| `%homeplugin_home_location_<name>%` | Returns the location of the home with the specified name. |

> 🔁 Replace `<name>` with the actual home name.

---

## 🧑‍💻 Developer Resources

- **API Documentation**: [GitHub Wiki - API](https://github.com/fuzeblocks/HomePlugin/wiki)
- **Plugin Loader Guide**: [GitHub Wiki - Plugin API](https://github.com/fuzeblocks/HomePlugin/wiki/Plugin-API)

---

## 🧪 Compatibility

- ✅ Minecraft version **1.20.x** and earlier versions  
- 📦 Requires: [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
- API version : [![](https://jitpack.io/v/fuzeblocks/HomePlugin.svg)](https://jitpack.io/#fuzeblocks/HomePlugin)

---

## 🤝 Support & Contributions

For support, bug reports, feature suggestions, or to contribute, visit the official GitHub:  
🔗 [HomePlugin GitHub Repository](https://github.com/fuzeblocks/HomePlugin)

Your feedback helps us keep FuzeBlocks HomePlugin powerful, stable, and enjoyable!

## 🚧 Update Coming Soon!

We're currently working on something new and exciting.  
Stay tuned — fresh updates are on the way! 🔥

📅 Estimated Release: _Coming Soon_

Thanks for your patience 🙏  
— The Team
