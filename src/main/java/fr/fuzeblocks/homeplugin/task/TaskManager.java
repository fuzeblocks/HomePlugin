package fr.fuzeblocks.homeplugin.task;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.event.OnHomeTeleportEvent;
import fr.fuzeblocks.homeplugin.event.OnSpawnTeleportEvent;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.task.exception.TeleportTaskException;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * The type Task manager.
 */
public class TaskManager extends BukkitRunnable {
    private final HomePlugin plugin = HomePlugin.getPlugin(HomePlugin.class);
    private Task task;
    private Player player;
    private BukkitTask teleportTask;
    private BukkitRunnable titleTask;
    private String homeName;
    private Location homeLocation;


    @Override
    public void run() {
        if (task.equals(Task.HOME)) {
            teleportHome();
        } else if (task.equals(Task.SPAWN)) {
            teleportSpawn();
        }
    }

    private void teleportHome() {
        OnHomeTeleportEvent onHomeTeleport = new OnHomeTeleportEvent(player, homeLocation, homeName);
        Bukkit.getServer().getPluginManager().callEvent(onHomeTeleport);
        if (!onHomeTeleport.isCancelled()) {
            player.teleport(onHomeTeleport.getLocation());
            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor("Language.Teleport-to-home") + " " + homeName);
            player.resetTitle();
            if (HomePlugin.getConfigurationSection().getBoolean("Config.Task.Particles-After-Teleport")) {
                player.playEffect(homeLocation, Effect.MOBSPAWNER_FLAMES, 5000);
            }
        }
        StatusManager.setPlayerStatus(player, false);
        player.resetTitle();
    }

    private void teleportSpawn() {
        OnSpawnTeleportEvent onSpawnTeleport = new OnSpawnTeleportEvent(player, HomePlugin.getSpawnManager().getSpawn(player.getWorld()));
        Player target = onSpawnTeleport.getPlayer();
        Bukkit.getServer().getPluginManager().callEvent(onSpawnTeleport);
        if (!onSpawnTeleport.isCancelled()) {
            target.teleport(onSpawnTeleport.getLocation());
            target.sendMessage(HomePlugin.getLanguageManager().getStringWithColor("Language.Teleport-to-spawn"));
            target.resetTitle();
        }
        StatusManager.setPlayerStatus(player, false);
        target.resetTitle();
    }

    /**
     * Start teleport task.
     */
    public void startTeleportTask() {
        StatusManager.setPlayerStatus(player, true);
        TaskSaveUtils.setTaskManagerInstance(player, this);
        addTimeTitle(player);
        int duration = HomePlugin.getConfigurationSection().getInt("Config.Task.Task-duration");
        teleportTask = runTaskLater(plugin, 20L * duration);
    }


    /**
     * Cancel teleport task.
     *
     * @throws TeleportTaskException the teleport task exception
     */
    public void cancelTeleportTask() throws TeleportTaskException {
        if (teleportTask != null && !teleportTask.isCancelled()) {
            teleportTask.cancel();
            titleTask.cancel();
            player.resetTitle();
            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor("Language.Teleport-canceled"));
            StatusManager.setPlayerStatus(player, false);
        } else {
            throw new TeleportTaskException();
        }
    }


    /**
     * Home task.
     *
     * @param homeName the home name
     * @param player   the player
     * @param location the location
     */
    public void homeTask(String homeName, Player player, Location location) {
        this.player = player;
        this.homeName = homeName;
        this.homeLocation = location;
        task = Task.HOME;
    }

    /**
     * Spawn task.
     *
     * @param player the player
     */
    public void spawnTask(Player player) {
        this.player = player;
        task = Task.SPAWN;
    }

    private void addTimeTitle(Player player) {
        int duration = HomePlugin.getConfigurationSection().getInt("Config.Task.Task-duration");

        titleTask = new BukkitRunnable() {
            int time = duration;

            @Override
            public void run() {
                String key = "Language.Teleportation-in-progress";
                String message = HomePlugin.getLanguageManager().getStringWithColor(key) + " ";

                if (HomePlugin.getConfigurationSection().getBoolean("Config.Task.Use-Title")) {
                    player.sendTitle(message, String.valueOf(time), 0, 20, 0);
                }

                if (HomePlugin.getConfigurationSection().getBoolean("Config.Task.Use-Message")) {
                    player.sendMessage(message + time);
                }

                time--;
                if (time <= 0) {
                    player.resetTitle();
                    cancel();
                }
            }
        };

        titleTask.runTaskTimer(plugin, 0L, 20L);
    }


    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets task.
     *
     * @return the task
     */
    public Task getTask() {
        return task;
    }

    /**
     * Gets home name.
     *
     * @return the home name
     */
    public String getHomeName() {
        return homeName;
    }

    /**
     * Gets home location.
     *
     * @return the home location
     */
    public Location getHomeLocation() {
        return homeLocation;
    }
}
