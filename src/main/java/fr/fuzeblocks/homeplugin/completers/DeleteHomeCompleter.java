package fr.fuzeblocks.homeplugin.completers;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Delete home completer.
 */
public class DeleteHomeCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(HomePlugin.getHomeManager().getHomesName((Player) sender));
        }
        return null;
    }
}
