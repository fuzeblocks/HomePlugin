package fr.fuzeblocks.homeplugin;

import fr.fuzeblocks.homeplugin.Commands.DelHomeCommand;
import fr.fuzeblocks.homeplugin.Commands.HomeCommand;
import fr.fuzeblocks.homeplugin.Commands.SetHomeCommand;
import fr.fuzeblocks.homeplugin.Home.HomeManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class HomePlugin extends JavaPlugin {
   public static LuckPerms api;
   public static File home;
   public static HomeManager homeManager;

    @Override
    public void onEnable() {
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("sethome").setExecutor(new SetHomeCommand());
        getCommand("delhome").setExecutor(new DelHomeCommand());
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
            getLogger().info("LuckPerms API chargée avec succès.");
        } else {
            getLogger().severe("Impossible de charger LuckPerms API. Le plugin peut ne pas fonctionner correctement.");
            getServer().getPluginManager().disablePlugin(this);
        }
        home = new File(this.getDataFolder(), "homes.yml");
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }

        if (!home.exists()) {
            try {
                home.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        homeManager = new HomeManager(home);
    }

        @Override
    public void onDisable() {

    }
}
