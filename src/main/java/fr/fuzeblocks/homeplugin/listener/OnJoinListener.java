package fr.fuzeblocks.homeplugin.listener;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.spawn.SpawnManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            SpawnManager spawnManager = HomePlugin.getSpawnManager();
            if (spawnManager.hasSpawn()) {
                player.teleport(spawnManager.getSpawn());
            }
        }

    }
}
