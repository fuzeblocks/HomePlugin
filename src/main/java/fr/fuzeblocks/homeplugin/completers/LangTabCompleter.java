package fr.fuzeblocks.homeplugin.completers;

import fr.fuzeblocks.homeplugin.language.Language;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Lang tab completer.
 */
public class LangTabCompleter implements TabCompleter {

    private static final List<String> SUBCOMMANDS = Arrays.asList("update", "set", "merge");
    private final Plugin plugin;

    /**
     * Instantiates a new Lang tab completer.
     *
     * @param plugin the plugin
     */
    public LangTabCompleter(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (args.length == 1) {
            String start = args[0].toLowerCase();
            return SUBCOMMANDS.stream()
                    .filter(sub -> sub.startsWith(start))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            if ("set".equalsIgnoreCase(args[0])) {
                String start = args[1].toUpperCase();
                List<String> langs = new ArrayList<>();
                for (Language lang : Language.values()) {
                    String name = lang.name();
                    if (name.startsWith(start)) {
                        langs.add(name);
                    }
                }
                return langs;
            } else if ("merge".equalsIgnoreCase(args[0])) {
                return List.of(plugin.getDataFolder().list());

            }
        }

        return List.of();
    }
}
