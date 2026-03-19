package fr.fuzeblocks.homeplugin.placeholder;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.economy.EconomyManager;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The type Home plugin expansion.
 */
public class HomePluginExpansion extends PlaceholderExpansion {

    private final HomePlugin homePlugin;

    /**
     * Instantiates a new Home plugin expansion.
     *
     * @param homePlugin the home plugin
     */
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

        if ("homes".equals(lowerParams)) {
            return getHomeNames(player);
        } else if ("homes_numbers".equals(lowerParams)) {
            return getHomeNumber(player);
        } else if ("has_homes".equals(lowerParams)) {
            return hasHomes(player);
        } else if ("home_teleport_price".equals(lowerParams)) {
            return String.valueOf(EconomyManager.getHomeTeleportPrice());
        } else if ("home_creation_price".equals(lowerParams)) {
            return String.valueOf(EconomyManager.getHomeCreationCost());
        } else if ("tpa_request_price".equals(lowerParams)) {
            return String.valueOf(EconomyManager.getTpaRequestPrice());
        } else if ("rtp_price".equals(lowerParams)) {
            return String.valueOf(EconomyManager.getRtpPrice());
        } else if (lowerParams.startsWith("home_location_")) {
            String name = params.substring("home_location_".length());
            return getHomeLocation(player, name);
        } else if (lowerParams.startsWith("home_exists_")) {
            String name = params.substring("home_exists_".length());
            return homeExists(player, name);
        } else if (lowerParams.startsWith("home_world_")) {
            String name = params.substring("home_world_".length());
            return getHomeWorld(player, name);
        } else if (lowerParams.startsWith("home_coordinates_")) {
            String name = params.substring("home_coordinates_".length());
            return getHomeCoordinates(player, name);
        }

        return null;
    }


    private String getHomeNames(Player player) {
        List<String> homeNames = HomePlugin.getHomeManager().getHomesName(player);
        if (homeNames != null && !homeNames.isEmpty()) {
            return String.join(", ", homeNames);
        }
        return translate("Messages.no-homes", "Aucun home");
    }

    private String getHomeNumber(Player player) {
        return String.valueOf(HomePlugin.getHomeManager().getHomeNumber(player));
    }

    private String hasHomes(Player player) {
        return HomePlugin.getHomeManager().getHomeNumber(player) > 0 ? "true" : "false";
    }

    private String getHomeLocation(Player player, String name) {
        Location loc = HomePlugin.getHomeManager().getHomeLocation(player, name);
        if (loc == null) return translate("Messages.Unknown", "Inconnu");
        String format = translate("Messages.home-location-format", "X: %.1f, Y: %.1f, Z: %.1f, Monde: %s");
        return String.format(format, loc.getX(), loc.getY(), loc.getZ(), loc.getWorld().getName());
    }

    private String homeExists(Player player, String name) {
        return HomePlugin.getHomeManager().exist(player, name) ? "true" : "false";
    }

    private String getHomeWorld(Player player, String name) {
        Location loc = HomePlugin.getHomeManager().getHomeLocation(player, name);
        return (loc != null) ? loc.getWorld().getName() : translate("Messages.Unknown", "Inconnu");
    }

    private String getHomeCoordinates(Player player, String name) {
        Location loc = HomePlugin.getHomeManager().getHomeLocation(player, name);
        return (loc != null) ? String.format("%.1f %.1f %.1f", loc.getX(), loc.getY(), loc.getZ())
                : translate("Messages.Unknown", "Inconnu");
    }

    private String translate(String path, String defaultValue) {
        String value = HomePlugin.getLanguageManager().getString(path);
        if (value == null || value.isEmpty()) {
            value = defaultValue;
        }
        return LanguageManager.translateAlternateColorCodes(value);
    }
}
