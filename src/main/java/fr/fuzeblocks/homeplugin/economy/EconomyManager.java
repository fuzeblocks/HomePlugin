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

        // --- DEBUG ---
        if (config == null) {
            plugin.getLogger().warning("§c[EconomyManager] config == null !");
            return;
        }

        String path = "Config.Economy";
        if (config.isConfigurationSection(path)) {
            plugin.getLogger().info("[EconomyManager] Section '" + path + "' trouvée !");
            double creation = config.getDouble(path + ".HomeCreationCost", -1);
            double teleport = config.getDouble(path + ".Home-Teleport-Price", -1);
            double tpa = config.getDouble(path + ".Tpa-Request-Price", -1);
            double rtp = config.getDouble(path + ".Rtp-Price", -1);
            boolean useEco = config.getBoolean(path + ".UseEconomy", false);

            plugin.getLogger().info("  UseEconomy: " + useEco);
            plugin.getLogger().info("  HomeCreationCost: " + creation);
            plugin.getLogger().info("  Home-Teleport-Price: " + teleport);
            plugin.getLogger().info("  Tpa-Request-Price: " + tpa);
            plugin.getLogger().info("  Rtp-Price: " + rtp);
        } else {
            plugin.getLogger().warning("[EconomyManager] Section '" + path + "' introuvable dans config.yml !");
            plugin.getLogger().info("Clés présentes à la racine : " + config.getKeys(false));
        }
        // --- FIN DEBUG ---
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
