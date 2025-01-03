package fr.fuzeblocks.homeplugin.placeholder;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.home.sql.HomeSQLManager;
import fr.fuzeblocks.homeplugin.home.yml.HomeYMLManager;
import fr.fuzeblocks.homeplugin.sync.type.SyncMethod;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class HomePluginExpansion extends PlaceholderExpansion {
    private final HomePlugin homePlugin;

    public HomePluginExpansion(HomePlugin homePlugin) {
        this.homePlugin = homePlugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return homePlugin.getDescription().getName();
    }

    @Override
    public @NotNull String getAuthor() {
        return "fuzeblocks";
    }

    @Override
    public @NotNull String getVersion() {
        return homePlugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return "";
        }


        if (params.equalsIgnoreCase("homes")) {
            List<String> homesList = getHomesList(player.getPlayer());
            if (homesList != null && !homesList.isEmpty()) {
                return String.join(", ", homesList);
            } else {
                return "No homes";
            }
        }

        if (params.equalsIgnoreCase("homes_numbers")) {
            return String.valueOf(getHomes((Player) player));
        }

        if (params.toLowerCase().startsWith("home_location_")) {
            String homeName = params.substring("home_location_".length());
            Location homeLocation = getHomeLocation(player.getPlayer(), homeName);
            if (homeLocation != null) {
                return homeLocation.toString();
            } else {
                return "Unknown";
            }
        }
        return null;
    }

    private List<String> getHomesList(Player player) {
            return HomePlugin.getHomeManager().getHomesName(player);
    }

    private Location getHomeLocation(Player player, String s) {
        return HomePlugin.getHomeManager().getHomeLocation(player, s);
    }

    private int getHomes(Player player) {
        return HomePlugin.getHomeManager().getHomeNumber(player);
    }
}
