package fr.fuzeblocks.homeplugin;

import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.commands.*;
import fr.fuzeblocks.homeplugin.completer.DelHomeCompleter;
import fr.fuzeblocks.homeplugin.completer.HomeCompleter;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.listener.OnJoinListener;
import fr.fuzeblocks.homeplugin.listener.OnMoveListener;
import fr.fuzeblocks.homeplugin.spawn.SpawnManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class HomePlugin extends JavaPlugin {
    private static HomeManager homeManager;
    private static SpawnManager spawnManager;
    private static CacheManager cacheManager;

    @Override
    public void onEnable() {
        System.out.println("----------------------HomePlugin----------------------");
        System.out.println("----------HomePlugin a démmaré avec succés !----------");
        System.out.println("------------------------------------------------------");
        homeRegistration();
        commandRegistration();
        eventRegistration();
        completerManager();
        spawnManager();
        cacheManager = new CacheManager();
    }

        @Override
    public void onDisable() {
            System.out.println("----------------------HomePlugin----------------------");
            System.out.println("----------HomePlugin a été éteint  avec succés !----------");
            System.out.println("------------------------------------------------------");
    }
    private void homeRegistration() {
        File home = new File(this.getDataFolder(), "homes.yml");
        registration(home);
        homeManager = new HomeManager(home);
    }
    private void spawnManager() {
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
                getLogger().severe("Cannot create file exiting...");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
    }

    private void commandRegistration() {
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("sethome").setExecutor(new SetHomeCommand());
        getCommand("delhome").setExecutor(new DelHomeCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("delspawn").setExecutor(new DelSpawnCommand());
        getCommand("spawn").setExecutor(new SpawnCommand(this));
    }
    private void eventRegistration() {
        Bukkit.getPluginManager().registerEvents(new OnJoinListener(),this);
        Bukkit.getPluginManager().registerEvents(new OnMoveListener(),this);
    }
    private void completerManager() {
        getCommand("home").setTabCompleter(new HomeCompleter());
        getCommand("delhome").setTabCompleter(new DelHomeCompleter());
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
}
