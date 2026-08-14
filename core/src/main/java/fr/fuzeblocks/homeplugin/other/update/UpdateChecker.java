package fr.fuzeblocks.homeplugin.other.update;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * The type Update checker.
 */
public class UpdateChecker {

    private static boolean shouldAskForUpdateLangFiles = false;
    private static boolean shouldAskForUpdatePlugin = false;
    private static boolean markForUpdatePlugin = false;
    private final JavaPlugin plugin;
    private final String INITIAL_VERSION_KEY = "Config.Initial-Plugin-Version";


    /**
     * Instantiates a new Update checker.
     *
     * @param plugin     the plugin
     */
    public UpdateChecker(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static boolean shoudAskForUpdatePlugin() {
        return shouldAskForUpdatePlugin;
    }

    public static boolean shouldAskForUpdateLangFiles() {
        return shouldAskForUpdateLangFiles;
    }

    public static boolean isMarkForUpdatePlugin() {
        return markForUpdatePlugin;
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

    /**
     * Gets version.
     *
     * @param consumer the consumer
     */
     public void getVersion(final Consumer<String> consumer) {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            HttpURLConnection connection = null;

            try {
                URL url = new URL(
                        "https://api.github.com/repos/fuzeblocks/HomePlugin/tags"
                );

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty(
                        "Accept",
                        "application/vnd.github+json"
                );
                connection.setRequestProperty(
                        "User-Agent",
                        "HomePlugin"
                );
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();

                if (responseCode != HttpURLConnection.HTTP_OK) {
                    plugin.getLogger().warning(
                            "GitHub API returned HTTP " + responseCode
                    );
                    return;
                }

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                connection.getInputStream(),
                                StandardCharsets.UTF_8
                        ))) {

                    String response = reader.lines()
                            .collect(Collectors.joining());

                    int nameIndex = response.indexOf("\"name\":\"");

                    if (nameIndex == -1) {
                        plugin.getLogger().warning(
                                "Could not find version in GitHub response."
                        );
                        return;
                    }

                    int start = nameIndex + "\"name\":\"".length();
                    int end = response.indexOf("\"", start);

                    if (end == -1) {
                        plugin.getLogger().warning(
                                "Could not parse version from GitHub response."
                        );
                        return;
                    }

                    String remoteVersion = response.substring(start, end);

                    plugin.getLogger().info(
                            "Latest GitHub version: " + remoteVersion
                    );

                    consumer.accept(remoteVersion);
                }

            } catch (IOException e) {
                plugin.getLogger().warning(
                        "Could not check for updates: " + e.getMessage()
                );
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    public String getInitialVersion() {
        return plugin.getConfig().getString(INITIAL_VERSION_KEY);
    }

    public void setInitialVersion(String version) {
        if (!isDefaultVersion()) return;
        plugin.getConfig().set(INITIAL_VERSION_KEY, version);
        plugin.saveConfig();
    }

    public boolean isInitialVersionOutdated(String latestVersion) {
        String initialVersion = getInitialVersion();
        if (isDefaultVersion()) return false;
        return !Objects.equals(initialVersion, latestVersion);
    }

    private boolean isDefaultVersion() {
        return getInitialVersion().equals("not-yet-defined");
    }

    public void setShouldAskForUpdateLangFiles(boolean shouldAskForUpdateLangFiles) {
        UpdateChecker.shouldAskForUpdateLangFiles = shouldAskForUpdateLangFiles;
    }

    public void setShouldAskForUpdatePlugin(boolean shoudAskForUpdatePlugin) {
        UpdateChecker.shouldAskForUpdatePlugin = shoudAskForUpdatePlugin;
    }
}