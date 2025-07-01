package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.share.PublicHomeManager;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.task.TaskManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static fr.fuzeblocks.homeplugin.task.TaskSaveUtils.setTaskManagerInstance;

public class PublicHomeCommand implements CommandExecutor {

    private final PublicHomeManager publicHomeManager = new PublicHomeManager();
    private final HomePlugin instance;

    public PublicHomeCommand(HomePlugin instance) {
        this.instance = instance;
    }

    private String tr(String key) {
        return HomePlugin.translateAlternateColorCodes(instance.getLanguageManager().getString(key));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(tr("Language.Only-a-player-can-execute"));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(tr("PublicHome.Usage"));
            return true;
        }

        String action = args[0].toLowerCase();

        switch (action) {
            case "add":
                if (args.length < 2) {
                    player.sendMessage(tr("PublicHome.AddUsage"));
                    return true;
                }
                String homeName = args[1];
                if (publicHomeManager.addHome(player.getLocation() ,homeName)) {
                    player.sendMessage(tr("PublicHome.AddSuccess").replace("{homeName}", homeName));
                } else {
                    player.sendMessage(tr("PublicHome.AddFail").replace("{homeName}", homeName));
                }
                break;

            case "list":
                String homesList = String.join(", ", publicHomeManager.getHomesName());
                if (homesList.isEmpty()) {
                    player.sendMessage(tr("PublicHome.NoHomes"));
                } else {
                    player.sendMessage(tr("PublicHome.List").replace("{homes}", homesList));
                }
                break;

            case "delete":
                if (args.length < 2) {
                    player.sendMessage(tr("PublicHome.DeleteUsage"));
                    return true;
                }
                String deleteHomeName = args[1];
                if (publicHomeManager.deleteHome(deleteHomeName)) {
                    player.sendMessage(tr("PublicHome.DeleteSuccess").replace("{homeName}", deleteHomeName));
                } else {
                    player.sendMessage(tr("PublicHome.DeleteFail").replace("{homeName}", deleteHomeName));
                }
                break;

            case "rename":
                if (args.length < 3) {
                    player.sendMessage(tr("PublicHome.RenameUsage"));
                    return true;
                }
                String oldName = args[1];
                String newName = args[2];
                if (publicHomeManager.renameHome(oldName, newName)) {
                    player.sendMessage(tr("PublicHome.RenameSuccess")
                            .replace("{oldName}", oldName)
                            .replace("{newName}", newName));
                } else {
                    player.sendMessage(tr("PublicHome.RenameFail").replace("{oldName}", oldName));
                }
                break;
            case "tp":
                if (args.length < 2) {
                    player.sendMessage(tr("PublicHome.TpUsage"));
                    return true;
                }
                String tpHomeName = args[1];
                if (publicHomeManager.exist(tpHomeName)) {
                    setPlayerTeleportation(player,tpHomeName, publicHomeManager.getHomeLocation(tpHomeName));
                    player.sendMessage(tr("PublicHome.TpSuccess").replace("{homeName}", tpHomeName));
                } else {
                    player.sendMessage(tr("PublicHome.TpFail").replace("{homeName}", tpHomeName));
                }
                break;

            default:
                player.sendMessage(tr("PublicHome.UnknownAction"));
                break;
        }

        return true;
    }
    private void setPlayerTeleportation(Player player, String homeName, Location location) {
        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Language.Start-of-teleportation")) + " " + homeName);
        StatusManager.setPlayerStatus(player, true);
        TaskManager taskManager = new TaskManager(instance);
        taskManager.homeTask(homeName, player, location);
        taskManager.startTeleportTask();
        setTaskManagerInstance(player, taskManager);
    }
}
