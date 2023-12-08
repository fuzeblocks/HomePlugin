package fr.fuzeblocks.homeplugin;

import fr.fuzeblocks.homeplugin.commands.*;
import fr.fuzeblocks.homeplugin.completer.HomeCompleter;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.listener.OnJoinListener;
import fr.fuzeblocks.homeplugin.spawn.SpawnManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class HomePlugin extends JavaPlugin {
   public static LuckPerms api;
    public static HomeManager homeManager;
    public static SpawnManager spawnManager;

    @Override
    public void onEnable() {
        System.out.println("----------------------HomePlugin----------------------");
        System.out.println("----------HomePlugin a démmaré avec succés !----------");
        System.out.println("------------------------------------------------------");
        luckPermRegistration();
        homeRegistration();
        spawnManager();
        commandRegistration();
        eventRegistration();
    }

        @Override
    public void onDisable() {
            System.out.println("----------------------HomePlugin----------------------");
            System.out.println("----------HomePlugin a été éteint  avec succés !----------");
            System.out.println("------------------------------------------------------");
    }
    private void luckPermRegistration() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
            getLogger().info("LuckPerms API chargée avec succès.");
        } else {
            getLogger().severe("Impossible de charger LuckPerms API. Le plugin peut ne pas fonctionner correctement.");
            getServer().getPluginManager().disablePlugin(this);
        }
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
                throw new RuntimeException(e);
            }
        }
    }

    private void commandRegistration() {
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("sethome").setExecutor(new SetHomeCommand());
        getCommand("delhome").setExecutor(new DelHomeCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("delspawn").setExecutor(new DelSpawnCommand());
    }
    private void eventRegistration() {
        Bukkit.getPluginManager().registerEvents(new OnJoinListener(),this);
    }
    private void completerManager() {
        getCommand("home").setTabCompleter(new HomeCompleter());
    }
}
