package fr.fuzeblocks.homeplugin.task;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.api.event.OnHomeTeleportEvent;
import fr.fuzeblocks.homeplugin.api.event.OnSpawnTeleportEvent;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.task.exception.TeleportTaskException;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class TaskManager extends BukkitRunnable {
    private Task task;
    private Player player;
    private BukkitTask teleportTask;
    private BukkitRunnable titleTask;
    private final HomePlugin plugin;
    private String homeName;
    private Location homeLocation;


    public TaskManager(HomePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (task.equals(Task.HOME)) {
            teleportHome();
        } else if (task.equals(Task.SPAWN)) {
            teleportSpawn();
        }
    }

    private void teleportHome() {
        OnHomeTeleportEvent onHomeTeleport = new OnHomeTeleportEvent(player, homeLocation);
        Bukkit.getServer().getPluginManager().callEvent(onHomeTeleport);
        if (!onHomeTeleport.isCancelled()) {
            player.teleport(onHomeTeleport.getLocation());
            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Language.Teleport-to-home")) + " " + homeName);
            player.resetTitle();
            if (HomePlugin.getConfigurationSection().getBoolean("Config.Task.Add-particles-after-teleport")) {
                player.playEffect(homeLocation, Effect.MOBSPAWNER_FLAMES, 5000);
            }
        }
        StatusManager.setPlayerStatus(player, false);
        player.resetTitle();
    }

    private void teleportSpawn() {
        OnSpawnTeleportEvent onSpawnTeleport = new OnSpawnTeleportEvent(player, HomePlugin.getSpawnManager().getSpawn(player.getWorld()));
        Bukkit.getServer().getPluginManager().callEvent(onSpawnTeleport);
        if (!onSpawnTeleport.isCancelled()) {
            player.teleport(onSpawnTeleport.getLocation());
            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Language.Teleport-to-spawn")));
            player.resetTitle();
        }
        StatusManager.setPlayerStatus(player, false);
        player.resetTitle();
    }

    public void startTeleportTask() {
        StatusManager.setPlayerStatus(player, true);
        TaskSaveUtils.setTaskManagerInstance(player,this);
        addTimeTitle();
        titleTask.runTaskTimer(plugin, 0L, 20L);
        teleportTask = runTaskLater(plugin, 20L * 3L);
    }


    public void cancelTeleportTask() throws TeleportTaskException {
        if (teleportTask != null && !teleportTask.isCancelled()) {
            teleportTask.cancel();
            titleTask.cancel();
            player.resetTitle();
            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString("Language.Teleport-canceled")));
            StatusManager.setPlayerStatus(player, false);
        } else {
            throw new TeleportTaskException();
        }
    }



    public void homeTask(String homeName, Player player, Location location) {
        this.player = player;
        this.homeName = homeName;
        this.homeLocation = location;
        task = Task.HOME;
    }

    public void spawnTask(Player player) {
        this.player = player;
        task = Task.SPAWN;
    }

    private void addTimeTitle() {
        titleTask = new BukkitRunnable() {
            int time = HomePlugin.getConfigurationSection().getInt("Config.Task.Task-duration");

            @Override
            public void run() {
                String key = "Language.Teleportation-in-progress";
                if (HomePlugin.getConfigurationSection().getBoolean("Config.Task.UseTitle")) {
                    player.sendTitle(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key)) + " ", String.valueOf(time), 0, 1000, 0);
                }
                if (HomePlugin.getConfigurationSection().getBoolean("Config.Task.UseMessage")) {
                    player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getLanguageManager().getString(key)) + " ", String.valueOf(time));
                }
                time--;
                if (time <= 0) {
                    cancel();
                }
            }
        };

    }


    public Player getPlayer() {
        return player;
    }

    public Task getTask() {
        return task;
    }

    public String getHomeName() {
        return homeName;
    }

    public Location getHomeLocation() {
        return homeLocation;
    }
}
