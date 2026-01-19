package fr.fuzeblocks.homeplugin.commands.home;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.economy.EconomyManager;
import fr.fuzeblocks.homeplugin.gui.BackItem;
import fr.fuzeblocks.homeplugin.gui.ForwardItem;
import fr.fuzeblocks.homeplugin.gui.HomeItem;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import fr.fuzeblocks.homeplugin.task.TeleportationManager;
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
 * Command to teleport to a player's saved home or open the home GUI.
 */
public class HomeCommand implements CommandExecutor {

    private static final String HOME = "Home.";
    private static final String LANG = "Language.";

    /**
     * Parses a version string into a triple array [major, minor, patch].
     *
     * @param v The version string
     * @return Array of [major, minor, patch]
     */
    private static int[] parseVersionTriple(String v) {
        if (v == null) return new int[]{0, 0, 0};
        v = v.replaceAll("[^0-9.]", "");
        String[] p = v.split("\\.");
        int major = 0, minor = 0, patch = 0;
        try {
            if (p.length > 0) major = Integer.parseInt(p[0]);
        } catch (Exception ignored) {
        }
        try {
            if (p.length > 1) minor = Integer.parseInt(p[1]);
        } catch (Exception ignored) {
        }
        try {
            if (p.length > 2) patch = Integer.parseInt(p[2]);
        } catch (Exception ignored) {
        }
        return new int[]{major, minor, patch};
    }

    /**
     * Compares two version arrays.
     *
     * @param a First version array
     * @param b Second version array
     * @return Negative if a < b, 0 if equal, positive if a > b
     */
    private static int compareVersion(int[] a, int[] b) {
        for (int i = 0; i < 3; i++) {
            int cmp = Integer.compare(a[i], b[i]);
            if (cmp != 0) return cmp;
        }
        return 0;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final LanguageManager languageManager = HomePlugin.getLanguageManager();

        if (!(sender instanceof Player)) {
            sender.sendMessage(languageManager.getStringWithColor(LANG + "Only-a-player-can-execute"));
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("homeplugin.command.home")) {
            player.sendMessage(languageManager.getStringWithColor(LANG + "No-permission"));
            return false;
        }

        HomeManager homeManager = HomePlugin.getHomeManager();

        if (args.length == 1) {
            return teleportToHome(player, args[0], homeManager, languageManager);
        }

        if (args.length == 0) {
            return openHomeSelection(player, homeManager, languageManager);
        }

        player.sendMessage(languageManager.getStringWithColor(HOME + "Home-usage-message"));
        return false;
    }

    /**
     * Teleports the player to a specific home.
     *
     * @param player          The player to teleport
     * @param homeName        The name of the home
     * @param homeManager     The home manager instance
     * @param languageManager The language manager instance
     * @return true if successful, false otherwise
     */
    private boolean teleportToHome(Player player, String homeName, HomeManager homeManager, LanguageManager languageManager) {
        if (homeManager.isStatus(player)) {
            player.sendMessage(languageManager.getStringWithColor(LANG + "A-teleport-is-already-in-progress"));
            return false;
        }

        if (homeManager.getHomeNumber(player) <= 0) {
            player.sendMessage(languageManager.getStringWithColor(HOME + "Have-no-home"));
            return false;
        }

        Location homeLocation = homeManager.getHomeLocation(player, homeName);
        if (homeLocation == null) {
            player.sendMessage(languageManager.getStringWithColor(HOME + "Home-does-not-exist"));
            return false;
        }

        if (homeLocation.getWorld() == null) {
            player.sendMessage(languageManager.getStringWithColor(HOME + "Home-world-not-loaded"));
            return false;
        }

        double cost = EconomyManager.getHomeTeleportPrice();
        if (cost > 0 && !EconomyManager.pay(player, cost)) {
            player.sendMessage(languageManager.getStringWithColor(LANG + "Not-Enough-Money"));
            return false;
        }

        if (!isInCache(homeManager, player, homeName)) {
            homeManager.getCacheManager().addHome(player.getUniqueId(), homeName, homeLocation);
        }

        TeleportationManager.teleportPlayerToHome(player, homeName);
        return true;
    }

    /**
     * Opens the home selection GUI or sends a message if GUI is not supported.
     *
     * @param player          The player to show the GUI to
     * @param homeManager     The home manager instance
     * @param languageManager The language manager instance
     * @return true if successful, false otherwise
     */
    private boolean openHomeSelection(Player player, HomeManager homeManager, LanguageManager languageManager) {
        if (homeManager.getHomeNumber(player) <= 0) {
            player.sendMessage(languageManager.getStringWithColor(HOME + "Have-no-home"));
            return false;
        }

        if (isGuiSupported()) {
            openHomeGui(player);
        } else {
            player.sendMessage(languageManager.getStringWithColor(HOME + "Gui-not-supported"));
        }
        return true;
    }

    /**
     * Checks if a home is already in the cache.
     *
     * @param homeManager The home manager instance
     * @param player      The player
     * @param homeName    The home name
     * @return true if in cache, false otherwise
     */
    private boolean isInCache(HomeManager homeManager, Player player, String homeName) {
        Map<String, Location> homes = homeManager.getCacheManager().getHomes(player.getUniqueId());
        return homes != null && homes.containsKey(homeName);
    }

    /**
     * Opens the interactive home selection GUI.
     *
     * @param player The player to show the GUI to
     */
    private void openHomeGui(Player player) {
        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(""));

        List<Item> homeItems = getHomeItems(player);

        if (homeItems.isEmpty()) {
            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(HOME + "Have-no-home"));
            return;
        }

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
                .setContent(homeItems)
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle(HomePlugin.getLanguageManager().getStringWithColor(HOME + "Home-gui-title")
                        .replace("%player%", player.getName()))
                .setGui(gui)
                .build();

        window.open();
    }

    /**
     * Gets the list of home items for the GUI.
     *
     * @param player The player
     * @return List of home items
     */
    private List<Item> getHomeItems(Player player) {
        HomeManager homeManager = HomePlugin.getHomeManager();
        List<String> homeNames = homeManager.getHomesName(player);

        return homeNames.stream()
                .filter(name -> name != null)
                .map(HomeItem::new)
                .collect(Collectors.toList());
    }

    /**
     * Checks if the GUI is supported on the current server version.
     *
     * @return true if supported, false otherwise
     */
    private boolean isGuiSupported() {
        try {
            String raw = org.bukkit.Bukkit.getBukkitVersion();
            String ver = raw.split("-")[0];
            int[] current = parseVersionTriple(ver);
            int[] min = {1, 14, 0};
            int[] max = {1, 21, 11};
            return compareVersion(current, min) >= 0 && compareVersion(current, max) <= 0;
        } catch (Exception e) {
            return false;
        }
    }
}