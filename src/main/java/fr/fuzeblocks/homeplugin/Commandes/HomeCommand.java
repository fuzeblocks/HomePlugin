package fr.fuzeblocks.homeplugin.Commandes;


import fr.fuzeblocks.homeplugin.Home.HomeManager;
import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            String home_name = args[0];
            if (args.length == 1) {
                if (args[0] != null) {
                    HomeManager home = new HomeManager(HomePlugin.home);
                    if (home.getHomeNumber(player) > 0) {
                       Location location = home.getHomeLocation(player,home_name);
                        if (location != null) {
                            player.teleport(location);
                        }
                    }
                }
            }
        }
        return false;
    }
}
