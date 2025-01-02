package fr.fuzeblocks.homeplugin.listeners;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.home.sql.HomeSQLManager;
import fr.fuzeblocks.homeplugin.home.yml.HomeYMLManager;
import fr.fuzeblocks.homeplugin.spawn.yml.SpawnYMLManager;
import fr.fuzeblocks.homeplugin.sync.type.SyncMethod;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (HomePlugin.getRegistrationType().equals(SyncMethod.MYSQL)) {
            HomeSQLManager homeSQLManager = HomePlugin.getHomeSQLManager();
            if (homeSQLManager.getHomeNumber(player) > 0) {
                CacheManager cacheManager = homeSQLManager.getCacheManager();
                cacheManager.addAllPlayerHomes(player);
            }
        } else {
            HomeYMLManager homeYMLManager = HomePlugin.getHomeYMLManager();
            if (homeYMLManager.getHomeNumber(player) > 0) {
                CacheManager cacheManager = homeYMLManager.getCacheManager();
                cacheManager.addAllPlayerHomes(player);
            }
        }
        if (!player.hasPlayedBefore()) {
            SpawnYMLManager spawnYMLManager = HomePlugin.getSpawnYMLManager();
            if (spawnYMLManager.hasSpawn(player.getWorld())) {
                player.teleport(spawnYMLManager.getSpawn(player.getWorld()));
            }
        }

    }
}
