package fr.fuzeblocks.homeplugin.home;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.entity.Player;

/**
 * The type Home permission manager.
 */
public class HomePermissionManager {

    private static final int DEFAULT_LIMIT = HomePlugin.getConfigurationSection().getInt("Config.Home.DefaultHomeLimit");

    /**
     * Gets max homes.
     *
     * @param player the player
     * @return the max homes
     */
    public static int getMaxHomes(Player player) {
        int maxLimit = DEFAULT_LIMIT;
        for (int i = 1; i <= 100; i++) {
            if (player.hasPermission("homeplugin.limit." + i)) {
                maxLimit = i;
            }
        }
        return maxLimit;
    }

    /**
     * Can set home boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public static boolean canSetHome(Player player) {
        return HomePlugin.getHomeManager().getHomeNumber(player) < getMaxHomes(player);
    }

}
