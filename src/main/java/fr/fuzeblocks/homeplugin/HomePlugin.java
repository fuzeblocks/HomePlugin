package fr.fuzeblocks.homeplugin;

import fr.fuzeblocks.homeplugin.Commandes.HomeCommand;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class HomePlugin extends JavaPlugin {
   public static LuckPerms api;
   public static File home;

    @Override
    public void onEnable() {
        getCommand("home").setExecutor(new HomeCommand());
        home = new File(this.getDataFolder() + "/homes.yml");
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
             api = provider.getProvider();
        }

    }

    @Override
    public void onDisable() {

    }
}
