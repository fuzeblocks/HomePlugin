package fr.fuzeblocks.homeplugin;

import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.commands.*;
import fr.fuzeblocks.homeplugin.completers.CacheCompleter;
import fr.fuzeblocks.homeplugin.completers.DeleteHomeCompleter;
import fr.fuzeblocks.homeplugin.completers.HomeCompleter;
import fr.fuzeblocks.homeplugin.database.CreateTable;
import fr.fuzeblocks.homeplugin.database.DatabaseManager;
import fr.fuzeblocks.homeplugin.database.DatabaseConnection;
import fr.fuzeblocks.homeplugin.home.yml.HomeManager;
import fr.fuzeblocks.homeplugin.listeners.OnJoinListener;
import fr.fuzeblocks.homeplugin.listeners.OnMoveListener;
import fr.fuzeblocks.homeplugin.placeholder.HomePluginExpansion;
import fr.fuzeblocks.homeplugin.plugin.PluginLoader;
import fr.fuzeblocks.homeplugin.plugin.PluginManager;
import fr.fuzeblocks.homeplugin.spawn.yml.SpawnManager;
import fr.fuzeblocks.homeplugin.sync.type.SyncMethod;
import fr.fuzeblocks.homeplugin.update.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class HomePlugin extends JavaPlugin {
    private static HomeManager homeManager;
    private static SpawnManager spawnManager;
    private static CacheManager cacheManager;
    private static fr.fuzeblocks.homeplugin.home.sql.HomeManager homeSQLManager;
    private static fr.fuzeblocks.homeplugin.spawn.sql.SpawnManager spawnSQLManager;
    private static ConfigurationSection configurationSection;

    @Override
    public void onEnable() {
        configurationSection = getConfig();
        if (getConfig().getString("Config.Connector.TYPE").isEmpty()) {
            getConfig().set("Config.Connector.TYPE", "YAML");
            saveConfig();
        }
        System.out.println(getConfig().getString("Config.Connector.TYPE") + " has been selected !");
        loadPlugins();
        saveDefaultConfig();
        databaseRegistration();
        homeRegistration();
        commandRegistration();
        eventRegistration();
        completerManager();
        spawnManager();
        pluginsManager();
        cacheManager = CacheManager.getInstance();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new HomePluginExpansion(this).register();
        } else {
            getLogger().warning("PlaceholderAPI is not installed. Placeholders will not be available.");
        }
        checkUpdate(113935);
        getLogger().info("----------------------HomePlugin----------------------");
        getLogger().info("----------HomePlugin a démmaré avec succés !----------");
        getLogger().info("------------------------------------------------------");
        initPluginFonc();
    }

    @Override
    public void onDisable() {
        getLogger().info("----------------------HomePlugin----------------------");
        getLogger().info("----------HomePlugin a été éteint avec succés !----------");
        getLogger().info("------------------------------------------------------");
        stopPluginFonc();
    }

    private void databaseRegistration() {
        if (getConfig().getString("Config.Connector.TYPE").equalsIgnoreCase("MYSQL")) {
            getLogger().info("Registering Database");
            new DatabaseManager(this);
            getLogger().info("Registering Manager");
            new CreateTable(DatabaseConnection.getConnection());
            getLogger().info("Registering Table");
            homeSQLManager = new fr.fuzeblocks.homeplugin.home.sql.HomeManager();
            spawnSQLManager = new fr.fuzeblocks.homeplugin.spawn.sql.SpawnManager();
            getLogger().info("Registering More");
        }
    }

    private void homeRegistration() {
        getLogger().info("Registering Homes");
        File home = new File(this.getDataFolder(), "homes.yml");
        registration(home);
        homeManager = new HomeManager(home);
    }

    private void spawnManager() {
        getLogger().info("Registering Spawns");
        File spawn = new File(this.getDataFolder(), "spawn.yml");
        registration(spawn);
        spawnManager = new SpawnManager(spawn);
    }
    private void pluginsManager() {
        getLogger().info("Registering Plugins");
        File plugins = new File(this.getDataFolder(),"plugins");
        plugins.mkdirs();
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
    }

    private void eventRegistration() {
        getLogger().info("Registering Events");
        Bukkit.getPluginManager().registerEvents(new OnJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new OnMoveListener(), this);
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
                getLogger().info("There is a new update available.");
            }
        });
    }
    private void loadPlugins() {
            getLogger().info(checkPlugin().getName() + "." + "loaded plugin !");
    }
    private void initPluginFonc() {
        checkPlugin().initialize();
    }
    private void stopPluginFonc() {
        checkPlugin().stop();
    }
    private fr.fuzeblocks.homeplugin.plugin.HomePlugin checkPlugin() {
        for (fr.fuzeblocks.homeplugin.plugin.HomePlugin homePlugin : PluginManager.getInstance().getHomePlugin()) {
            assert homePlugin != null;
            return homePlugin;
        }
        return null;
    }
    public static HomeManager getHomeManager() {
        return homeManager;
    }

    public static SpawnManager getSpawnManager() {
        return spawnManager;
    }

    public static CacheManager getCacheManager() {
        return cacheManager;
    }

    public static fr.fuzeblocks.homeplugin.home.sql.HomeManager getHomeSQLManager() {
        return homeSQLManager;
    }

    public static fr.fuzeblocks.homeplugin.spawn.sql.SpawnManager getSpawnSQLManager() {
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

}
