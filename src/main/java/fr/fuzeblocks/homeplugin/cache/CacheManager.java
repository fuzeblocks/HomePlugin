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
import org.bukkit.Location;
import org.bukkit.entity.Player;
import redis.clients.jedis.JedisPooled;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {

    private static CacheManager instance;

    private final boolean useRedis;
    private final JedisPooled jedis;

    private final TpaRequestStore tpaRequestStore;
    private final HomeRequestStore homeStore;
    private final SpawnRequestStore spawnStore;
    private final RtpRequestStore rtpRequestStore;

    private CacheManager() {
        this.useRedis = HomePlugin.getConfigurationSection().getBoolean("Config.Connector.Redis.UseRedis");
        this.jedis = useRedis ? HomePlugin.getJedisPooled() : null;

        if (useRedis) {
            this.tpaRequestStore = new RedisTpaRequestStore(jedis);
            this.homeStore = new RedisHomeStore(jedis);
            this.spawnStore = new RedisSpawnStore(jedis);
            this.rtpRequestStore = new RedisRtpRequestStore(jedis);
        } else {
            this.tpaRequestStore = new LocalTpaRequestStore();
            this.homeStore = new LocalHomeStore();
            this.spawnStore = new LocalSpawnStore();
            this.rtpRequestStore = new LocalRtpRequestStore();
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


    public boolean hasTpaRequest(UUID sender,UUID target) {
        return tpaRequestStore.hasTpaRequest(sender,target);
    }

    public void removeTpaRequest(UUID sender,UUID target) {
        tpaRequestStore.removeTpaRequest(sender, target);
    }

    public UUID getTargetWithSender(UUID sender) {
       return tpaRequestStore.getTargetWithSender(sender);
    }

    public boolean hasIncomingTpa(UUID target) {
        return tpaRequestStore.hasIncomingTpa(target);
    }

    public UUID getSenderForTarget(UUID target) {
        return tpaRequestStore.getSenderForTarget(target);
    }

    public UUID getTpaTarget(UUID senderId) {
        return tpaRequestStore.getTpaTarget(senderId); // tpaRequests : Map<UUID, UUID>
    }
    public  Set<UUID> getAllTpaSenders() {
        return tpaRequestStore.getAllTpaSenders();
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

    // --- RTP ---



        public void addRtpRequest(UUID playerId, Long timestamp) {
            rtpRequestStore.addRtpRequest(playerId, timestamp);
        }

        public Long getRtpRequest(UUID playerId) {
            return rtpRequestStore.getRtpRequest(playerId);
        }


        public void removeRtpRequest(UUID playerId) {
            rtpRequestStore.removeRtpRequest(playerId);
        }


        public boolean hasRtpRequest(UUID playerId) {
            return rtpRequestStore.hasRtpRequest(playerId);
        }


        public Map<UUID, Long> getAllRtpRequests() {
            return rtpRequestStore.getAllRtpRequests();
        }



    // --- UTIL ---

    public boolean isUsingRedis() {
        return useRedis;
    }
}
