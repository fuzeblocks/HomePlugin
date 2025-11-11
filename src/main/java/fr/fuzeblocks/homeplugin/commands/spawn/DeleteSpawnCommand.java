package fr.fuzeblocks.homeplugin.commands.spawn;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.event.OnSpawnDeletedEvent;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command to delete the spawn point of a world.
 */
public class DeleteSpawnCommand implements CommandExecutor {

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
            player.sendMessage(languageManager.getStringWithColor(LANG + "Player-is-not-OP"));
            return false;
        }

        World world = player.getWorld();

        Location currentSpawn = HomePlugin.getSpawnManager().getSpawn(world);

        if (currentSpawn == null) {
            player.sendMessage(languageManager.getStringWithColor(SPAWN + "No-spawn-set"));
            return false;
        }

        OnSpawnDeletedEvent event = new OnSpawnDeletedEvent(player, currentSpawn);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }

        if (HomePlugin.getSpawnManager().removeSpawn(event.getLocation().getWorld())) {
            player.sendMessage(languageManager.getStringWithColor(SPAWN + "Spawn-deleted"));
            return true;
        }


        return false;
    }
}