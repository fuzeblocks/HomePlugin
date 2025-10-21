package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.spawn.SpawnManager;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.task.TaskManager;
import fr.fuzeblocks.homeplugin.task.TeleportationManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static fr.fuzeblocks.homeplugin.task.TaskSaveUtils.setTaskManagerInstance;

public class SpawnCommand implements CommandExecutor {
    private final String LANG = "Language.";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            String SPAWN = "Spawn.";
            if (args.length == 0) {
                SpawnManager spawnManager = HomePlugin.getSpawnManager();
                    if (spawnManager.hasSpawn(player.getWorld())) {
                        if (spawnManager.isStatus(player)) {
                            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(LANG + "A-teleport-is-already-in-progress"));
                            return false;
                        }
                        if (!player.hasPermission("homeplugin.command.spawn")) {
                            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(SPAWN + "SetSpawn-permission-deny-message"));
                            return false;
                        }
                        TeleportationManager.teleportPlayerToSpawn(player);
                    } else {
                        player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(SPAWN + "No-spawn-defined"));
                    }
            } else {
                player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor( SPAWN + "Spawn-usage-message"));
            }

        } else {
            sender.sendMessage((HomePlugin.getLanguageManager().getString(LANG + "Only-a-player-can-execute")));
        }
        return false;

    }
}