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

public class TpAcceptCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (!(sender instanceof Player) || args.length != 1) return completions;

        Player target = (Player) sender;
        CacheManager cacheManager = CacheManager.getInstance();

        for (UUID senderUUID : cacheManager.getAllTpaSenders()) {
            UUID tpaTargetUUID = cacheManager.getTpaTarget(senderUUID);
            if (tpaTargetUUID != null && tpaTargetUUID.equals(target.getUniqueId())) {
                Player requester = Bukkit.getPlayer(senderUUID);
                if (requester != null && requester.isOnline()) {
                    completions.add(requester.getName());
                }
            }
        }

        return completions;
    }
}
