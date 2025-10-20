package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.event.OnHomeCreatedEvent;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.home.HomePermissionManager;
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
 * The type Set home command.
 */
public class SetHomeCommand implements CommandExecutor {
    private static final String HOME = "Home.";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String LANG = "Language.";
        if (!(sender instanceof Player)) {
            sender.sendMessage(translate(LANG + "Only-a-player-can-execute"));
            return false;
        }

        if (!sender.hasPermission("homeplugin.command.sethome")) {
            sender.sendMessage(translate(HOME + "SetHome-permission-deny-message"));
            return false;
        }

        Player player = (Player) sender;
        HomeManager homeManager = HomePlugin.getHomeManager();

        // /sethome info
        if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
            int used = homeManager.getHomeNumber(player);
            int max = HomePermissionManager.getMaxHomes(player);
            int remaining = max - used;

            player.sendMessage(translate(HOME + "Home-info-count")
                    .replace("%used%", String.valueOf(used))
                    .replace("%max%", String.valueOf(max))
                    .replace("%remaining%", String.valueOf(Math.max(0, remaining))));
            return true;
        }

        // /sethome <name>
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

            if (!event.isCancelled()) {
                boolean success = homeManager.addHome(player, event.getHomeName());
                player.sendMessage(translate(success ? HOME + "Home-added" : LANG + "Error"));
            }

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

    private static String translate(String key) {
        return HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key));
    }

    /**
     * Is fair boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public static boolean isFair(Player player) {
        Location loc = player.getLocation();
        World world = loc.getWorld();
        if (world == null) return false;


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

    /**
     * Is on floating platform boolean.
     *
     * @param loc the loc
     * @return the boolean
     */
    public static boolean isOnFloatingPlatform(Location loc) {
        World world = loc.getWorld();
        if (world == null) return false;

        // considérer comme "vraiment flottant" uniquement si il y a une colonne d'air suffisamment profonde
        final int requiredAirDepth = 20; // augmenter la profondeur réduit les faux positifs
        for (int i = 1; i <= requiredAirDepth; i++) {
            Block b = world.getBlockAt(loc.getBlockX(), loc.getBlockY() - i, loc.getBlockZ());
            Material t = b.getType();
            if (t != Material.AIR && t != Material.CAVE_AIR) {
                // on a rencontré un bloc solide assez proche -> pas une plateforme flottante profonde
                return false;
            }
        }

        // ne considérer les très basses altitudes (caves profondes) que si on est en-dessous d'un seuil
        if (loc.getY() >= 60) {
            return false; // trop haut -> probablement pas une plateforme flottante problématique
        }

        // vérifier la zone directement sous les pieds (1 bloc en dessous) : si il y a du support, ce n'est pas flottant
        Block below = world.getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ());
        Material belowType = below.getType();
        if (belowType != Material.AIR && belowType != Material.CAVE_AIR) {
            return false; // soutien direct -> pas flottant
        }

        // vérifier une petite aire sous les pieds (3x3) au niveau -1 : si un bloc solide y est présent, considérer comme non-flottant
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                Block b = world.getBlockAt(loc.getBlockX() + dx, loc.getBlockY() - 1, loc.getBlockZ() + dz);
                Material t = b.getType();
                if (t != Material.AIR && t != Material.CAVE_AIR) {
                    return false;
                }
            }
        }

        // si on arrive ici, il s'agit d'un vide profond sous les pieds à basse altitude -> considérer comme plateforme flottante
        return true;
    }

}
