package fr.fuzeblocks.homeplugin;

import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.commands.*;
import fr.fuzeblocks.homeplugin.completers.CacheCompleter;
import fr.fuzeblocks.homeplugin.completers.DelHomeCompleter;
import fr.fuzeblocks.homeplugin.completers.HomeCompleter;
import fr.fuzeblocks.homeplugin.database.CreateTable;
import fr.fuzeblocks.homeplugin.database.DatabaseManager;
import fr.fuzeblocks.homeplugin.database.DbConnection;
import fr.fuzeblocks.homeplugin.home.yml.HomeManager;
import fr.fuzeblocks.homeplugin.listeners.OnJoinListener;
import fr.fuzeblocks.homeplugin.listeners.OnMoveListener;
import fr.fuzeblocks.homeplugin.placeholder.HomePluginExpansion;
import fr.fuzeblocks.homeplugin.spawn.yml.SpawnManager;
import fr.fuzeblocks.homeplugin.update.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

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
            getConfig().set("Config.Connector.TYPE","YAML");
            saveConfig();
        }
        System.out.println(getConfig().getString("Config.Connector.TYPE") + " has been selected !");
        saveDefaultConfig();
        databaseRegistration();
        homeRegistration();
        commandRegistration();
        eventRegistration();
        completerManager();
        spawnManager();
        cacheManager = CacheManager.getInstance();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new HomePluginExpansion(this).register();
        } else {
            getLogger().warning("PlaceholderAPI is not installed. Placeholders will not be available.");
        }
        new UpdateChecker(this,113935);
        getLogger().info("----------------------HomePlugin----------------------");
        getLogger().info("----------HomePlugin a démmaré avec succés !----------");
        getLogger().info("------------------------------------------------------");
    }


        @Override
    public void onDisable() {
            getLogger().info("----------------------HomePlugin----------------------");
            getLogger().info("----------HomePlugin a été éteint avec succés !----------");
            getLogger().info("------------------------------------------------------");
    }
    private void databaseRegistration() {
        if (getConfig().getString("Config.Connector.TYPE").equalsIgnoreCase("MYSQL")) {
            getLogger().info("Registering Database");
            new DatabaseManager(this);
            getLogger().info("Registering Manager");
            new CreateTable(DbConnection.getConnection());
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
        getCommand("delhome").setExecutor(new DelHomeCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("delspawn").setExecutor(new DelSpawnCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("cache").setExecutor(new CacheCommand());
        getCommand("homeadmin").setExecutor(new HomeAdminCommand());
    }
    private void eventRegistration() {
        getLogger().info("Registering Events");
        Bukkit.getPluginManager().registerEvents(new OnJoinListener(),this);
        Bukkit.getPluginManager().registerEvents(new OnMoveListener(),this);
    }
    private void completerManager() {
        getLogger().info("Registering Completers");
        getCommand("home").setTabCompleter(new HomeCompleter());
        getCommand("delhome").setTabCompleter(new DelHomeCompleter());
        getCommand("cache").setTabCompleter(new CacheCompleter());
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
    public static int getRegistrationType() {
        if (configurationSection.getString("Config.Connector.TYPE").equalsIgnoreCase("MYSQL")) {
            return 1;
        } else {
            return 0;
        }
    }

    public static ConfigurationSection getConfigurationSection() {
        return configurationSection;
    }
    public static String translateAlternateColorCodes(String s) {
        return s.replace('&','§');
    }
}
