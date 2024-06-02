package fr.fuzeblocks.homeplugin.placeholder;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.yml.HomeManager;
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
        System.out.println("Return null");
        return null;
    }

    private List<String> getHomesList(Player player) {
        if (HomePlugin.getRegistrationType().equals(SyncMethod.YAML)) {
            HomeManager homeManager = HomePlugin.getHomeManager();
            return homeManager.getHomesName(player);
        } else {
            fr.fuzeblocks.homeplugin.home.sql.HomeManager homeManager = HomePlugin.getHomeSQLManager();
            return homeManager.getHomesName(player);
        }
    }

    private Location getHomeLocation(Player player, String s) {
        if (HomePlugin.getRegistrationType().equals(SyncMethod.YAML)) {
            HomeManager homeManager = HomePlugin.getHomeManager();
            return homeManager.getHomeLocation(player, s);
        } else {
            fr.fuzeblocks.homeplugin.home.sql.HomeManager homeManager = HomePlugin.getHomeSQLManager();
            return homeManager.getHomeLocation(player, s);
        }
    }

    private int getHomes(Player player) {
        if (HomePlugin.getRegistrationType().equals(SyncMethod.YAML)) {
            HomeManager homeManager = HomePlugin.getHomeManager();
            return homeManager.getHomeNumber(player);
        } else {
            fr.fuzeblocks.homeplugin.home.sql.HomeManager homeManager = HomePlugin.getHomeSQLManager();
            return homeManager.getHomeNumber(player);
        }
    }
}
