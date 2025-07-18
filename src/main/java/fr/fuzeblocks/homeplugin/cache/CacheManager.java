package fr.fuzeblocks.homeplugin.cache;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.home.HomeManager;
import fr.fuzeblocks.homeplugin.home.HomeStore;
import fr.fuzeblocks.homeplugin.home.LocalHomeStore;
import fr.fuzeblocks.homeplugin.home.RedisHomeStore;
import fr.fuzeblocks.homeplugin.spawn.LocalSpawnStore;
import fr.fuzeblocks.homeplugin.spawn.RedisSpawnStore;
import fr.fuzeblocks.homeplugin.spawn.SpawnStore;
import fr.fuzeblocks.homeplugin.tpa.LocalTpaRequestStore;
import fr.fuzeblocks.homeplugin.tpa.RedisTpaRequestStore;
import fr.fuzeblocks.homeplugin.tpa.TpaRequestStore;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import redis.clients.jedis.JedisPooled;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CacheManager {

    private static CacheManager instance;

    private final boolean useRedis;
    private final JedisPooled jedis;

    private final TpaRequestStore tpaRequestStore;
    private final HomeStore homeStore;
    private final SpawnStore spawnStore;

    private CacheManager() {
        this.useRedis = HomePlugin.getConfigurationSection().getBoolean("Config.Connector.Redis.UseRedis");
        this.jedis = useRedis ? HomePlugin.getJedisPooled() : null;

        if (useRedis) {
            this.tpaRequestStore = new RedisTpaRequestStore(jedis);
            this.homeStore = new RedisHomeStore(jedis);
            this.spawnStore = new RedisSpawnStore(jedis);
        } else {
            this.tpaRequestStore = new LocalTpaRequestStore();
            this.homeStore = new LocalHomeStore();
            this.spawnStore = new LocalSpawnStore();
        }
    }

    public static synchronized CacheManager getInstance() {
        if (instance == null) instance = new CacheManager();
        return instance;
    }

    // --- TPA REQUESTS ---

    public void addTpaRequest(UUID sender, UUID target) {
        tpaRequestStore.addTpaRequest(sender, target);
    }

    public UUID getTpaRequest(UUID sender) {
        return tpaRequestStore.getTpaRequest(sender);
    }

    public boolean hasTpaRequest(UUID sender) {
        return tpaRequestStore.hasTpaRequest(sender);
    }

    public void removeTpaRequest(UUID sender) {
        tpaRequestStore.removeTpaRequest(sender);
    }

    public Set<UUID> getAllTpaSenders() {
        return tpaRequestStore.getAllSenders();
    }

    // --- HOMES ---

    public void addHome(UUID playerId, String homeName, Location location) {
        homeStore.addHome(playerId, homeName, location);
    }

    public void removeHome(UUID playerId, String homeName) {
        homeStore.removeHome(playerId, homeName);
    }

    public Map<String, Location> getHomes(UUID playerId) {
        return homeStore.getHomes(playerId);
    }

    public void clearHomes(UUID playerId) {
        homeStore.clearHomes(playerId);
    }

    public void clearAllHomes() {
        homeStore.clearAllHomes();
    }

    /**
     * Charge toutes les homes du joueur depuis HomeManager et les ajoute au cache.
     * @param player Joueur concerné
     */
    public void loadAllHomesToCache(Player player) {
        HomeManager homeManager = HomePlugin.getHomeManager();
        for (String homeName : homeManager.getHomesName(player)) {
            Location location = homeManager.getHomeLocation(player, homeName);
            addHome(player.getUniqueId(), homeName, location);
        }
    }

    // --- SPAWN ---

    public void setSpawn(Location location) {
        spawnStore.setSpawn(location);
    }

    public Location getSpawn() {
        return spawnStore.getSpawn();
    }

    public void clearSpawn() {
        spawnStore.clearSpawn();
    }

    // --- UTIL ---

    public boolean isUsingRedis() {
        return useRedis;
    }
}
