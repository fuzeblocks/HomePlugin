package fr.fuzeblocks.homeplugin.Commands;

import fr.fuzeblocks.homeplugin.Home.HomeManager;
import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.Status.Status;
import fr.fuzeblocks.homeplugin.Status.StatusManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender).getPlayer();
            if (args.length == 1) {
                if (StatusManager.getPlayerStatus(player) != null && StatusManager.getPlayerStatus(player).equals(Status.TRUE)) {
                    player.sendMessage("§cUne téléportation est déja en cours !");
                    return false;
                }
                String home_name = args[0];
                HomeManager homeManager = HomePlugin.homeManager;
                if (sethomecheck(player, homeManager) == false) {
                   if (homeManager.addHome(player,home_name)) {
                       player.sendMessage("§aHome ajouté !");
                   }
                    return true;
                } else {
                    player.sendMessage("§cVous avez atteint la limite d'homes disponible !");
                    return false;
                }
            }
        }
        return false;
    }

    public boolean sethomecheck(Player player, HomeManager homeManager) {
        User user = HomePlugin.api.getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            for (Node node : user.getNodes()) {
                if (node.getKey().equals("group")) {
                    String groupName = String.valueOf(node.getValue());
                    if (groupName.equalsIgnoreCase("VIP") && homeManager.getHomeNumber(player) >= 6) {
                        return true;
                    }
                }
            }
        }

        return homeManager.getHomeNumber(player) >= 3;
    }
}
