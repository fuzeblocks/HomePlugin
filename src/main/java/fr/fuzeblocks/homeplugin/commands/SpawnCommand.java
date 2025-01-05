package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.spawn.SpawnManager;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.task.TaskManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static fr.fuzeblocks.homeplugin.task.TaskSaveUtils.setTaskManagerInstance;

public class SpawnCommand implements CommandExecutor {
    private final String key = "Language.";
    private final String spawnKey = "Spawn.";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Only-a-player-can-execute")));
        }
        Player player = ((Player) sender).getPlayer();
        if (args.length != 0) {
            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString( spawnKey + "Spawn-usage-message")));
        }
            SpawnManager spawnManager = HomePlugin.getSpawnManager();
        if (!spawnManager.hasSpawn(player.getWorld())) {
            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "No-spawn-defined")));
        }
        if (!spawnManager.isStatus(player)) {
            addTask(player);
        }
            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "A-teleport-is-already-in-progress")));
            return true;
    }

    private void addTask(Player player) {
        if (!player.hasPermission(HomePlugin.getLanguageManager().getString(spawnKey + "Spawn-teleportation-permission"))) {
            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(spawnKey + "SetSpawn-permission-deny-message")));
            return;
        }
        TaskManager taskManager = new TaskManager(HomePlugin.getPlugin(HomePlugin.class));
        taskManager.spawnTask(player);
        taskManager.startTeleportTask();
        setTaskManagerInstance(player, taskManager);
        StatusManager.setPlayerStatus(player, true);
        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Start-of-teleportation-for-spawn")));
    }
}