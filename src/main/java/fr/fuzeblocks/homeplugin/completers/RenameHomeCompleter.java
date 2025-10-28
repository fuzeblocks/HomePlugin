package fr.fuzeblocks.homeplugin.completers;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The type Rename home completer.
 */
public class RenameHomeCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return HomePlugin.getHomeManager().getHomesName((Player) sender);
        }
        return null;
    }
}
