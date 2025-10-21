package fr.fuzeblocks.homeplugin.language;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * The type Language merge.
 */
public class LanguageMerge {

    private final YamlConfiguration editedConfig;
    private final YamlConfiguration latestConfig;
    private final YamlConfiguration mergedConfig;

    private final File mergedFile;

    /**
     * Instantiates a new Language merge.
     *
     * @param editedFile the edited file
     * @param latestFile the latest file
     */
    public LanguageMerge(File editedFile, File latestFile) {
        this.editedConfig = YamlConfiguration.loadConfiguration(editedFile);
        this.latestConfig = YamlConfiguration.loadConfiguration(latestFile);
        this.mergedConfig = new YamlConfiguration();
        this.mergedFile = editedFile;
    }

    /**
     * Simple merge: copies the edited config,
     * adds missing keys from the latest version,
     * without removing any key.
     */
    public void mergeAddOnly() {
        for (String key : editedConfig.getKeys(true)) {
            mergedConfig.set(key, editedConfig.get(key));
        }
        for (String key : latestConfig.getKeys(true)) {
            if (!mergedConfig.contains(key)) {
                mergedConfig.set(key, latestConfig.get(key));
            }
        }
    }

    /**
     * Synchronized merge: copies the edited config,
     * adds missing keys,
     * removes keys no longer present in the latest version.
     */
    public void mergeSync() {

        for (String key : editedConfig.getKeys(true)) {
            mergedConfig.set(key, editedConfig.get(key));
        }

        for (String key : mergedConfig.getKeys(true)) {
            if (!latestConfig.contains(key)) {
                mergedConfig.set(key, null);
            }
        }

        for (String key : latestConfig.getKeys(true)) {
            if (!mergedConfig.contains(key)) {
                mergedConfig.set(key, latestConfig.get(key));
            }
        }
    }

    /**
     * Saves the merged config to the edited file.
     *
     * @return true if saving succeeded, false otherwise.
     */
    public boolean pushChanges() {
        try {
            mergedConfig.save(mergedFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Push to boolean.
     *
     * @param destinationFile the destination file
     * @return the boolean
     */
    public boolean pushTo(File destinationFile) {
        if (mergedConfig == null) return false;

        try {
            mergedConfig.save(destinationFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
