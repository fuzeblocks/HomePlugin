package fr.fuzeblocks.homeplugin.update;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.jar.JarFile;

/**
 * The type Update checker.
 */
public class UpdateChecker {

    private final JavaPlugin plugin;
    private final int resourceId;
    private final String INITIAL_VERSION_KEY = "Config.Initial-Plugin-Version";
    private static boolean shouldAskForUpdateLangFiles = false;
    private static boolean shoudAskForUpdatePlugin = false;
    private static boolean markForUpdatePlugin = false;


    /**
     * Instantiates a new Update checker.
     *
     * @param plugin     the plugin
     * @param resourceId the resource id
     */
    public UpdateChecker(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    /**
     * Gets version.
     *
     * @param consumer the consumer
     */
    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream is = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId + "/~").openStream(); Scanner scann = new Scanner(is)) {
                if (scann.hasNext()) {
                    consumer.accept(scann.next());
                }
            } catch (IOException e) {
                plugin.getLogger().info("Unable to check for updates: " + e.getMessage());
            }
        });
    }
    public void setInitialVersion(String version) {
        if (!isDefaultVersion()) return;
        plugin.getConfig().set(INITIAL_VERSION_KEY, version);
        plugin.saveConfig();
    }
    public String getInitialVersion() {
        return plugin.getConfig().getString(INITIAL_VERSION_KEY);
    }

    public boolean isInitialVersionOutdated(String latestVersion) {
        String initialVersion = getInitialVersion();
        if (isDefaultVersion()) return false;
        return !Objects.equals(initialVersion, latestVersion);
    }

    private boolean isDefaultVersion() {
        return getInitialVersion().equals("not-yet-defined");
    }

    public static boolean shoudAskForUpdatePlugin() {
        return shoudAskForUpdatePlugin;
    }

    public static boolean shouldAskForUpdateLangFiles() {
        return shouldAskForUpdateLangFiles;
    }
    public static boolean isMarkForUpdatePlugin() {
            return markForUpdatePlugin;
        }

    public void setShouldAskForUpdateLangFiles(boolean shouldAskForUpdateLangFiles) {
        UpdateChecker.shouldAskForUpdateLangFiles = shouldAskForUpdateLangFiles;
    }

    public void setShoudAskForUpdatePlugin(boolean shoudAskForUpdatePlugin) {
        UpdateChecker.shoudAskForUpdatePlugin = shoudAskForUpdatePlugin;
    }
    public void setMarkForUpdatePlugin(boolean markForUpdatePlugin) {
            UpdateChecker.markForUpdatePlugin = markForUpdatePlugin;

    }

    public static String getVersionFromJar(Path jarPath) {
        try (JarFile jar = new JarFile(jarPath.toFile())) {
            InputStream is = jar.getInputStream(jar.getEntry("plugin.yml"));
            if (is != null) {
                YamlConfiguration yml = YamlConfiguration.loadConfiguration(new java.io.InputStreamReader(is));
                return yml.getString("version");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown";
    }
}