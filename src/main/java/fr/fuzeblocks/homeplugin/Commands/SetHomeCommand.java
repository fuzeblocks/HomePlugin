package fr.fuzeblocks.homeplugin.Commands;

import fr.fuzeblocks.homeplugin.Home.HomeManager;
import fr.fuzeblocks.homeplugin.HomePlugin;
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
                String home_name = args[0];
                HomeManager homeManager = HomePlugin.homeManager;
                if (!sethomecheck(player, homeManager)) {
                    homeManager.addHome(player,home_name);
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
