package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.spawn.yml.SpawnManager;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.task.TaskManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static fr.fuzeblocks.homeplugin.task.TaskSaveUtils.setTaskManagerInstance;

public class SpawnCommand implements CommandExecutor {
    private HomePlugin instance;
    private static TaskManager taskManager;
    private String key = "Config.Language.";

    public SpawnCommand(HomePlugin instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length == 0) {
                SpawnManager spawnManager = HomePlugin.getSpawnManager();
                fr.fuzeblocks.homeplugin.spawn.sql.SpawnManager spawnSQLManager = HomePlugin.getSpawnSQLManager();
                if (HomePlugin.getRegistrationType() == 1) {
                    if (spawnSQLManager.hasSpawn(player.getWorld())) {
                        if (spawnSQLManager.isStatus(player)) {
                            return false;
                        }
                        addTask(player);
                    } else {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "No-spawn-defined")));
                    }
                } else {
                    if (spawnManager.hasSpawn(player.getWorld())) {
                        if (spawnManager.isStatus(player)) {
                            return false;
                        }
                        addTask(player);
                    } else {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "No-spawn-defined")));
                    }
                }

            } else {
                player.sendMessage("§cUtilisation de la commande : /spawn");
            }

        } else {
            sender.sendMessage("§cSeul un joueur peut executer cette commande !");
        }
        return false;

    }
    private void addTask(Player player) {
        taskManager = new TaskManager(instance);
        taskManager.spawnTask(player);
        taskManager.startTeleportTask();
        setTaskManagerInstance(player,taskManager);
        StatusManager.setPlayerStatus(player, true);
        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Start-of-teleportation-for-spawn")));
    }
}
