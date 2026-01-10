package fr.fuzeblocks.homeplugin.economy;

import fr.fuzeblocks.homeplugin.HomePlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Economy manager with a global toggle (Config.Economy.UseEconomy).
 * <ul>
 *   <li>If disabled, payments are skipped silently (return {@code true}).</li>
 *   <li>If amount {@literal <=} 0, payments are skipped silently (return {@code true}).</li>
 *   <li>Returns boolean instead of Vault's ResponseType for wider compatibility.</li>
 * </ul>
 */
public class EconomyManager {

    private static Economy economy;
    private static ConfigurationSection config;
    private static final String ECONOMY_KEY = "Config.Economy.";
    private static boolean enabled = true;

    /**
     * Initialize economy settings and cache config reference.
     *
     * @param plugin the plugin instance
     */
    public static void setup(HomePlugin plugin) {
        economy = plugin.getEconomy();
        config = plugin.getConfig();
        enabled = config != null && config.getBoolean(ECONOMY_KEY + "UseEconomy", true);
    }

    /**
     * Is enabled boolean.
     *
     * @return true if economy is enabled via config
     */
    public static boolean isEnabled() {
        return enabled;
    }

    /**
     * Read a cost value from config.
     *
     * @param key          config key suffix (appended to {@code Config.Economy.})
     * @param defaultValue default value if config is missing
     * @return the configured cost
     */
    public static double getCost(String key, double defaultValue) {
        return config != null ? config.getDouble(ECONOMY_KEY + key, defaultValue) : defaultValue;
    }

    /**
     * Gets home creation cost.
     *
     * @return home creation cost
     */
    public static double getHomeCreationCost() {
        return getCost("Home-Creation-Price", 0.0);
    }

    /**
     * Gets home teleport price.
     *
     * @return home teleport price
     */
    public static double getHomeTeleportPrice() {
        return getCost("Home-Teleport-Price", 0.0);
    }

    /**
     * Gets tpa request price.
     *
     * @return TPA request price
     */
    public static double getTpaRequestPrice() {
        return getCost("Tpa-Request-Price", 0.0);
    }

    /**
     * Gets rtp price.
     *
     * @return RTP price
     */
    public static double getRtpPrice() {
        return getCost("RTP-Price", 0.0);
    }

    /**
     * Attempts to withdraw the given amount from the player's balance.
     * Behavior:
     * <ul>
     *   <li>If economy is disabled (UseEconomy=false), returns {@code true} without message.</li>
     *   <li>If amount {@literal <=} 0, returns {@code true} without message.</li>
     *   <li>If Vault is not present, sends an error message and returns {@code false}.</li>
     *   <li>Otherwise performs a Vault withdrawal, sends a success/failure message and returns the result.</li>
     * </ul>
     *
     * @param player the player to charge
     * @param amount the amount to withdraw
     * @return {@code true} if economy is disabled, the amount is {@literal <=} 0, or the transaction succeeds; {@code false} otherwise
     */
    public static boolean pay(Player player, double amount) {
        // Respect the toggle
        if (!enabled) return true;

        // Free actions: skip silently
        if (amount <= 0) return true;

        if (economy == null) {
            player.sendMessage("§cAucun système économique détecté.");
            return false;
        }

        net.milkbowl.vault.economy.EconomyResponse response = economy.withdrawPlayer(player, amount);

        String messageKey = response.transactionSuccess() ? "Economy.Payment-successful" : "Economy.Payment-failed";
        String defaultMessage = response.transactionSuccess()
                ? "&aVous avez été débité de %amount% pour cette action."
                : "&cLe paiement a échoué. Veuillez réessayer plus tard.";

        player.sendMessage(HomePlugin.getLanguageManager()
                .getStringWithColor(messageKey, defaultMessage)
                .replace("%amount%", String.valueOf(amount)));

        return response.transactionSuccess();
    }
}