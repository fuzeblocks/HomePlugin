package fr.fuzeblocks.homeplugin;

import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.commands.*;
import fr.fuzeblocks.homeplugin.commands.home.*;
import fr.fuzeblocks.homeplugin.commands.rtp.RTPCommand;
import fr.fuzeblocks.homeplugin.commands.spawn.DeleteSpawnCommand;
import fr.fuzeblocks.homeplugin.commands.spawn.SetSpawnCommand;
import fr.fuzeblocks.homeplugin.commands.spawn.SpawnCommand;
import fr.fuzeblocks.homeplugin.commands.tpa.TPAAcceptCommand;
import fr.fuzeblocks.homeplugin.commands.tpa.TPACommand;
import fr.fuzeblocks.homeplugin.commands.tpa.TPADenyCommand;
import fr.fuzeblocks.homeplugin.completers.*;
import fr.fuzeblocks.homeplugin.database.CreateTable;
import fr.fuzeblocks.homeplugin.database.DatabaseConnection;
import fr.fuzeblocks.homeplugin.database.DatabaseManager;
import fr.fuzeblocks.homeplugin.economy.EconomyManager;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.home.sql.HomeSQLManager;
import fr.fuzeblocks.homeplugin.home.yml.HomeYMLManager;
import fr.fuzeblocks.homeplugin.language.Language;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import fr.fuzeblocks.homeplugin.listeners.BackListener;
import fr.fuzeblocks.homeplugin.listeners.OnJoinListener;
import fr.fuzeblocks.homeplugin.listeners.OnMoveListener;
import fr.fuzeblocks.homeplugin.listeners.OnPlayerTakeDamageByAnotherPlayer;
import fr.fuzeblocks.homeplugin.placeholder.HomePluginExpansion;
import fr.fuzeblocks.homeplugin.plugin.PluginManager;
import fr.fuzeblocks.homeplugin.spawn.SpawnManager;
import fr.fuzeblocks.homeplugin.spawn.sql.SpawnSQLManager;
import fr.fuzeblocks.homeplugin.spawn.yml.SpawnYMLManager;
import fr.fuzeblocks.homeplugin.sync.SyncMethod;
import fr.fuzeblocks.homeplugin.update.UpdateChecker;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Main plugin class for HomePlugin.
 */
public final class HomePlugin extends JavaPlugin {

    // Config keys
    private static final String CFG_ROOT = "Config.";
    private static final String CFG_CONNECTOR = CFG_ROOT + "Connector.TYPE";
    private static final String CFG_LANGUAGE = CFG_ROOT + "Language";
    private static final String CFG_REDIS_USE = CFG_ROOT + "Redis.UseRedis";
    private static final String CFG_REDIS_HOST = CFG_ROOT + "Redis.HOST";
    private static final String CFG_REDIS_PORT = CFG_ROOT + "Redis.PORT";
    private static final String CFG_REDIS_SSL = CFG_ROOT + "Redis.SSL";
    private static final String CFG_REDIS_PASSWORD = CFG_ROOT + "Redis.PASSWORD";
    private static final String CFG_RTP_ENABLED = CFG_ROOT + "Rtp.enabled";

    // Managers and services
    private static HomeYMLManager homeYMLManager;
    private static SpawnYMLManager spawnYMLManager;
    private static CacheManager cacheManager;
    private static HomeSQLManager homeSQLManager;
    private static SpawnSQLManager spawnSQLManager;
    private static ConfigurationSection configurationSection;
    private static JedisPooled jedisPooled;
    private static HomeManager homeManager;
    private static SpawnManager spawnManager;
    private static LanguageManager languageManager;
    private static BukkitAudiences adventure;
    private static Economy economy;
    private static Metrics metrics;

    @Override
    public void onEnable() {
        // Config and metrics
        saveDefaultConfig();
        applyDefaultConfigValues();
        configurationSection = getConfig();
        adventure = BukkitAudiences.create(this);
        setupMetrics();

        // Dependencies and services
        setupEconomy();
        checkDepend();
        loadLanguage();
        redisRegistration();
        databaseRegistration();

        // Domain managers
        homeRegistration();
        spawnRegistration();
        cacheManager = CacheManager.getInstance();

        // Commands, events, completers
        commandRegistration();
        eventRegistration();
        completerRegistration();

        // Optional plugin extensions
        countPlugins();
        initPluginFunc();

        // Update check
        checkUpdate(113935);

        getLogger().info("------------------------------------------------------");
        getLogger().info("HomePlugin started successfully!");
        getLogger().info("Language: " + getConfig().getString(CFG_LANGUAGE));
        getLogger().info("Storage: " + getConfig().getString(CFG_CONNECTOR));
        getLogger().info("------------------------------------------------------");
    }

    @Override
    public void onDisable() {
        // Close Adventure
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }

        // Close Redis
        if (jedisPooled != null) {
            try {
                jedisPooled.close();
            } catch (Exception ignored) {
            } finally {
                jedisPooled = null;
            }
        }

        stopPluginFunc();

        getLogger().info("------------------------------------------------------");
        getLogger().info("HomePlugin shut down successfully!");
        getLogger().info("------------------------------------------------------");
    }

    // -------------------- Setup and registration --------------------

    private void applyDefaultConfigValues() {
        getConfig().addDefault(CFG_CONNECTOR, "YAML");
        getConfig().addDefault(CFG_LANGUAGE, "FRENCH");

        getConfig().addDefault(CFG_REDIS_USE, false);
        getConfig().addDefault(CFG_REDIS_HOST, "127.0.0.1");
        getConfig().addDefault(CFG_REDIS_PORT, 6379);
        getConfig().addDefault(CFG_REDIS_SSL, false);
        getConfig().addDefault(CFG_REDIS_PASSWORD, "");

        getConfig().addDefault(CFG_RTP_ENABLED, false);

        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void checkDepend() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new HomePluginExpansion(this).register();
            getLogger().info("PlaceholderAPI detected. Placeholders enabled.");
        } else {
            getLogger().warning("PlaceholderAPI not installed. Placeholders disabled.");
        }
    }

    private void loadLanguage() {
        String languageString = getConfig().getString(CFG_LANGUAGE, "FRENCH");
        Language language;
        try {
            language = Language.valueOf(languageString.toUpperCase());
        } catch (Exception e) {
            getLogger().warning("Invalid language '" + languageString + "'. Falling back to FRENCH.");
            language = Language.FRENCH;
        }
        languageManager = new LanguageManager(language, this);
    }

    private void redisRegistration() {
        if (!getConfig().getBoolean(CFG_REDIS_USE, false)) {
            getLogger().info("Redis disabled in config. Skipping Redis setup.");
            return;
        }

        try {
            JedisClientConfig jedisClientConfig = DefaultJedisClientConfig.builder()
                    .password(blankToNull(getConfig().getString(CFG_REDIS_PASSWORD)))
                    .ssl(getConfig().getBoolean(CFG_REDIS_SSL, false))
                    .build();

            HostAndPort hostAndPort = new HostAndPort(
                    getConfig().getString(CFG_REDIS_HOST, "127.0.0.1"),
                    getConfig().getInt(CFG_REDIS_PORT, 6379)
            );

            jedisPooled = new JedisPooled(hostAndPort, jedisClientConfig);
            // Basic connectivity test
            jedisPooled.ping();
            getLogger().info("Redis registered successfully!");
        } catch (Exception e) {
            getLogger().warning("Cannot connect to Redis (" + e.getClass().getSimpleName() + ": " + e.getMessage() + "). Falling back to default cache.");
            jedisPooled = null;
        }
    }

    private void databaseRegistration() {
        String connector = getConfig().getString(CFG_CONNECTOR, "YAML");
        if ("MYSQL".equalsIgnoreCase(connector)) {
            getLogger().info("Setting up MySQL storage...");
            try {
                new DatabaseManager(this);
                new CreateTable(DatabaseConnection.getConnection());
                homeSQLManager = new HomeSQLManager();
                spawnSQLManager = new SpawnSQLManager();
                getLogger().info("MySQL storage initialized.");
            } catch (Exception e) {
                getLogger().severe("Failed to initialize MySQL storage: " + e.getMessage());
            }
        } else {
            getLogger().info("Using YAML storage.");
        }
    }

    private void homeRegistration() {
        getLogger().info("Registering Homes...");
        File home = new File(getDataFolder(), "homes.yml");
        ensureFile(home);
        homeYMLManager = new HomeYMLManager(home);
        homeManager = HomeManager.getInstance();
    }

    private void spawnRegistration() {
        getLogger().info("Registering Spawns...");
        File spawn = new File(getDataFolder(), "spawn.yml");
        ensureFile(spawn);
        spawnYMLManager = new SpawnYMLManager(spawn);
        spawnManager = SpawnManager.getInstance();
    }

    private void ensureFile(File file) {
        if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
            getLogger().severe("[HomePlugin] Cannot create plugin data folder. Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new IOException("createNewFile returned false");
                }
            } catch (IOException e) {
                getLogger().severe("[HomePlugin] Cannot create file " + file.getName() + " (" + e.getMessage() + "). Disabling plugin...");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
    }

    private void commandRegistration() {
        getLogger().info("Registering commands...");

        bind("home", new HomeCommand(), new HomeCompleter());
        bind("sethome", new SetHomeCommand(), new SetHomeCompleter());
        bind("delhome", new DeleteHomeCommand(), new DeleteHomeCompleter());
        bind("renamehome", new RenameHomeCommand(), new RenameHomeCompleter());
        bind("relocatehome", new RelocateHomeCommand(), new RelocateHomeCompleter());
        bind("listhome", new ListHomeCommand(), null);

        bind("setspawn", new SetSpawnCommand(), null);
        bind("delspawn", new DeleteSpawnCommand(), null);
        bind("spawn", new SpawnCommand(), null);

        bind("cache", new CacheCommand(), new CacheCompleter());
        bind("homeadmin", new HomeAdminCommand(), new HomeAdminCompleter());
        bind("plugins", new fr.fuzeblocks.homeplugin.commands.PluginCommand(), null);
        bind("lang", new LangCommand(this), new LangTabCompleter(this));

        bind("tpa", new TPACommand(), new TpaCompleter());
        bind("tpaccept", new TPAAcceptCommand(), new TpAcceptCompleter());
        bind("tpdeny", new TPADenyCommand(), new TpDenyCompleter());
        bind("back", new BackCommand(), null);

        if (getConfig().getBoolean(CFG_RTP_ENABLED, false)) {
            bind("rtp", new RTPCommand(), null);
        }
    }

    private void eventRegistration() {
        getLogger().info("Registering events...");
        Bukkit.getPluginManager().registerEvents(new OnJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new OnMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerTakeDamageByAnotherPlayer(), this);
        Bukkit.getPluginManager().registerEvents(new BackListener(), this);
    }

    private void completerRegistration() {
        // Completers are attached in bind(...) above to avoid duplicate getCommand calls.
        // Method kept for symmetry and future additions.
    }

    private void checkUpdate(int id) {
        new UpdateChecker(this, id).getVersion(version -> {
            try {
                String local = safeDigits(this.getDescription().getVersion());
                String remote = safeDigits(version);

                int localVersion = Integer.parseInt(local.replace(".", ""));
                int remoteVersion = Integer.parseInt(remote.replace(".", ""));

                if (remoteVersion > localVersion) {
                    getLogger().warning("A new update is available. Current: " + local + " | Latest: " + remote);
                }
            } catch (Exception e) {
                getLogger().fine("Could not compare versions: " + e.getMessage());
            }
        });
    }

    private void initPluginFunc() {
        fr.fuzeblocks.homeplugin.plugin.HomePlugin plug = getFirstHomePlugin();
        if (plug != null) {
            try {
                plug.initialize();
                getLogger().info(plug.getName() + " plugin initialized.");
            } catch (Exception e) {
                getLogger().warning("Failed to initialize plugin extension: " + e.getMessage());
            }
        }
    }

    private void stopPluginFunc() {
        fr.fuzeblocks.homeplugin.plugin.HomePlugin plug = getFirstHomePlugin();
        if (plug != null) {
            try {
                plug.stop();
            } catch (Exception e) {
                getLogger().warning("Failed to stop plugin extension: " + e.getMessage());
            }
        }
    }

    private fr.fuzeblocks.homeplugin.plugin.HomePlugin getFirstHomePlugin() {
        List<fr.fuzeblocks.homeplugin.plugin.HomePlugin> plugins = PluginManager.getInstance().getHomePlugin();
        return (plugins == null || plugins.isEmpty()) ? null : plugins.get(0);
    }

    private void countPlugins() {
        int count = PluginManager.getInstance().getHomePlugin().size();
        if (count == 0) {
            getLogger().warning("No plugin extensions detected.");
        } else {
            getLogger().info("Loaded " + count + " plugin extension(s).");
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().info("Vault not found. Economy features disabled.");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().info("No Economy provider found. Economy features disabled.");
            return false;
        }
        economy = rsp.getProvider();
        EconomyManager.setup(this);
        return economy != null;
    }

    private void setupMetrics() {
        metrics = new Metrics(this, 27702);
    }

    // -------------------- Helpers --------------------

    private void bind(String name, CommandExecutor exec, TabCompleter tab) {
        PluginCommand c = getCommand(name);
        if (c == null) {
            getLogger().warning("Command '" + name + "' not found in plugin.yml. Skipping registration.");
            return;
        }
        if (exec != null) c.setExecutor(exec);
        if (tab != null) c.setTabCompleter(tab);
    }

    private static String safeDigits(String ver) {
        return ver == null ? "0" : ver.replaceAll("[^0-9.]", "");
    }

    private static String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    // -------------------- Static getters --------------------

    /**
     * Gets home yml manager.
     *
     * @return the home yml manager
     */
    public static HomeYMLManager getHomeYMLManager() {
        return homeYMLManager;
    }

    /**
     * Gets spawn yml manager.
     *
     * @return the spawn yml manager
     */
    public static SpawnYMLManager getSpawnYMLManager() {
        return spawnYMLManager;
    }

    /**
     * Gets cache manager.
     *
     * @return the cache manager
     */
    public static CacheManager getCacheManager() {
        return cacheManager;
    }

    /**
     * Gets home sql manager.
     *
     * @return the home sql manager
     */
    public static HomeSQLManager getHomeSQLManager() {
        return homeSQLManager;
    }

    /**
     * Gets spawn sql manager.
     *
     * @return the spawn sql manager
     */
    public static SpawnSQLManager getSpawnSQLManager() {
        return spawnSQLManager;
    }

    /**
     * Gets registration type.
     *
     * @return the registration type
     */
    public static SyncMethod getRegistrationType() {
        String type = configurationSection != null ? configurationSection.getString(CFG_CONNECTOR, "YAML") : "YAML";
        return "MYSQL".equalsIgnoreCase(type) ? SyncMethod.MYSQL : SyncMethod.YAML;
    }

    /**
     * Gets configuration section.
     *
     * @return the configuration section
     */
    public static ConfigurationSection getConfigurationSection() {
        return configurationSection;
    }

    /**
     * Gets jedis pooled.
     *
     * @return the jedis pooled
     */
    public static JedisPooled getJedisPooled() {
        return jedisPooled;
    }

    /**
     * Gets home manager.
     *
     * @return the home manager
     */
    public static HomeManager getHomeManager() {
        return homeManager;
    }

    /**
     * Gets spawn manager.
     *
     * @return the spawn manager
     */
    public static SpawnManager getSpawnManager() {
        return spawnManager;
    }

    /**
     * Gets language manager.
     *
     * @return the language manager
     */
    public static LanguageManager getLanguageManager() {
        return languageManager;
    }

    /**
     * Gets economy.
     *
     * @return the economy
     */
    public static Economy getEconomy() {
        return economy;
    }

    /**
     * Gets adventure.
     *
     * @return the adventure
     */
    @NonNull
    public static BukkitAudiences getAdventure() {
        if (adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return adventure;
    }

    /**
     * Gets metrics.
     *
     * @return the metrics
     */
    public static Metrics getMetrics() {
        return metrics;
    }
}