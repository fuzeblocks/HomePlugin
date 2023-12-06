package fr.fuzeblocks.homeplugin.Commands;

import fr.fuzeblocks.homeplugin.Home.HomeManager;
import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HomeCommand implements CommandExecutor {
    private final HomePlugin instance;

    public HomeCommand(HomePlugin instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                String home_name = args[0];
                HomeManager home = instance.homeManager;
                if (home.getHomeNumber(player) > 0) {
                    Location location = home.getHomeLocation(player, home_name);
                    if (location != null) {
                        player.sendMessage("§6Début de la téléportation pour le home : " + home_name);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.teleport(location);
                            }
                        }.runTaskLater(instance, 3 * 20);
                        return true;
                    } else {
                        player.sendMessage("§cLe home spécifié n'existe pas.");
                    }
                } else {
                    player.sendMessage("§cVous n'avez aucun home enregistré.");
                }
            } else {
                player.sendMessage("§cUtilisation: /home <nom-du-home>");
            }
        }
        return false;
    }
}
