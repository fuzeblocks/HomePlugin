package fr.fuzeblocks.homeplugin.completer;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
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
            HomeManager homeManager = HomePlugin.homeManager;
            Player player = (Player) sender;
            home.addAll(homeManager.getHomesNames(player));
            return home;
        }
        return null;
    }
}
