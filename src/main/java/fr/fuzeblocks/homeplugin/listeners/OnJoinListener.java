package fr.fuzeblocks.homeplugin.listeners;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.spawn.SpawnManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        HomeManager homeManager = HomePlugin.getHomeManager();
            if (homeManager.getHomeNumber(player) > 0) {
                CacheManager cacheManager = homeManager.getCacheManager();
                cacheManager.loadAllHomesToCache(player);
            }
        if (!player.hasPlayedBefore()) {
            SpawnManager spawnManager = HomePlugin.getSpawnManager();
            if (spawnManager.hasSpawn(player.getWorld())) {
                player.teleport(spawnManager.getSpawn(player.getWorld()));
            }
        }

    }
}
