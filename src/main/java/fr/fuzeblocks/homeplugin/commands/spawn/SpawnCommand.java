package fr.fuzeblocks.homeplugin.commands.spawn;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import fr.fuzeblocks.homeplugin.spawn.SpawnManager;
import fr.fuzeblocks.homeplugin.task.TeleportationManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command to teleport a player to the spawn point of their current world.
 */
public class SpawnCommand implements CommandExecutor {

    private static final String SPAWN = "Spawn.";
    private static final String LANG = "Language.";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final LanguageManager languageManager = HomePlugin.getLanguageManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Only-a-player-can-execute"));
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("homeplugin.command.spawn")) {
            player.sendMessage(languageManager.getStringWithColor(LANG + "No-permission"));
            return false;
        }

        if (args.length != 0) {
            player.sendMessage(languageManager.getStringWithColor(SPAWN + "Spawn-usage-message"));
            return false;
        }

        return teleportToSpawn(player, languageManager);
    }

    /**
     * Teleports the player to the spawn point.
     *
     * @param player The player to teleport
     * @param languageManager The language manager instance
     * @return true if successful, false otherwise
     */
    private boolean teleportToSpawn(Player player, LanguageManager languageManager) {
        SpawnManager spawnManager = HomePlugin.getSpawnManager();

        if (player.getWorld() == null) {
            player.sendMessage(languageManager.getStringWithColor(SPAWN + "Invalid-world"));
            return false;
        }

        if (!spawnManager.hasSpawn(player.getWorld())) {
            player.sendMessage(languageManager.getStringWithColor(SPAWN + "No-spawn-defined"));
            return false;
        }

        if (spawnManager.isStatus(player)) {
            player.sendMessage(languageManager.getStringWithColor(LANG + "A-teleport-is-already-in-progress"));
            return false;
        }

        TeleportationManager.teleportPlayerToSpawn(player);
        return true;
    }
}