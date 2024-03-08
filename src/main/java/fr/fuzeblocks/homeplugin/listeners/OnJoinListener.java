package fr.fuzeblocks.homeplugin.listeners;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.home.yml.HomeManager;
import fr.fuzeblocks.homeplugin.spawn.yml.SpawnManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (HomePlugin.getRegistrationType() == 1) {
            fr.fuzeblocks.homeplugin.home.sql.HomeManager homeSQLManager = HomePlugin.getHomeSQLManager();
            if (homeSQLManager.getHomeNumber(player) > 0) {
                CacheManager cacheManager = homeSQLManager.getCacheManager();
                cacheManager.addAllPlayerHomes(player);
            }
        } else {
            HomeManager homeManager = HomePlugin.getHomeManager();
            if (homeManager.getHomeNumber(player) > 0) {
                CacheManager cacheManager = homeManager.getCacheManager();
                cacheManager.addAllPlayerHomes(player);
            }
        }
        if (!player.hasPlayedBefore()) {
            SpawnManager spawnManager = HomePlugin.getSpawnManager();
            if (spawnManager.hasSpawn(player.getWorld())) {
                player.teleport(spawnManager.getSpawn(player.getWorld()));
            }
        }

    }
}
