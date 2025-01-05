package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.api.event.OnHomeCreatedEvent;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {
    private final String homeKey = "Home.";
    private final  String key = "Language.";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Only-a-player-can-execute")));
        }
            Player player = ((Player) sender).getPlayer();
        if (args.length != 1) {
            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(homeKey + "SetHome-usage-message")));
        }
            String home_name = args[0];
            HomeManager homeManager = HomePlugin.getHomeManager();
                if (homeManager.isStatus(player)) player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "A-teleport-is-already-in-progress")));
                if (havePermsHomes(player,homeManager)) return false;
            OnHomeCreatedEvent onHomeCreate = new OnHomeCreatedEvent(player, player.getLocation(), HomePlugin.getRegistrationType(), home_name);
                Bukkit.getPluginManager().callEvent(onHomeCreate);
                if (!onHomeCreate.isCancelled()) {
                    if (homeManager.addHome(player, onHomeCreate.getHomeName())) {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Home-added")));
                    } else {
                        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Error")));
                    }
                }
                return true;
        }


    private boolean havePermsHomes(Player player,HomeManager homeManager) {
        int count = HomePlugin.getLanguageManager().getInt(homeKey + "Home-limite-for-player");
        if (player.hasPermission(HomePlugin.getLanguageManager().getString( homeKey + "Home-limite-permission-for-bypass"))) return false;
        boolean returnBoolean = homeManager.getHomeNumber(player) == count;
        if (returnBoolean) player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(homeKey + "Home-limite-message")));

        return returnBoolean;

    }

}