package fr.fuzeblocks.homeplugin.commands.spawn;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.event.OnSpawnCreatedEvent;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import fr.fuzeblocks.homeplugin.spawn.SpawnManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command to set the spawn point of a world.
 */
public class SetSpawnCommand implements CommandExecutor {

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

        if (!player.hasPermission("homeplugin.admin")) {
            player.sendMessage(languageManager.getStringWithColor(SPAWN + "SetSpawn-permission-deny-message"));
            return false;
        }

        Location location = player.getLocation();

        if (location.getWorld() == null) {
            player.sendMessage(languageManager.getStringWithColor(SPAWN + "Invalid-world"));
            return false;
        }

        SpawnManager spawnManager = HomePlugin.getSpawnManager();

        if (spawnManager.hasSpawn(location.getWorld())) {
            player.sendMessage(languageManager.getStringWithColor(SPAWN + "Spawn-already-exists"));
            return false;
        }

        OnSpawnCreatedEvent event = new OnSpawnCreatedEvent(player, location);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }

        boolean success = spawnManager.setSpawn(event.getLocation());

        if (success) {
            Location spawn = event.getLocation();
            player.sendMessage(languageManager.getStringWithColor(SPAWN + "Spawn-has-been-set")
                    .replace("%x%", String.format("%.1f", spawn.getX()))
                    .replace("%y%", String.format("%.1f", spawn.getY()))
                    .replace("%z%", String.format("%.1f", spawn.getZ()))
                    .replace("%world%", spawn.getWorld().getName()));
        } else {
            player.sendMessage(languageManager.getStringWithColor(LANG + "Error"));
        }

        return success;
    }
}