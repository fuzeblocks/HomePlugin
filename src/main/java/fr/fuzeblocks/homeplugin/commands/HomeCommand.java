package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.status.Status;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.task.CancelTask;
import fr.fuzeblocks.homeplugin.task.TaskManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static fr.fuzeblocks.homeplugin.task.TaskSaveUtils.getTaskManagerInstance;
import static fr.fuzeblocks.homeplugin.task.TaskSaveUtils.setTaskManagerInstance;

public class HomeCommand implements CommandExecutor, CancelTask {
    private final HomePlugin instance;
    private static TaskManager taskManager;

    public HomeCommand(HomePlugin instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                String homeName = args[0];
                HomeManager homeManager = HomePlugin.homeManager;
                if (homeManager.isStatus(player)) {
                    return false;
                }
                if (homeManager.getHomeNumber(player) > 0) {
                    Location homeLocation = homeManager.getHomeLocation(player, homeName);
                    if (homeLocation != null) {
                        player.sendMessage("§6Début de la téléportation pour le home : " + homeName);
                        StatusManager.setPlayerStatus(player, Status.TRUE);
                        taskManager = new TaskManager(instance);
                        taskManager.homeTask(homeName,player);
                        taskManager.startTeleportTask();
                        setTaskManagerInstance(player,taskManager);
                        return true;
                    } else {
                        player.sendMessage("§cLe home spécifié n'existe pas.");
                    }
                } else {
                    player.sendMessage("§cVous n'avez aucun home enregistré.");
                }
            } else {
                player.sendMessage("§cUtilisation de la commande : /home <nom-du-home>");
            }
        }  else {
            sender.sendMessage("§cSeul un joueur peut executer cette commande !");
        }
        return false;
    }
}
