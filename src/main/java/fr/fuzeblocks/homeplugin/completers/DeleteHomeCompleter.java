package fr.fuzeblocks.homeplugin.completers;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.sync.type.SyncMethod;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DeleteHomeCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> home = new ArrayList<>();
            if (HomePlugin.getRegistrationType().equals(SyncMethod.MYSQL)) {
                home.addAll(HomePlugin.getHomeSQLManager().getHomesName((Player) sender));
            } else {
                home.addAll(HomePlugin.getHomeYMLManager().getHomesName((Player) sender));
            }
            return home;
        }
        return null;
    }
}
