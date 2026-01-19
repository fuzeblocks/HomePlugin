package fr.fuzeblocks.homeplugin.cache;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.home.HomeRequestStore;
import fr.fuzeblocks.homeplugin.home.LocalHomeStore;
import fr.fuzeblocks.homeplugin.home.RedisHomeStore;
import fr.fuzeblocks.homeplugin.rtp.LocalRtpRequestStore;
import fr.fuzeblocks.homeplugin.rtp.RedisRtpRequestStore;
import fr.fuzeblocks.homeplugin.rtp.RtpRequestStore;
import fr.fuzeblocks.homeplugin.spawn.LocalSpawnStore;
import fr.fuzeblocks.homeplugin.spawn.RedisSpawnStore;
import fr.fuzeblocks.homeplugin.spawn.SpawnRequestStore;
import fr.fuzeblocks.homeplugin.tpa.LocalTpaRequestStore;
import fr.fuzeblocks.homeplugin.tpa.RedisTpaRequestStore;
import fr.fuzeblocks.homeplugin.tpa.TpaRequestStore;
import fr.fuzeblocks.homeplugin.warps.LocalWarpStore;
import fr.fuzeblocks.homeplugin.warps.RedisWarpStore;
import fr.fuzeblocks.homeplugin.warps.WarpData;
import fr.fuzeblocks.homeplugin.warps.WarpRequestStore;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import redis.clients.jedis.JedisPooled;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The type Cache manager.
 */
public class CacheManager {

    private static CacheManager instance;

    private final boolean useRedis;
    private final JedisPooled jedis;

    private final TpaRequestStore tpaRequestStore;
    private final HomeRequestStore homeStore;
    private final SpawnRequestStore spawnStore;
    private final RtpRequestStore rtpRequestStore;
    private final WarpRequestStore warpRequestStore;

    private CacheManager() {
        this.useRedis = HomePlugin.getConfigurationSection().getBoolean("Config.Connector.Redis.UseRedis");
        this.jedis = useRedis ? HomePlugin.getJedisPooled() : null;

        if (useRedis) {
            this.tpaRequestStore = new RedisTpaRequestStore(jedis);
            this.homeStore = new RedisHomeStore(jedis);
            this.spawnStore = new RedisSpawnStore(jedis);
            this.rtpRequestStore = new RedisRtpRequestStore(jedis);
            this.warpRequestStore = new RedisWarpStore(jedis);
        } else {
            this.tpaRequestStore = new LocalTpaRequestStore();
            this.homeStore = new LocalHomeStore();
            this.spawnStore = new LocalSpawnStore();
            this.rtpRequestStore = new LocalRtpRequestStore();
            this.warpRequestStore = new LocalWarpStore();
        }
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static synchronized CacheManager getInstance() {
        if (instance == null) instance = new CacheManager();
        return instance;
    }


    // --- TPA REQUESTS ---

    /**
     * Add tpa request.
     *
     * @param sender the sender
     * @param target the target
     */
    public void addTpaRequest(UUID sender, UUID target) {
        tpaRequestStore.addTpaRequest(sender, target);
    }


    /**
     * Has tpa request boolean.
     *
     * @param sender the sender
     * @param target the target
     * @return the boolean
     */
    public boolean hasTpaRequest(UUID sender, UUID target) {
        return tpaRequestStore.hasTpaRequest(sender, target);
    }

    /**
     * Remove tpa request.
     *
     * @param sender the sender
     * @param target the target
     */
    public void removeTpaRequest(UUID sender, UUID target) {
        tpaRequestStore.removeTpaRequest(sender, target);
    }

    /**
     * Gets target with sender.
     *
     * @param sender the sender
     * @return the target with sender
     */
    public UUID getTargetWithSender(UUID sender) {
        return tpaRequestStore.getTargetWithSender(sender);
    }

    /**
     * Has incoming tpa boolean.
     *
     * @param target the target
     * @return the boolean
     */
    public boolean hasIncomingTpa(UUID target) {
        return tpaRequestStore.hasIncomingTpa(target);
    }

    /**
     * Gets sender for target.
     *
     * @param target the target
     * @return the sender for target
     */
    public UUID getSenderForTarget(UUID target) {
        return tpaRequestStore.getSenderForTarget(target);
    }

    /**
     * Gets tpa target.
     *
     * @param senderId the sender id
     * @return the tpa target
     */
    public UUID getTpaTarget(UUID senderId) {
        return tpaRequestStore.getTpaTarget(senderId); // tpaRequests : Map<UUID, UUID>
    }

    /**
     * Gets all tpa senders.
     *
     * @return the all tpa senders
     */
    public Set<UUID> getAllTpaSenders() {
        return tpaRequestStore.getAllTpaSenders();
    }


    // --- HOMES ---

    /**
     * Add home.
     *
     * @param playerId the player id
     * @param homeName the home name
     * @param location the location
     */
    public void addHome(UUID playerId, String homeName, Location location) {
        homeStore.addHome(playerId, homeName, location);
    }

    /**
     * Remove home.
     *
     * @param playerId the player id
     * @param homeName the home name
     */
    public void removeHome(UUID playerId, String homeName) {
        homeStore.removeHome(playerId, homeName);
    }

    /**
     * Relocate home.
     *
     * @param playerId    the player id
     * @param homeName    the home name
     * @param newLocation the new location
     */
    public void relocateHome(UUID playerId, String homeName, Location newLocation) {
        homeStore.removeHome(playerId, homeName);
    }

    /**
     * Gets homes.
     *
     * @param playerId the player id
     * @return the homes
     */
    public Map<String, Location> getHomes(UUID playerId) {
        return homeStore.getHomes(playerId);
    }

    /**
     * Clear homes.
     *
     * @param playerId the player id
     */
    public void clearHomes(UUID playerId) {
        homeStore.clearHomes(playerId);
    }

    /**
     * Clear all homes.
     */
    public void clearAllHomes() {
        homeStore.clearAllHomes();
    }

    /**
     * Charge toutes les homes du joueur depuis HomeManager et les ajoute au cache.
     *
     * @param player Joueur concerné
     */
    public void loadAllHomesToCache(Player player) {
        HomeManager homeManager = HomePlugin.getHomeManager();
        for (String homeName : homeManager.getHomesName(player)) {
            System.out.println("Loading home '" + homeName + "' for player " + player.getName() + " into cache.");
            assert homeName != null;
            assert homeManager.exist(player, homeName);
            Location location = homeManager.getHomeLocation(player, homeName);
            addHome(player.getUniqueId(), homeName, location);
        }
    }

    // --- SPAWN ---

    /**
     * Gets spawn.
     *
     * @return the spawn
     */
    public Location getSpawn() {
        return spawnStore.getSpawn();
    }

    /**
     * Sets spawn.
     *
     * @param location the location
     */
    public void setSpawn(Location location) {
        spawnStore.setSpawn(location);
    }

    /**
     * Clear spawn.
     */
    public void clearSpawn() {
        spawnStore.clearSpawn();
    }

    // --- RTP ---


    /**
     * Add rtp request.
     *
     * @param playerId  the player id
     * @param timestamp the timestamp
     */
    public void addRtpRequest(UUID playerId, Long timestamp) {
        rtpRequestStore.addRtpRequest(playerId, timestamp);
    }

    /**
     * Gets rtp request.
     *
     * @param playerId the player id
     * @return the rtp request
     */
    public Long getRtpRequest(UUID playerId) {
        return rtpRequestStore.getRtpRequest(playerId);
    }


    /**
     * Remove rtp request.
     *
     * @param playerId the player id
     */
    public void removeRtpRequest(UUID playerId) {
        rtpRequestStore.removeRtpRequest(playerId);
    }

    /**
     * Has rtp request boolean.
     *
     * @param playerId the player id
     * @return the boolean
     */
    public boolean hasRtpRequest(UUID playerId) {
        return rtpRequestStore.hasRtpRequest(playerId);
    }


    /**
     * Gets all rtp requests.
     *
     * @return the all rtp requests
     */
    public Map<UUID, Long> getAllRtpRequests() {
        return rtpRequestStore.getAllRtpRequests();
    }


    // --- WARPS ---

    /**
     * Ajoute ou met à jour un warp dans le cache.
     *
     * @param warpData the warp data
     */
    public void addWarp(WarpData warpData) {
        warpRequestStore.saveWarp(warpData);
    }

    /**
     * Supprime un warp par son nom.
     *
     * @param name the name
     */
    public void removeWarp(String name) {
        warpRequestStore.deleteWarp(name);
    }

    /**
     * Charge un warp par son nom.
     *
     * @param name the name
     * @return the warp
     */
    public WarpData getWarp(String name) {
        return warpRequestStore.loadWarp(name);
    }

    /**
     * Vérifie si un warp existe dans le cache.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean warpExists(String name) {
        return warpRequestStore.warpExists(name);
    }

    /**
     * Retourne tous les warps dans le cache (nom -> WarpData).
     *
     * @return the all warps
     */
    public Map<String, WarpData> getAllWarps() {
        return warpRequestStore.loadAllWarps();
    }

    /**
     * Retourne tous les noms de warps dans le cache.
     *
     * @return the warp names
     */
    public Set<String> getWarpNames() {
        return warpRequestStore.getWarpNames();
    }


    // --- UTIL ---

    /**
     * Is using redis boolean.
     *
     * @return the boolean
     */
    public boolean isUsingRedis() {
        return useRedis;
    }
}
