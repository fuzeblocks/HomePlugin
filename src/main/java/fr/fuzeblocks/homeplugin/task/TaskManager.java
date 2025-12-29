package fr.fuzeblocks. homeplugin.task;

import fr.fuzeblocks. homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.event. OnHomeTeleportEvent;
import fr.fuzeblocks. homeplugin.event.OnSpawnTeleportEvent;
import fr.fuzeblocks. homeplugin.language.LanguageManager;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.task.exception.TeleportTaskException;
import org.bukkit. Bukkit;
import org. bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * The type Task manager.
 */
public class TaskManager extends BukkitRunnable {

    private static final String LANG_PREFIX = "Language.";
    private static final String TASK_PREFIX = "Config.Task.";
    private static final long TICKS_PER_SECOND = 20L;

    private final HomePlugin plugin = HomePlugin.getPlugin(HomePlugin. class);
    private final ConfigurationSection config = HomePlugin.getConfigurationSection();
    private final LanguageManager languageManager = HomePlugin.getLanguageManager();

    private Task task;
    private Player player;
    private BukkitTask teleportTask;
    private BukkitRunnable titleTask;
    private String homeName;
    private Location homeLocation;

    @Override
    public void run() {
        try {
            if (task == Task.HOME) {
                teleportHome();
            } else if (task == Task.SPAWN) {
                teleportSpawn();
            }
        } finally {
            cleanup();
        }
    }

    /**
     * Teleports player to their home.
     */
    private void teleportHome() {
        OnHomeTeleportEvent event = new OnHomeTeleportEvent(player, homeLocation, homeName);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        player.teleport(event. getLocation());
        sendTeleportMessage(LANG_PREFIX + "Teleport-to-home", homeName);

        if (shouldPlayParticles()) {
            player.playEffect(homeLocation, Effect.MOBSPAWNER_FLAMES, 5000);
        }
    }

    /**
     * Teleports player to spawn.
     */
    private void teleportSpawn() {
        Location spawnLocation = HomePlugin.getSpawnManager().getSpawn(player.getWorld());
        OnSpawnTeleportEvent event = new OnSpawnTeleportEvent(player, spawnLocation);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        Player target = event.getPlayer();
        target.teleport(event. getLocation());
        target.sendMessage(languageManager. getStringWithColor(LANG_PREFIX + "Teleport-to-spawn"));
    }

    /**
     * Start teleport task.
     */
    public void startTeleportTask() {
        StatusManager.setPlayerStatus(player, true);
        TaskSaveUtils.setTaskManagerInstance(player, this);

        int duration = getTeleportDuration();
        startTitleTask(duration);

        teleportTask = runTaskLater(plugin, TICKS_PER_SECOND * duration);
    }

    /**
     * Cancel teleport task.
     *
     * @throws TeleportTaskException if task cannot be cancelled
     */
    public void cancelTeleportTask() throws TeleportTaskException {
        if (teleportTask == null || teleportTask.isCancelled()) {
            throw new TeleportTaskException();
        }

        teleportTask.cancel();

        if (titleTask != null && !titleTask.isCancelled()) {
            titleTask.cancel();
        }

        player.resetTitle();
        player.sendMessage(languageManager. getStringWithColor(LANG_PREFIX + "Teleport-canceled"));
        StatusManager.setPlayerStatus(player, false);
        TaskSaveUtils.removeTaskManagerInstance(player);
    }

    /**
     * Configure home task.
     *
     * @param homeName the home name
     * @param player   the player
     * @param location the location
     */
    public void homeTask(String homeName, Player player, Location location) {
        this.player = player;
        this.homeName = homeName;
        this.homeLocation = location;
        this.task = Task.HOME;
    }

    /**
     * Configure spawn task.
     *
     * @param player the player
     */
    public void spawnTask(Player player) {
        this.player = player;
        this.task = Task.SPAWN;
    }

    /**
     * Starts the title/message countdown task.
     *
     * @param duration the duration in seconds
     */
    private void startTitleTask(int duration) {
        final boolean useTitle = config.getBoolean(TASK_PREFIX + "Use-Title");
        final boolean useMessage = config.getBoolean(TASK_PREFIX + "Use-Message");

        if (! useTitle && !useMessage) {
            return; // No need to start task if neither is enabled
        }

        titleTask = new BukkitRunnable() {
            int timeRemaining = duration;

            @Override
            public void run() {
                if (timeRemaining < 0) {
                    player.resetTitle();
                    cancel();
                    return;
                }

                String message = languageManager.getStringWithColor(LANG_PREFIX + "Teleportation-in-progress") + " ";

                if (useTitle) {
                    player.sendTitle(message, String.valueOf(timeRemaining), 0, 20, 0);
                }

                if (useMessage) {
                    player.sendMessage(message + timeRemaining);
                }

                timeRemaining--;
            }
        };

        titleTask.runTaskTimer(plugin, 0L, TICKS_PER_SECOND);
    }

    /**
     * Gets the teleport duration, accounting for OP skip.
     *
     * @return duration in seconds
     */
    private int getTeleportDuration() {
        boolean skipForOp = config.getBoolean(TASK_PREFIX + "Skip-If-Op");
        if (player.isOp() && skipForOp) {
            return 0;
        }
        return config.getInt(TASK_PREFIX + "Task-Duration");
    }

    /**
     * Checks if particles should be played after teleport.
     *
     * @return true if particles are enabled
     */
    private boolean shouldPlayParticles() {
        return config.getBoolean(TASK_PREFIX + "Particles-After-Teleport");
    }

    /**
     * Sends a teleport completion message.
     *
     * @param key the language key
     * @param suffix optional suffix to append
     */
    private void sendTeleportMessage(String key, String suffix) {
        String message = languageManager. getStringWithColor(key);
        if (suffix != null && !suffix.isEmpty()) {
            message += " " + suffix;
        }
        player.sendMessage(message);
    }

    /**
     * Cleans up task state after completion.
     */
    private void cleanup() {
        StatusManager.setPlayerStatus(player, false);
        player.resetTitle();
        TaskSaveUtils.removeTaskManagerInstance(player);
    }

    // Getters

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