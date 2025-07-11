package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.gui.BackItem;
import fr.fuzeblocks.homeplugin.gui.ForwardItem;
import fr.fuzeblocks.homeplugin.gui.HomeItem;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.task.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static fr.fuzeblocks.homeplugin.task.TaskSaveUtils.setTaskManagerInstance;

public class HomeCommand implements CommandExecutor {

    private final HomePlugin instance;
    private final String key = "Language.";

    public HomeCommand(HomePlugin instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Only-a-player-can-execute")));
            return false;
        }

        Player player = (Player) sender;

        HomeManager homeManager = HomePlugin.getHomeManager();

        if (args.length == 1) {
            String homeName = args[0];
            if (homeManager.isStatus(player)) {
                player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "A-teleport-is-already-in-progress")));
                return false;
            }

            if (homeManager.getHomeNumber(player) > 0) {
                if (isInCache(homeManager, player, homeName)) {
                    setPlayerTeleportation(player, homeName, homeManager.getCacheManager().getHomesInCache(player).get(homeName));
                    return true;
                }

                Location homeLocation = homeManager.getHomeLocation(player, homeName);
                if (homeLocation != null) {
                    homeManager.getCacheManager().addHomeInCache(player, homeName, homeLocation);
                    setPlayerTeleportation(player, homeName, homeLocation);
                    return true;
                } else {
                    player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Home-does-not-exist")));
                    return false;
                }
            } else {
                player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Have-no-home")));
                return false;
            }
        }

        if (args.length == 0) {
            if (isGuiSupported()) {
                openHomeGui(player);
            } else {
                player.sendMessage("The graphical interface is not yet compatible with this version of Minecraft.");
            }
            return true;
        }

        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Home.Home-usage-message")));
        return false;
    }

    private void setPlayerTeleportation(Player player, String homeName, Location location) {
        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key + "Start-of-teleportation")) + " " + homeName);
        StatusManager.setPlayerStatus(player, true);
        TaskManager taskManager = new TaskManager(instance);
        taskManager.homeTask(homeName, player, location);
        taskManager.startTeleportTask();
        setTaskManagerInstance(player, taskManager);
    }

    private boolean isInCache(HomeManager homeManager, Player player, String homeName) {
        Map<String, Location> homes = homeManager.getCacheManager().getHomesInCache(player);
        return homes != null && homes.containsKey(homeName);
    }

    private void openHomeGui(Player player) {
        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(""));

        Gui gui = PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < # > # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('#', border)
                .addIngredient('<', new BackItem())
                .addIngredient('>', new ForwardItem())
                .setContent(getHomeItems(player))
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle("Home List")
                .setGui(gui)
                .build();

        window.open();
    }

    private List<Item> getHomeItems(Player player) {
        HomeManager homeManager = HomePlugin.getHomeManager();
        return homeManager.getHomesName(player).stream()
                .map(homeName -> new HomeItem(homeName, instance))
                .collect(Collectors.toList());
    }

    private boolean isGuiSupported() {
        String version = Bukkit.getBukkitVersion().split("-")[0]; // e.g. "1.20.4"
        try {
            String[] parts = version.split("\\.");
            int major = Integer.parseInt(parts[1]);
            return major <= 20;
        } catch (Exception e) {
            return false;
        }
    }
}
