package fr.fuzeblocks.homeplugin.completers;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Warp completer.
 */
public class WarpCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            if (sender.hasPermission("homeplugin.command.warp.modify")) {
                return List.of("delete", "list", "modify", "set");
            } else {
                return new ArrayList<>(HomePlugin.getWarpManager().getAllWarps().keySet());
            }
         } else if (args.length == 2) {
            if (sender.hasPermission("homeplugin.command.warp.modify")) return new ArrayList<>(HomePlugin.getWarpManager().getAllWarps().keySet());

        }
        return List.of();
     }
}
