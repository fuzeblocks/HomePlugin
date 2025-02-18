package fr.fuzeblocks.homeplugin;

import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.commands.*;
import fr.fuzeblocks.homeplugin.completers.CacheCompleter;
import fr.fuzeblocks.homeplugin.completers.DeleteHomeCompleter;
import fr.fuzeblocks.homeplugin.completers.HomeCompleter;
import fr.fuzeblocks.homeplugin.database.CreateTable;
import fr.fuzeblocks.homeplugin.database.DatabaseManager;
import fr.fuzeblocks.homeplugin.database.DatabaseConnection;
import fr.fuzeblocks.homeplugin.gui.HomeGUI;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.home.sql.HomeSQLManager;
import fr.fuzeblocks.homeplugin.home.yml.HomeYMLManager;
import fr.fuzeblocks.homeplugin.language.Language;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import fr.fuzeblocks.homeplugin.listeners.OnJoinListener;
import fr.fuzeblocks.homeplugin.listeners.OnMoveListener;
import fr.fuzeblocks.homeplugin.placeholder.HomePluginExpansion;
import fr.fuzeblocks.homeplugin.plugin.PluginManager;
import fr.fuzeblocks.homeplugin.spawn.SpawnManager;
import fr.fuzeblocks.homeplugin.spawn.sql.SpawnSQLManager;
import fr.fuzeblocks.homeplugin.spawn.yml.SpawnYMLManager;
import fr.fuzeblocks.homeplugin.sync.SyncMethod;
import fr.fuzeblocks.homeplugin.update.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public final class HomePlugin extends JavaPlugin {
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

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configurationSection = getConfig();
        checkConfig();
        loadPlugins();
        redisRegistration();
        databaseRegistration();
        homeRegistration();
        commandRegistration();
        eventRegistration();
        completerManager();
        spawnManager();
        cacheManager = CacheManager.getInstance();
        checkDepend();
        loadLanguage();
        checkUpdate(113935);
        getLogger().info("----------------------HomePlugin----------------------");
        getLogger().info("----------HomePlugin a démmaré avec succés !----------");
        getLogger().info("------------------------------------------------------");
        countPlugins();
        initPluginFonc();
    }

    @Override
    public void onDisable() {
        stopPluginFonc();
        getLogger().info("----------------------HomePlugin----------------------");
        getLogger().info("----------HomePlugin a été éteint avec succés !----------");
        getLogger().info("------------------------------------------------------");
    }
    private void checkConfig() {
        String key = "Config.";
        if (Objects.requireNonNull(getConfig().getString(key + "Connector.TYPE")).isEmpty()) {
            getConfig().set(key + "Connector.TYPE", "YAML");
        }
        if (Objects.requireNonNull(getConfig().getString(key + "Language")).isEmpty()) {
            getConfig().set(key + "Language", "FRENCH");
        }
        saveConfig();
        getLogger().info(getConfig().getString(key + "Language") + " has been selected !");
        getLogger().info(getConfig().getString(key + "Connector.TYPE") + " has been selected !");
    }
    private void checkDepend() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new HomePluginExpansion(this).register();
        } else {
            getLogger().warning("PlaceholderAPI is not installed. Placeholders will not be available.");
        }
    }
    private void loadLanguage() {
            String languageString = getConfig().getString("Config.Language");
            Language language = Language.valueOf(languageString.toUpperCase());
            if (language == null) {
                language = Language.FRENCH;
            }
            languageManager = new LanguageManager(language);
    }
    private void redisRegistration() {
        if (getConfig().getBoolean("Config.Redis.UseRedis")) {
            JedisClientConfig jedisClientConfig = DefaultJedisClientConfig.builder()
                    .password(getConfig().getString("ConfigRedis.PASSWORD"))
                    .ssl(getConfig().getBoolean("Config.Redis.SSL")).build();
            HostAndPort hostAndPort = new HostAndPort(getConfig().getString("Config.Redis.HOST"), getConfig().getInt("Config.Redis.PORT"));
            jedisPooled = new JedisPooled(hostAndPort, jedisClientConfig);
        } else {
                getLogger().info("Skipping Redis...");
            }
            if (jedisPooled != null) {
                getLogger().info("Redis registered successfully !");
            } else {
                getLogger().info("Cannot connect to Redis... use default cache!");
            }
        }

        private void databaseRegistration() {
        if (Objects.requireNonNull(getConfig().getString("Config.Connector.TYPE")).equalsIgnoreCase("MYSQL")) {
            getLogger().info("Registering Database");
            new DatabaseManager(this);
            getLogger().info("Registering Manager");
            new CreateTable(DatabaseConnection.getConnection());
            getLogger().info("Registering Table");
            homeSQLManager = new HomeSQLManager();
            spawnSQLManager = new SpawnSQLManager();
            getLogger().info("Registering More");
        }
    }

    private void homeRegistration() {
        getLogger().info("Registering Homes");
        File home = new File(this.getDataFolder(), "homes.yml");
        registration(home);
        homeYMLManager = new HomeYMLManager(home);
        homeManager = HomeManager.getInstance();
    }

    private void spawnManager() {
        getLogger().info("Registering Spawns");
        File spawn = new File(this.getDataFolder(), "spawn.yml");
        registration(spawn);
        spawnYMLManager = new SpawnYMLManager(spawn);
        spawnManager = SpawnManager.getInstance();
    }
    private void registration(File file) {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                getLogger().severe("[HomePlugin] Cannot create file exiting...");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
    }

    private void commandRegistration() {
        getLogger().info("Registering Commands");
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("sethome").setExecutor(new SetHomeCommand());
        getCommand("delhome").setExecutor(new DeleteHomeCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("delspawn").setExecutor(new DeleteSpawnCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("cache").setExecutor(new CacheCommand());
        getCommand("homeadmin").setExecutor(new HomeAdminCommand());
        getCommand("plugins").setExecutor(new PluginCommand());
        getCommand("listhome").setExecutor(new ListHomeCommand());
        getCommand("homegui").setExecutor(new HomeGuiCommand());
    }

    private void eventRegistration() {
        getLogger().info("Registering Events");
        Bukkit.getPluginManager().registerEvents(new OnJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new OnMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new HomeGUI(),this);
    }

    private void completerManager() {
        getLogger().info("Registering Completers");
        getCommand("home").setTabCompleter(new HomeCompleter());
        getCommand("delhome").setTabCompleter(new DeleteHomeCompleter());
        getCommand("cache").setTabCompleter(new CacheCompleter());
    }

    private void checkUpdate(int id) {
        new UpdateChecker(this, id).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                getLogger().info("There is not a new update available.");
            } else {
                getLogger().warning("There is a new update available.");
            }
        });
    }
    private void loadPlugins() {
        if (checkPlugin() != null) {
            getLogger().info(checkPlugin().getName() + "." + "loaded plugin !");
        }
    }
    private void initPluginFonc() {
        if (Objects.nonNull(checkPlugin())) checkPlugin().initialize();
    }
    private void stopPluginFonc() {
       if (Objects.nonNull(checkPlugin())) checkPlugin().stop();
    }
    private fr.fuzeblocks.homeplugin.plugin.HomePlugin checkPlugin() {
        List<fr.fuzeblocks.homeplugin.plugin.HomePlugin> pluginManager = PluginManager.getInstance().getHomePlugin();
        if (!pluginManager.isEmpty()) {
            for (fr.fuzeblocks.homeplugin.plugin.HomePlugin homePlugin : pluginManager) {
                return homePlugin;
            }
        }
        return null;
    }
    private void countPlugins() {
        if (PluginManager.getInstance().getHomePlugin().isEmpty()) {
            getLogger().warning("No plugins to load skipping...");
        }
    }

    public static HomeYMLManager getHomeYMLManager() {
        return homeYMLManager;
    }

    public static SpawnYMLManager getSpawnYMLManager() {
        return spawnYMLManager;
    }

    public static CacheManager getCacheManager() {
        return cacheManager;
    }

    public static HomeSQLManager getHomeSQLManager() {
        return homeSQLManager;
    }

    public static SpawnSQLManager getSpawnSQLManager() {
        return spawnSQLManager;
    }

    public static SyncMethod getRegistrationType() {
        if (configurationSection.getString("Config.Connector.TYPE").equalsIgnoreCase("MYSQL")) {
            return SyncMethod.MYSQL;
        } else {
            return SyncMethod.YAML;
        }
    }

    public static ConfigurationSection getConfigurationSection() {
        return configurationSection;
    }

    public static String translateAlternateColorCodes(String s) {
        return s.replace('&', '§');
    }

    public static JedisPooled getJedisPooled() {
        return jedisPooled;
    }

    public static HomeManager getHomeManager() {
        return homeManager;
    }
    public static SpawnManager getSpawnManager() {
        return spawnManager;
    }

    public static LanguageManager getLanguageManager() {
        return languageManager;
    }
}
