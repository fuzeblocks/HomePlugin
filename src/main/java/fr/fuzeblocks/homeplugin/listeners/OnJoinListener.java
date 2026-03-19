package fr.fuzeblocks.homeplugin.listeners;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.cache.CacheManager;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.spawn.SpawnManager;
import fr.fuzeblocks.homeplugin.update.UpdateChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * The type On join listener.
 */
public class OnJoinListener implements Listener {

    private final String UPDATE_KEY = "Language.Update.";
    /**
     * On join.
     *
     * @param event the event
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.isOp()) {
            if (UpdateChecker.shoudAskForUpdatePlugin()) {
                player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                        UPDATE_KEY + "Update-Plugin",
                        "&aMerci de mettre à jour le plugin pour profiter des dernières fonctionnalités et corrections de bugs !"));

            }
            if (UpdateChecker.shouldAskForUpdateLangFiles()) {
                player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor(
                        UPDATE_KEY + "Update-Lang" ,
                        "&aMerci d'executer la commande /lang update pour mettre à jour votre fichier de langue avec les nouvelles clés et traductions !"));
            }
        }
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
