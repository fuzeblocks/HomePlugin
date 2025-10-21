package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.economy.EconomyManager;
import fr.fuzeblocks.homeplugin.event.OnHomeCreatedEvent;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.home.HomePermissionManager;
import net.milkbowl.vault.economy.EconomyResponse;
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

public class SetHomeCommand implements CommandExecutor {
    private static final String HOME = "Home.";
    private static final String LANG = "Language.";
    private static final String PERMISSION_SET_HOME = "homeplugin.command.sethome";
    private static final String MESSAGE_PERMISSION_DENIED = HOME + "SetHome-permission-deny-message";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(translate(LANG + "Only-a-player-can-execute"));
            return false;
        }

        if (!sender.hasPermission(PERMISSION_SET_HOME)) {
            sender.sendMessage(translate(MESSAGE_PERMISSION_DENIED));
            return false;
        }

        Player player = (Player) sender;
        HomeManager homeManager = HomePlugin.getHomeManager();

        if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
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
            if (homeManager.isStatus(player)) {
                player.sendMessage(translate(LANG + "A-teleport-is-already-in-progress"));
                return false;
            }

            if (!canSetHome(player)) return false;

            if (HomePlugin.getConfigurationSection().getBoolean("Config.Home.PreventUnfairLocation", true)) {
                if (!isFair(player)) return false;
            }

            Location loc = player.getLocation();
            String homeName = args[0];
            OnHomeCreatedEvent event = new OnHomeCreatedEvent(player, loc, homeName);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) return true;

            if (homeManager.exist(player, event.getHomeName())) {
                player.sendMessage(translate(LANG + "Home-Already-Exists"));
                return true;
            }

            double cost = EconomyManager.getHomeCreationCost();
            if (EconomyManager.pay(player, cost).equals(EconomyResponse.ResponseType.FAILURE)) {
                player.sendMessage(translate(LANG + "Not-Enough-Money"));
                return true;
            }

            boolean success = homeManager.addHome(player, event.getHomeName());
            player.sendMessage(translate(success ? HOME + "Home-added" : LANG + "Error"));

            return true;
        }

        player.sendMessage(translate(HOME + "SetHome-usage-message"));
        return false;
    }

    private boolean canSetHome(Player player) {
        String bypassPerm = HomePlugin.getLanguageManager().getString(HOME + "Home-limite-permission-for-bypass");
        if (player.hasPermission(bypassPerm)) return true;

        boolean allowed = HomePermissionManager.canSetHome(player);
        if (!allowed) {
            player.sendMessage(translate(HOME + "Home-limite-message"));
        }

        return allowed;
    }

    private String translate(String key) {
        return HomePlugin.getLanguageManager().getStringWithColor(key);
    }

    private boolean isFair(Player player) {
        Location loc = player.getLocation();
        World world = loc.getWorld();

        if (loc.getY() >= world.getMaxHeight() - 2 || loc.getY() <= 5) {
            player.sendMessage(translate(HOME + "Invalid-height"));
            return false;
        }

        Material mat = loc.getBlock().getType();
        if (mat == Material.NETHER_PORTAL || mat == Material.END_PORTAL || mat == Material.END_GATEWAY) {
            player.sendMessage(translate(HOME + "Portal-location"));
            return false;
        }

        if (mat == Material.WATER || mat == Material.BUBBLE_COLUMN) {
            player.sendMessage(translate(HOME + "Water-location"));
            return false;
        }

        List<String> disabledWorlds = HomePlugin.getConfigurationSection()
                .getStringList("Config.Home.DisabledWorlds");

        if (disabledWorlds.stream()
                .map(String::toLowerCase)
                .anyMatch(name -> name.equals(world.getName().toLowerCase()))) {
            player.sendMessage(translate(HOME + "End-disabled"));
            return false;
        }

        if (isOnFloatingPlatform(loc) || mat == Material.AIR) {
            player.sendMessage(translate(HOME + "Floating-platform"));
            return false;
        }
        return true;
    }

    private boolean isOnFloatingPlatform(Location loc) {
        World world = loc.getWorld();

        for (int i = 1; i <= 10; i++) {
            Block b = world.getBlockAt(loc.getBlockX(), loc.getBlockY() - i, loc.getBlockZ());
            if (b.getType() != Material.AIR) {
                return false;
            }
        }

        return loc.getY() < 100 && !hasNearbySolidBlocks(world, loc);
    }

    private boolean hasNearbySolidBlocks(World world, Location loc) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) continue;
                if (world.getBlockAt(loc.getBlockX() + dx, loc.getBlockY(), loc.getBlockZ() + dz).getType() != Material.AIR) {
                    return true;
                }
            }
        }
        return false;
    }
}
