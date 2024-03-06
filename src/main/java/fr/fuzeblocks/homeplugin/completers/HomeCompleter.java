package fr.fuzeblocks.homeplugin.completers;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomeCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> home = new ArrayList<>();
        if (args.length == 1) {
            if (HomePlugin.getRegistrationType() == 1) {
                home.addAll(HomePlugin.getHomeSQLManager().getHomesName((Player) sender));
            } else {
                home.addAll(HomePlugin.getHomeManager().getHomesName((Player) sender));
            }
            return home;
        }
        return null;
    }
}
