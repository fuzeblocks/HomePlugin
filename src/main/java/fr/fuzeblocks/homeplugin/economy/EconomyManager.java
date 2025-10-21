package fr.fuzeblocks.homeplugin.economy;

import fr.fuzeblocks.homeplugin.HomePlugin;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;

public class EconomyManager {

    private static Economy economy;
    private static ConfigurationSection config;
    private static final String ECONOMY_KEY = "Config.Economy.";

    public static void setup(HomePlugin plugin) {
        economy = plugin.getEconomy();
        config = plugin.getConfig();
    }


    public static double getCost(String key, double defaultValue) {
        return config != null ? config.getDouble(ECONOMY_KEY + key, defaultValue) : defaultValue;
    }

    public static double getHomeCreationCost() {
        return getCost("HomeCreationCost", 0.0);
    }

    public static double getHomeTeleportPrice() {
        return getCost("Home-Teleport-Price", 0.0);
    }

    public static double getTpaRequestPrice() {
        return getCost("Tpa-Request-Price", 0.0);
    }

    public static double getRtpPrice() {
        return getCost("Rtp-Price", 0.0);
    }

    public static EconomyResponse.ResponseType pay(Player player, double amount) {
        if (economy == null) {
            player.sendMessage("§cAucun système économique détecté.");
            return EconomyResponse.ResponseType.FAILURE;
                }
        if (amount <= 0) {
            player.sendMessage("§cLe montant du paiement doit être supérieur à zéro.");
            return EconomyResponse.ResponseType.FAILURE;
        }

        EconomyResponse response = economy.withdrawPlayer(player, amount);
        String messageKey = response.transactionSuccess() ? "Economy.Payment-successful" : "Economy.Payment-failed";
        String defaultMessage = response.transactionSuccess()
                ? "&aVous avez été débité de %amount% pour cette action."
                : "&cLe paiement a échoué. Veuillez réessayer plus tard.";

        player.sendMessage(HomePlugin.getLanguageManager()
                .getStringWithColor(messageKey, defaultMessage)
                .replace("%amount%", String.valueOf(amount)));

        return response.transactionSuccess() ? EconomyResponse.ResponseType.SUCCESS : EconomyResponse.ResponseType.FAILURE;
    }
}
