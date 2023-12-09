package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (player.isOp()) {
                Location location = player.getLocation();
                if (HomePlugin.spawnManager.hasSpawn()) {
                    player.sendMessage("§cLe spawn existe deja !");
                    return false;
                }
                HomePlugin.spawnManager.setSpawn(location);
                player.sendMessage("§aLe spawn a été defini en X : " + location.getX() + " Y : " + location.getY() + " Z : " + location.getZ());
                return true;
            } else {
                player.sendMessage("§cVous n'étes pas op !");
            }
         } else {
            sender.sendMessage("§cSeul un joueur peut executer cette commande !");
        }
        return false;
    }
}
