<div align="center">


<img width="1021" height="55" alt="homeplugin_author" src="https://github.com/user-attachments/assets/31d00352-ad6c-42e0-8901-8e07f6088153" />

[![](https://jitpack.io/v/fuzeblocks/HomePlugin.svg)](https://jitpack.io/#fuzeblocks/HomePlugin)
[![sponsor](https://img.shields.io/badge/Sponsor-Support%20Development-blue)](https://client.pristis.fr/aff.php?aff=2)
[![Discord](https://img.shields.io/discord/1394947383560900618?color=5865F2&logo=discord&logoColor=white&label=Discord)](https://discord.gg/5zJyKz6Nfm)
[![License](https://img.shields.io/badge/license-Apache--2.0-orange.svg)](https://github.com/fuzeblocks/HomePlugin?tab=Apache-2.0-1-ov-file)


**A lightweight, flexible home & teleport management plugin for Paper/Spigot servers**

**Stable • Fast • Modular • API-Driven**

[Features](#-features) • [Installation](#%EF%B8%8F-installation) • [Commands & Permissions](#-commandes--permissions) • [Configuration](#%EF%B8%8F-configuration) • [API Documentation](https://fuzeblocks.github.io/Hom...)

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


### Commandes & Permissions

Les tableaux ci-dessous décrivent les commandes utilisateur et administrateur ainsi que les permissions nécessaires. Les permissions suivent la convention `homeplugin.<type>.<action>`.

Joueurs (Player Commands)

| Commande | Description | Permission | Par défaut |
|---|---:|---|---|
| `/sethome [name] [info]` | Créer ou mettre à jour une home nommée (par défaut: `home`) | `homeplugin.command.sethome` | true |
| `/home [name]` | Se téléporter vers une home. Sans nom, ouvre l'interface GUI | `homeplugin.command.home` | true |
| `/delhome <name>` | Supprimer une home | `homeplugin.command.delhome` | true |
| `/listhome` ou `/homes` | Lister toutes vos homes | `homeplugin.command.listhome` | true |
| `/renamehome <old> <new>` | Renommer une home | `homeplugin.command.renamehome` | true |
| `/relocatehome <name>` | Déplacer la home à votre position actuelle | `homeplugin.command.relocatehome` | true |
| `/back` | Revenir à votre position précédente | `homeplugin.command.back` | op |
| `/spawn` | Se téléporter au spawn global | `homeplugin.command.spawn` | true |
| `/tpa <player>` | Demande de téléportation vers un joueur | `homeplugin.command.tpa` | true |
| `/tpaccept [player]` | Accepter une demande TPA | `homeplugin.command.tpa.accept` | true |
| `/tpdeny [player]` | Refuser une demande TPA | `homeplugin.command.tpa.deny` | true |
| `/rtp` | Téléportation aléatoire (RTP) | `homeplugin.command.rtp` | true |

Administrateurs (Admin Commands)

| Commande | Description | Permission |
|---|---:|---|
| `/setspawn` | Définir le spawn global | `homeplugin.admin.spawn.set` |
| `/delspawn` | Supprimer le spawn global | `homeplugin.admin.spawn.delete` |
| `/homeadmin <player>` | Gérer les homes d'un autre joueur (création/suppression/édition) | `homeplugin.admin.home.manage` |
| `/cache view` | Voir les statistiques du cache | `homeplugin.admin.cache.view` |
| `/cache clearall` | Vider tous les caches du plugin | `homeplugin.admin.cache.clear` |
| `/cache player <name>` | Vider le cache pour un joueur spécifique | `homeplugin.admin.cache.player` |
| `/lang set <code>` | Définir la langue active pour le serveur (ex: `en`, `fr`) | `homeplugin.admin.lang.set` |
| `/lang update` | Mettre à jour/merger les fichiers de langue | `homeplugin.admin.lang.update` |
| `/modules` | Lister les modules HomePlugin chargés | `homeplugin.admin.modules` |

Remarques:
- Les commandes d'administration peuvent également être accessibles via des sous-permissions plus larges comme `homeplugin.admin.*`.
- Les préfixes utilisés pour les permissions sont configurables dans certaines installations via l'API (si nécessaire).

---

## 🔐 Permissions détaillées

Permissions de base et leur comportement par défaut (Default):

| Permission | Description | Default |
|---|---:|---|
| `homeplugin.command.home` | Utiliser la commande `/home` | true |
| `homeplugin.command.sethome` | Créer/modifier des homes | true |
| `homeplugin.command.delhome` | Supprimer des homes | true |
| `homeplugin.command.listhome` | Lister ses homes | true |
| `homeplugin.command.spawn` | Utiliser `/spawn` | true |
| `homeplugin.command.tpa` | Envoyer des TPAs | true |
| `homeplugin.command.tpa.accept` | Accepter les TPAs | true |
| `homeplugin.command.tpa.deny` | Refuser les TPAs | true |
| `homeplugin.command.rtp` | Utiliser la téléportation aléatoire | true |
| `homeplugin.command.back` | Utiliser `/back` | op |
| `homeplugin.admin.*` | Accès à toutes les commandes administratives | op |
| `homeplugin.admin.home.manage` | Gérer les homes des autres joueurs | op |
| `homeplugin.admin.cache.*` | Voir/vider le cache | op |
| `homeplugin.admin.lang.*` | Gérer les langues | op |

Limites dynamiques de homes

- Utilisez les permissions `homeplugin.limit.<N>` pour donner le droit à N homes.
  - Exemple: `homeplugin.limit.5` → 5 homes
  - Le plugin scanne les permissions de 1 à 100 et applique la plus haute valeur trouvée.

Permissions de contournement (Bypass)

| Permission | Description |
|---|---|
| `homeplugin.home.bypass` | Ignore les limites de nombre de homes et certaines restrictions de placement |
| `homeplugin.spawn.bypassworlds` | Permet de /spawn même si le monde est listé dans `Disabled-Worlds` |

---

<img width="270" height="50" alt="homeplugin_config" src="https://github.com/user-attachments/assets/8f51b1c3-2c5b-4c16-91de-328482b45a39" />


### 📄 Fichier de configuration (config.yml)

Le fichier de configuration se trouve par défaut dans `plugins/HomePlugin/config.yml` après le premier démarrage du serveur.

**[📖 Voir config.yml complet](https://github.com/fuzeblocks/HomePlugin/blob/main/src/main/resources/config.yml)**

Exemple rapide (extrait):

```yaml
# Configuration simplifiée — voir le fichier complet pour toutes les options
General:
  Language: "FRENCH" # Valeurs possibles: FRENCH, ENGLISH, SPANISH, RUSSIAN, UKRAINIAN, GERMAN, TURKISH
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
```

Clés importantes expliquées:
- Language: code de langue utilisé par le plugin (peut être en majuscules ou minuscules selon YAML). Vous pouvez aussi changer à la volée via `/lang set <code>`.
- Storage.TYPE: `YAML` pour la configuration par défaut, `MYSQL` pour une base de données (configurer la section MYSQL).
- Redis: activez le cache cross-instance avec `Use-Redis: true` et renseignez l'hôte/port/mot de passe.
- Default-Home-Limit: nombre de homes de base si aucune permission `homeplugin.limit.*` ne l'override.
- Task-Duration: durée de warmup avant toute téléportation (en secondes).
- RTP.Max-Radius: distance max depuis le spawn pour la téléportation aléatoire.

Conseils de configuration:
- Pour les grands serveurs, préférez `MYSQL` + `Use-Redis: true` pour des performances et synchronisation multi-instances.
- Réduisez `Task-Duration` pour gameplay plus fluide ou augmentez pour prévenir l'utilisation abusive.
- Testez les prix d'économie en sandbox avant de les appliquer en production.

---

<img width="260" height="50" alt="homeplugin_integration" src="https://github.com/user-attachments/assets/553fad7a-6d23-4e4c-881e-b9a7e4372177" />


### PlaceholderAPI

HomePlugin fournit des placeholders utilisables dans d'autres plugins:

| Placeholder | Description |
|---|---|
| `%homeplugin_homes%` | Liste comma-séparée des noms de homes |
| `%homeplugin_homes_numbers%` | Nombre total de homes |
| `%homeplugin_has_homes%` | `true` si le joueur a au moins une home |
| `%homeplugin_home_location_<name>%` | Emplacement formaté d'une home |
| `%homeplugin_home_exists_<name>%` | Indique si une home existe |
| `%homeplugin_home_world_<name>%` | Nom du monde de la home |
| `%homeplugin_home_coordinates_<name>%` | Coordonnées brutes (X Y Z) |
| `%homeplugin_home_teleport_price%` | Prix de téléportation |
| `%homeplugin_home_creation_price%` | Prix de création |
| `%homeplugin_tpa_request_price%` | Prix d'envoi TPA |
| `%homeplugin_rtp_price%` | Prix RTP |

### Vault Economy

Configurez les coûts optionnels via Vault. Compatible avec tout plugin d'économie supportant Vault.

---

<img width="250" height="50" alt="homeplugin_languages" src="https://github.com/user-attachments/assets/b2046b7e-35f1-4a8a-bfd4-376f3350096f" />

Langues intégrées et fichiers personnalisables (YAML):

- 🇫🇷 Français — code: `FRENCH` (ou `fr` pour `/lang set`)
- 🇬🇧 Anglais — code: `ENGLISH` (ou `en`)
- 🇪🇸 Espagnol — code: `SPANISH` (ou `es`) — traduction par Henri Topper
- 🇷🇺 Russe — code: `RUSSIAN` (ou `ru`)
- 🇺🇦 Ukrainien — code: `UKRAINIAN` (ou `uk`)
- 🇩🇪 Allemand — code: `GERMAN` (ou `de`)
- 🇹🇷 Turc — code: `TURKISH` (ou `tr`) — traduction par Xenetotyp3

Comment ajouter/mettre à jour une langue:
1. Copiez un fichier de langue existant depuis `plugins/HomePlugin/lang/` ou depuis `src/main/resources/lang/`.
2. Traduisez les clés YAML en respectant la structure.
3. Chargez/mergez le fichier via `/lang update` ou redémarrez le serveur.

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

If you find this plugin useful, consider [sponsoring](https://client.pristis.fr/aff.php?aff=2) to support development!

</div>
