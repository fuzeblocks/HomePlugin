[![Dependency review](https://github.com/fuzeblocks/HomePlugin/actions/workflows/dependency-review.yml/badge.svg)](https://github.com/fuzeblocks/HomePlugin/actions/workflows/dependency-review.yml) [![CodeQL](https://github.com/fuzeblocks/HomePlugin/actions/workflows/codeql.yml/badge.svg)](https://github.com/fuzeblocks/HomePlugin/actions/workflows/codeql.yml)



🏠 Welcome to FuzeBlocks HomePlugin! 🏠

Enhance your players' gaming experience with ease using our Spigot plugin for home management created by fuzeblocks.

Key Features:

🔹Multiple synchronization methods (MYSQL and YML).

🔹An API for developers

🔹 PlaceHolderAPI

🔹 Easy Home Setting: Use the /sethome command to define a home point at the player's current location.

🔹 Intuitive Management: Access a list of all defined homes with the /homes command, allowing players to view, rename, and delete their home points.

🔹 Configurable Limits: Customize settings in the configuration file to adjust the number of homes per player.

🔹 Powerful Commands: Enjoy a rich and personalized gaming experience with commands like /home, /spawn, /setspawn, /delspawn, /delhome.

🔹 Player Cache: Easily clear the cache with the /cache command, either for all players or for a specific player. 🔹 Home Administration: Manage player homes effortlessly with the /homeadmin command.

Installation: 🛠️ Download the JAR file from the version page and place it in the "plugins" folder of your Spigot server. Restart the server for changes to take effect. Customize the experience in the configuration file (config.yml).

Commands: ⚙️ /sethome [name] - Set a home point at the player's current location.

⚙️ /home [name] - Teleport to a home point.

⚙️ /spawn - Teleport to the spawn.

⚙️ /setspawn - Set the spawn point.

⚙️/delspawn - Delete the spawn point.

⚙️ /delhome [name] - Delete a home point.

⚙️ /cache [player,clearall,view player] - Clear the cache for players or specific ones.

⚙️ /homeadmin [player] - View a player's homes (admin command).

# PlaceHolderAPI Tags, for FuzeBlocks HomePlugin 🏡

## %homeplugin_homes% 🏠

**Description:**  
This tag is used to get homes for a player.

**Parameters:**  
No parameters are required.

**Usage:**  
`%homeplugin_homes%` returns the phrase "Get Homes for a player", indicating that homes are being retrieved for a specific player.

---

## %homeplugin_homes_numbers% 📊

**Description:**  
This tag is used to get the total number of homes for a player.

**Parameters:**  
No parameters are required.

**Usage:**  
`%homeplugin_homes_numbers%` returns an integer representing the total number of homes for a player.

---

## %homeplugin_home_location_<home_name>% 📍

**Description:**  
This tag is used to get the location of a specific home for a player.

**Parameters:**  
- `<home_name>`: Replace this with the actual name of the home whose location you want to retrieve.

---

## API Installation Instructions ⚙️

For developers:  
1. Download the JAR file from the version page.
2. Place it in the "plugins" folder of your Spigot server.
3. Restart the server for changes to take effect.
4. Customize the experience in the configuration file (`config.yml`).

**Note:** For assistance, bug reports, or contributions, visit our [GitHub repository](#). Your feedback is crucial in continuously improving FuzeBlocks HomePlugin! 💬

**Tested Version:** 1.20.x  
**HomePage:** [Link to HomePage](#)

---

**Disclaimer:**  
NOT AN OFFICIAL MINECRAFT SERVICE. NOT APPROVED BY OR ASSOCIATED WITH MOJANG OR ANY OTHER ENTITY.
