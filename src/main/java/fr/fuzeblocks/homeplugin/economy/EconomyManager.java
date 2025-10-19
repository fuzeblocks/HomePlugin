package fr.fuzeblocks.homeplugin.economy;

import fr.fuzeblocks.homeplugin.HomePlugin;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import static net.milkbowl.vault.economy.EconomyResponse.ResponseType.FAILURE;
import static net.milkbowl.vault.economy.EconomyResponse.ResponseType.SUCCESS;

public class EconomyManager {

    private static final Economy economy = HomePlugin.getEconomy();
    private static final ConfigurationSection config = HomePlugin.getConfigurationSection();
    private static final String economyKey = "Config.Economy.";

    public static boolean isEconomyEnabled() {
        return config.getBoolean(economyKey + "UseEconomy", false);
    }

    public static double getHomeCreationCost() {
        return config.getDouble(economyKey + "HomeCreationCost", 0.0);
    }

    public static double getHomeTeleportPrice() {
        return config.getDouble(economyKey + "Home-Teleport-Price", 0.0);
    }

    public static double getTpaRequestPrice() {
        return config.getDouble(economyKey + "Tpa-Request-Price", 0.0);
    }

    public static double getRtpPrice() {
        return config.getDouble(economyKey + "Rtp-Price", 0.0);
    }

    public static EconomyResponse.ResponseType pay(Player player, double amount) {
        if (economy.withdrawPlayer(player, amount).equals(SUCCESS)) {
            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                    "Economy.Payment-successful",
                    "&aVous avez été débité de %amount% pour cette action."
            ).replace("%amount%", String.valueOf(amount)));
            return SUCCESS;
        }
        player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                "Economy.Payment-failed",
                "&cLe paiement a échoué. Veuillez réessayer plus tard."
        ));
        return FAILURE;

    }

}
