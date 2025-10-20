package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.gui.BackItem;
import fr.fuzeblocks.homeplugin.gui.ForwardItem;
import fr.fuzeblocks.homeplugin.gui.HomeItem;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.task.TeleportationManager;
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


/**
 * The type Home command.
 */
public class HomeCommand implements CommandExecutor {

    private final String HOME = "Home.";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Only-a-player-can-execute")));
            return false;
        }

        if (!sender.hasPermission("homeplugin.command.home")) {
            sender.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("No-permission")));
            return false;
        }

        Player player = (Player) sender;

        HomeManager homeManager = HomePlugin.getHomeManager();

        if (args.length == 1) {
            String homeName = args[0];
            if (homeManager.isStatus(player)) {
                player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("A-teleport-is-already-in-progress")));
                return false;
            }

            if (homeManager.getHomeNumber(player) > 0) {
                if (isInCache(homeManager, player, homeName)) {
                    TeleportationManager.teleportPlayerToHome(player, homeName);
                    return true;
                }

                Location homeLocation = homeManager.getHomeLocation(player, homeName);
                if (homeLocation != null) {
                    homeManager.getCacheManager().addHome(player.getUniqueId(), homeName, homeLocation);
                    TeleportationManager.teleportPlayerToHome(player, homeName);
                    return true;
                } else {
                    player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Home-does-not-exist")));
                    return false;
                }
            } else {
                player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Have-no-home")));
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

        player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Home-usage-message")));
        return false;
    }

    private boolean isInCache(HomeManager homeManager, Player player, String homeName) {
        Map<String, Location> homes = homeManager.getCacheManager().getHomes(player.getUniqueId());
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
                .setTitle(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Home-gui-title").replace("%player%", player.getName())))
                .setGui(gui)
                .build();

        window.open();
    }

    private List<Item> getHomeItems(Player player) {
        HomeManager homeManager = HomePlugin.getHomeManager();
        return homeManager.getHomesName(player).stream()
                .map(HomeItem::new)
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
