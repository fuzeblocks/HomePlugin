package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.spawn.yml.SpawnManager;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.sync.type.SyncMethod;
import fr.fuzeblocks.homeplugin.task.TaskManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static fr.fuzeblocks.homeplugin.task.TaskSaveUtils.setTaskManagerInstance;

public class SpawnCommand implements CommandExecutor {
    private final String key = "Config.Language.";


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length == 0) {
                SpawnManager spawnManager = HomePlugin.getSpawnManager();
                fr.fuzeblocks.homeplugin.spawn.sql.SQLSpawnManager spawnSQLManager = HomePlugin.getSpawnSQLManager();
                if (HomePlugin.getRegistrationType().equals(SyncMethod.MYSQL)) {
                    if (spawnSQLManager.hasSpawn(player.getWorld())) {
                        if (spawnSQLManager.isStatus(player)) {
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString("Config.Language.A-teleport-is-already-in-progress")));
                            return false;
                        }
                        addTask(player);
                    } else {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "No-spawn-defined")));
                    }
                } else {
                    if (spawnManager.hasSpawn(player.getWorld())) {
                        if (spawnManager.isStatus(player)) {
                            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString("Config.Language.A-teleport-is-already-in-progress")));
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
        if (!player.hasPermission(HomePlugin.getConfigurationSection().getString("Config.Spawn.Spawn-teleportation-permission"))) {
            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString("Config.Spawn.SetSpawn-permission-deny-message")));
            return;
        }
        TaskManager taskManager = new TaskManager(HomePlugin.getPlugin(HomePlugin.class));
        taskManager.spawnTask(player);
        taskManager.startTeleportTask();
        setTaskManagerInstance(player, taskManager);
        StatusManager.setPlayerStatus(player, true);
        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key + "Start-of-teleportation-for-spawn")));
    }
}
