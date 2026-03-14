package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.update.UpdateDownloader;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jspecify.annotations.NonNull;

public class UpdateCommand implements CommandExecutor {

    private final HomePlugin homePlugin;
    public UpdateCommand(HomePlugin homePlugin) {
        this.homePlugin = homePlugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                Bukkit.getScheduler().runTaskAsynchronously(HomePlugin.getPlugin(HomePlugin.class), () -> {
                    UpdateDownloader downloader = new UpdateDownloader();
                    downloader.computeLogged("plugins/");
                    homePlugin.getUpdateChecker().setMarkForUpdatePlugin(true);
                      Bukkit.getScheduler().runTask(HomePlugin.getPlugin(HomePlugin.class), () -> {
                          homePlugin.getServer().shutdown();
                      });
                });
            }
        }
        return false;
    }
}
