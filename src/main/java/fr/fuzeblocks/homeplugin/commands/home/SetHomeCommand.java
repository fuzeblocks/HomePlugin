package fr.fuzeblocks.homeplugin.commands.home;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.economy.EconomyManager;
import fr.fuzeblocks.homeplugin.event.OnHomeCreatedEvent;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.home.HomePermissionManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Command to set a home at the player's current location.
 */
public class SetHomeCommand implements CommandExecutor {
    private static final String HOME = "Home.";
    private static final String LANG = "Language.";
    private static final String PERMISSION_SET_HOME = "homeplugin.command.sethome";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(translate(LANG + "Only-a-player-can-execute"));
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(PERMISSION_SET_HOME)) {
            player.sendMessage(translate(HOME + "SetHome-permission-deny-message"));
            return false;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
            HomeManager homeManager = HomePlugin.getHomeManager();
            int used = homeManager.getHomeNumber(player);
            int max = HomePermissionManager.getMaxHomes(player);
            int remaining = Math.max(0, max - used);

            player.sendMessage(translate(HOME + "Home-info-count")
                    .replace("%used%", String.valueOf(used))
                    .replace("%max%", String.valueOf(max))
                    .replace("%remaining%", String.valueOf(remaining)));
            return true;
        }

        if (args.length == 1) {
            String homeName = args[0];
            return createHome(player, homeName);
        }


        sendUsage(player);
        return false;
    }



    private void sendUsage(Player player) {
        player.sendMessage(ChatColor.DARK_GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

        // Title
        String title = translate(HOME + "SetHome-management-title");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "         " + title));
        player.sendMessage("");

        // Get usage icon from language file
        String usageIcon = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', translate(HOME + "SetHome-usage-icon")));

        // /sethome <home-name> - clickable
        TextComponent viewCmd = new TextComponent("  " + usageIcon + " ");
        viewCmd.setColor(ChatColor.DARK_GRAY);

        TextComponent viewText = new TextComponent("/sethome <home-name>");
        viewText.setColor(ChatColor.YELLOW);
        viewText.setBold(false);
        viewText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/sethome <home-name>"));

        String viewHover = translate(HOME + "SetHome-click-suggest");
        viewText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.stripColor(viewHover))
                        .color(ChatColor.GRAY).create()));

        String viewDesc = translate(HOME + "SetHome-view-description");
        TextComponent viewDescComp = new TextComponent(" - " + ChatColor.stripColor(viewDesc));
        viewDescComp.setColor(ChatColor.GRAY);

        viewCmd.addExtra(viewText);
        viewCmd.addExtra(viewDescComp);
        player.spigot().sendMessage(viewCmd);
        player.sendMessage("");
        player.sendMessage(ChatColor.DARK_GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }


    /**
     * Creates a new home for the player.
     *
     * @param player The player creating the home
     * @param homeName The name of the home
     * @return true if successful, false otherwise
     */
    private boolean createHome(Player player, String homeName) {
        HomeManager homeManager = HomePlugin.getHomeManager();

        if (homeManager.isStatus(player)) {
            player.sendMessage(translate(LANG + "A-teleport-is-already-in-progress"));
            return false;
        }

        if (!canSetHome(player)) {
            return false;
        }

        if (homeManager.exist(player, homeName)) {
            player.sendMessage(translate(HOME + "Home-Already-Exists"));
            return false;
        }

        if (HomePlugin.getConfigurationSection().getBoolean("Config.Home.Prevent-Unfair-Location", true)) {
            if (!isFair(player)) {
                return false;
            }
        }

        Location loc = player.getLocation();
        OnHomeCreatedEvent event = new OnHomeCreatedEvent(player, loc, homeName);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }

        double cost = EconomyManager.getHomeCreationCost();
        if (cost > 0 && !EconomyManager.pay(player, cost)) {
            player.sendMessage(translate(LANG + "Not-Enough-Money"));
            return false;
        }

        boolean success = homeManager.addHome(player, event.getHomeName());
        if (success) {
            player.sendMessage(translate(HOME + "Home-added"));
        } else {
            player.sendMessage(translate(LANG + "Error"));
        }

        return success;
    }

    /**
     * Checks if the player can set a home based on their limit.
     *
     * @param player The player to check
     * @return true if allowed, false otherwise
     */
    private boolean canSetHome(Player player) {
        String bypassPerm = HomePlugin.getLanguageManager().getString(HOME + "Home-limite-permission-for-bypass");
        if (bypassPerm != null && player.hasPermission(bypassPerm)) {
            return true;
        }

        boolean allowed = HomePermissionManager.canSetHome(player);
        if (!allowed) {
            player.sendMessage(translate(HOME + "Home-limite-message"));
        }

        return allowed;
    }

    /**
     * Checks if the player's location is valid for setting a home.
     *
     * @param player The player
     * @return true if location is fair, false otherwise
     */
    public static boolean isFair(Player player) {
        Location loc = player.getLocation();
        World world = loc.getWorld();

        if (world == null) {
            player.sendMessage(translate(HOME + "Invalid-world"));
            return false;
        }

        if (loc.getY() >= world.getMaxHeight() - 2 || loc.getY() <= getWorldMinY(world) + 5) {
            player.sendMessage(translate(HOME + "Invalid-height"));
            return false;
        }

        Material mat = loc.getBlock().getType();
        if (mat == Material.NETHER_PORTAL || mat == Material.END_PORTAL || mat == Material.END_GATEWAY) {
            player.sendMessage(translate(HOME + "Portal-location"));
            return false;
        }

        if (mat == Material.WATER || mat == Material.BUBBLE_COLUMN || mat == Material.LAVA) {
            player.sendMessage(translate(HOME + "Water-location"));
            return false;
        }

        List<String> disabledWorlds = HomePlugin.getConfigurationSection() == null
                ? List.of()
                : HomePlugin.getConfigurationSection().getStringList("Config.Home.Disabled-Worlds");

        if (!disabledWorlds.isEmpty() &&
                disabledWorlds.stream()
                        .map(String::toLowerCase)
                        .anyMatch(name -> name.equals(world.getName().toLowerCase()))) {
            player.sendMessage(translate(HOME + "Disabled-world"));
            return false;
        }

        Block ground = world.getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
        if (!ground.getType().isSolid()) {
            player.sendMessage(translate(HOME + "No-solid-ground"));
            return false;
        }

        if (isOnFloatingPlatform(loc)) {
            player.sendMessage(translate(HOME + "Floating-platform"));
            return false;
        }

        return true;
    }

    /**
     * Checks if the location is on a floating platform.
     *
     * @param loc The location to check
     * @return true if on floating platform, false otherwise
     */
    public static boolean isOnFloatingPlatform(Location loc) {
        World world = loc.getWorld();
        if (world == null) return false;

        final int x = loc.getBlockX();
        final int y = loc.getBlockY();
        final int z = loc.getBlockZ();

        Block ground = world.getBlockAt(x, y - 1, z);
        if (!ground.getType().isSolid()) {
            return false;
        }

        final int requiredAirDepth = Math.max(1, HomePlugin.getConfigurationSection().getInt("Config.Home.FloatingCheck.AirDepth", 5));
        final int horizontalRadius = Math.max(1, HomePlugin.getConfigurationSection().getInt("Config.Home.FloatingCheck.HorizontalRadius", 1));

        int minY = getWorldMinY(world);

        for (int i = 1; i <= requiredAirDepth; i++) {
            int checkY = ground.getY() - i;
            if (checkY < minY) {
                return false;
            }
            Material t = world.getBlockAt(x, checkY, z).getType();
            if (!isAir(t)) {
                return false;
            }
        }

        if (hasNearbySolidBlocks(world, ground.getX(), ground.getY(), ground.getZ(), horizontalRadius)) {
            return false;
        }

        if (hasNearbySolidBlocks(world, ground.getX(), ground.getY() - 1, ground.getZ(), horizontalRadius)) {
            return false;
        }

        return true;
    }

    /**
     * Checks if a material is air.
     *
     * @param t The material to check
     * @return true if air, false otherwise
     */
    private static boolean isAir(Material t) {
        return t == Material.AIR || t == Material.CAVE_AIR || t == Material.VOID_AIR;
    }

    /**
     * Gets the minimum Y level of the world.
     *
     * @param world The world
     * @return The minimum Y level
     */
    private static int getWorldMinY(World world) {
        try {
            return world.getMinHeight();
        } catch (NoSuchMethodError e) {
            return 0;
        }
    }

    /**
     * Checks if there are solid blocks nearby.
     *
     * @param world The world
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param radius Search radius
     * @return true if solid blocks found, false otherwise
     */
    private static boolean hasNearbySolidBlocks(World world, int x, int y, int z, int radius) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx == 0 && dz == 0) continue;
                Material type = world.getBlockAt(x + dx, y, z + dz).getType();
                if (type.isSolid()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Translates a language key to colored text.
     *
     * @param key The language key
     * @return The translated colored text
     */
    private static String translate(String key) {
        return HomePlugin.getLanguageManager().getStringWithColor(key);
    }
}