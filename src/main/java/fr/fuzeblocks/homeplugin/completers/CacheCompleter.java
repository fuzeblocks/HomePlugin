package fr.fuzeblocks.homeplugin.completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CacheCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completer = new ArrayList<>();
        if (args.length == 1) {
            completer.add("clearall");
            completer.add("view");
            completer.add("clear");
            return completer;
        }

        return null;
    }
}
