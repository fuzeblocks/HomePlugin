package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.spawn.SpawnManager;
import fr.fuzeblocks.homeplugin.status.Status;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.task.CancelTask;
import fr.fuzeblocks.homeplugin.task.TaskManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static fr.fuzeblocks.homeplugin.task.TaskSaveUtils.setTaskManagerInstance;

public class SpawnCommand implements CommandExecutor, CancelTask {
    private HomePlugin instance;
    private static TaskManager taskManager;


    public SpawnCommand(HomePlugin instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length == 0) {
                SpawnManager spawnManager = HomePlugin.spawnManager;
                if (spawnManager.hasSpawn()) {
                    if (spawnManager.isStatus(player)) {
                        return false;
                    }
                    taskManager = new TaskManager(instance);
                    taskManager.spawnTask(player);
                    taskManager.startTeleportTask();
                    setTaskManagerInstance(player,taskManager);
                    StatusManager.setPlayerStatus(player, Status.TRUE);
                    player.sendMessage("§6Début de la téléportation pour le spawn");
                } else {
                    player.sendMessage("§cAucun spawn n'est défini !");
                }
            } else {
                player.sendMessage("§cUtilisation de la commande : /spawn");
            }
        } else {
            sender.sendMessage("§cSeul un joueur peut executer cette commande !");
        }
        return false;
    }

}
