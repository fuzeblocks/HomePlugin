package fr.fuzeblocks.homeplugin.placeholder;

import fr.fuzeblocks.homeplugin.HomePlugin;
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
        return "homeplugin";
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
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        if (!offlinePlayer.isOnline()) {
            return translate("Messages.Player-is-not-online", "Joueur hors-ligne");
        }
        Player player = offlinePlayer.getPlayer();
        if (player == null) return "";

        String lowerParams = params.toLowerCase();

        switch (lowerParams) {
            case "homes":
                List<String> homeNames = HomePlugin.getHomeManager().getHomesName(player);
                if (homeNames != null && !homeNames.isEmpty()) {
                    return String.join(", ", homeNames);
                } else {
                    return translate("Messages.no-homes", "Aucun home");
                }

            case "homes_numbers":
                return String.valueOf(HomePlugin.getHomeManager().getHomeNumber(player));

            case "has_homes":
                return HomePlugin.getHomeManager().getHomeNumber(player) > 0
                        ? "true" : "false";
        }

        if (lowerParams.startsWith("home_location_")) {
            String name = params.substring("home_location_".length());
            Location loc = HomePlugin.getHomeManager().getHomeLocation(player, name);
            if (loc != null) {
                String format = translate("Messages.home-location-format", "X: %.1f, Y: %.1f, Z: %.1f, Monde: %s");
                return String.format(format, loc.getX(), loc.getY(), loc.getZ(), loc.getWorld().getName());
            } else {
                return translate("Messages.Unknown", "Inconnu");
            }
        }

        if (lowerParams.startsWith("home_exists_")) {
            String name = params.substring("home_exists_".length());
            boolean exists = HomePlugin.getHomeManager().exist(player, name);
            return exists ? "true" : "false";
        }

        if (lowerParams.startsWith("home_world_")) {
            String name = params.substring("home_world_".length());
            Location loc = HomePlugin.getHomeManager().getHomeLocation(player, name);
            if (loc != null) {
                return loc.getWorld().getName();
            } else {
                return translate("Messages.Unknown", "Inconnu");
            }
        }

        if (lowerParams.startsWith("home_coordinates_")) {
            String name = params.substring("home_coordinates_".length());
            Location loc = HomePlugin.getHomeManager().getHomeLocation(player, name);
            if (loc != null) {
                return String.format("%.1f %.1f %.1f", loc.getX(), loc.getY(), loc.getZ());
            } else {
                return translate("Messages.Unknown", "Inconnu");
            }
        }

        return null;
    }

    private String translate(String path, String defaultValue) {
        String value = HomePlugin.getLanguageManager().getString(path);
        if (value == null || value.isEmpty()) {
            value = defaultValue;
        }
        return HomePlugin.translateAlternateColorCodes(value);
    }
}
