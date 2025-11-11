package fr.fuzeblocks.homeplugin.completers;

import fr.fuzeblocks.homeplugin.cache.CacheManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The type Tp deny completer.
 */
public class TpDenyCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (!(sender instanceof Player) || args.length != 1)
            return completions;

        Player player = (Player) sender;
        CacheManager cacheManager = CacheManager.getInstance();


        for (UUID senderUUID : cacheManager.getAllTpaSenders()) {
            UUID targetUUID = cacheManager.getTpaTarget(senderUUID);
            if (targetUUID != null && targetUUID.equals(player.getUniqueId())) {
                Player requester = Bukkit.getPlayer(senderUUID);
                if (requester != null && requester.isOnline()) {
                    completions.add(requester.getName());
                }
            }
        }

        return completions;
    }
}
