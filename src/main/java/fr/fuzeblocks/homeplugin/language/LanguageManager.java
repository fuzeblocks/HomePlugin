package fr.fuzeblocks.homeplugin.language;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * The type Language manager.
 */
public class LanguageManager {

    private final Language language;
    private YamlConfiguration yamlConfiguration;
    private final File file;
    private final HomePlugin plugin;

    /**
     * Instantiates a new Language manager.
     *
     * @param type   the type
     * @param plugin the plugin
     */
    public LanguageManager(Language type, HomePlugin plugin) {
        this.language = type;
        this.plugin = plugin;

        String fileName = type.toString().toLowerCase() + ".yml";
        this.file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }

        this.yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Gets string.
     *
     * @param key the key
     * @return the string
     */
    @Nullable
    public String getString(String key) {
        return yamlConfiguration.getString(key);
    }

    /**
     * Gets string.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the string
     */
    @Nullable
    public String getString(String key, String defaultValue) {
        return yamlConfiguration.getString(key, defaultValue);
    }

    /**
     * Gets string with color.
     *
     * @param key the key
     * @return the string with color
     */
    @Nullable
    public String getStringWithColor(String key) {
        return translateAlternateColorCodes(yamlConfiguration.getString(key));
    }

    /**
     * Gets string with color.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the string with color
     */
    @Nullable
    public String getStringWithColor(String key, String defaultValue) {
        return translateAlternateColorCodes(yamlConfiguration.getString(key, defaultValue));
    }

    /**
     * Translate alternate color codes string.
     *
     * @param s the s
     * @return the string
     */
    public static @NotNull String translateAlternateColorCodes(@Nullable String s) {
        if (s == null) {
            return "§c[Traduction manquante]";
        }
        return s.replace('&', '§');
    }

    /**
     * Regenerates the language file from the internal resource and reloads it.
     * Warning: this will overwrite the current file.
     *
     * @return true if the operation succeeded, false otherwise.
     */
    public boolean regenerate() {
        try {
            if (file.exists()) {
                if (!file.delete()) {
                    return false;
                }
            }
            plugin.saveResource(file.getName(), true);
            this.yamlConfiguration = YamlConfiguration.loadConfiguration(file);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets language.
     *
     * @return the language
     */
    public Language getLanguage() {
        return language;
    }

}
