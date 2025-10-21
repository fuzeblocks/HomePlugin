package fr.fuzeblocks.homeplugin.completers;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Home admin completer.
 */
public class HomeAdminCompleter implements TabCompleter {

    private static final List<String> SUBCOMMANDS = List.of("list", "addhome", "deletehome", "tphome");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            return SUBCOMMANDS.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 3 && "tphome".equalsIgnoreCase(args[0]) || "deletehome".equalsIgnoreCase(args[0])) {
            Player player = Bukkit.getPlayer(args[1]);
            if (player != null) {
                return new ArrayList<>(HomePlugin.getHomeManager().getHomesName(player));
            }
        }
        return Collections.emptyList();
    }
}