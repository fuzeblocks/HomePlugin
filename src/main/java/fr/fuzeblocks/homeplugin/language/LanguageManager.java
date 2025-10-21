package fr.fuzeblocks.homeplugin.language;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class LanguageManager {

    private final Language language;
    private YamlConfiguration yamlConfiguration;
    private final File file;
    private final HomePlugin plugin;

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

    @Nullable
    public String getString(String key) {
        return yamlConfiguration.getString(key);
    }

    @Nullable
    public String getString(String key, String defaultValue) {
        return yamlConfiguration.getString(key, defaultValue);
    }

    @Nullable
    public String getStringWithColor(String key) {
        return translateAlternateColorCodes(yamlConfiguration.getString(key));
    }

    @Nullable
    public String getStringWithColor(String key, String defaultValue) {
        return translateAlternateColorCodes(yamlConfiguration.getString(key, defaultValue));
    }

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

    public Language getLanguage() {
        return language;
    }

}
