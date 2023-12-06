package fr.fuzeblocks.homeplugin;

import fr.fuzeblocks.homeplugin.Commands.DelHomeCommand;
import fr.fuzeblocks.homeplugin.Commands.HomeCommand;
import fr.fuzeblocks.homeplugin.Commands.SetHomeCommand;
import fr.fuzeblocks.homeplugin.Home.HomeManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class HomePlugin extends JavaPlugin {
   public static LuckPerms api;
   public static File home;
   public static HomeManager homeManager;

    @Override
    public void onEnable() {
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("sethome").setExecutor(new SetHomeCommand());
        getCommand("delhome").setExecutor(new DelHomeCommand());
        home = new File(this.getDataFolder() + "/homes.yml");
        homeManager = new HomeManager(home);
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
             api = provider.getProvider();
        }

    }

    @Override
    public void onDisable() {

    }
}
