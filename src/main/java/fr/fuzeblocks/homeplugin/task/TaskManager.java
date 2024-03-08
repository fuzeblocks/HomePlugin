package fr.fuzeblocks.homeplugin.task;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.api.event.OnHomeTeleport;
import fr.fuzeblocks.homeplugin.api.event.OnSpawnTeleport;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.task.exception.TeleportTaskException;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;


import static fr.fuzeblocks.homeplugin.HomePlugin.*;

public class TaskManager extends BukkitRunnable {
    private Task task;
    private Player player;
    private BukkitTask teleportTask;
    private BukkitRunnable titleTask;
    private HomePlugin plugin;
    private String homeName;
    private Location homeLocation;


    public TaskManager(HomePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (task.equals(Task.Home)) {
            teleportHome();
        } else if (task.equals(Task.Spawn)) {
            teleportSpawn();
        }
    }

    private void teleportHome() {
        OnHomeTeleport onHomeTeleport;
        if (HomePlugin.getRegistrationType() == 1) {
            onHomeTeleport = new OnHomeTeleport(player,homeLocation);
        } else {
            onHomeTeleport = new OnHomeTeleport(player,homeLocation);
        }
        Bukkit.getServer().getPluginManager().callEvent(onHomeTeleport);
        if (!onHomeTeleport.isCancelled()) {
            player.teleport(onHomeTeleport.getHomeLocation());
            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString("Config.Language.Teleport-to-home")) + " " + homeName);
            player.resetTitle();
            player.playEffect(homeLocation, Effect.MOBSPAWNER_FLAMES, 5000);
        }
        StatusManager.setPlayerStatus(player, false);
        player.resetTitle();
    }

    private void teleportSpawn() {
        OnSpawnTeleport onSpawnTeleport;
        if (HomePlugin.getRegistrationType() == 1) {
            onSpawnTeleport = new OnSpawnTeleport(player,getSpawnSQLManager().getSpawn(player.getWorld()));
        } else {
           onSpawnTeleport = new OnSpawnTeleport(player, getSpawnManager().getSpawn(player.getWorld()));
        }
        Bukkit.getServer().getPluginManager().callEvent(onSpawnTeleport);
        if (!onSpawnTeleport.isCancelled()) {
            player.teleport(onSpawnTeleport.getSpawnLocation());
            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString("Config.Language.Teleport-to-spawn")));
            player.resetTitle();
        }
        StatusManager.setPlayerStatus(player, false);
        player.resetTitle();
    }

    public void startTeleportTask() {
        addTimeTitle();
        titleTask.runTaskTimer(plugin, 0L, 20L);
        teleportTask = runTaskLater(plugin, 20L * 3L);
    }



    public void cancelTeleportTask() throws TeleportTaskException {
        if (teleportTask != null && !teleportTask.isCancelled()) {
            teleportTask.cancel();
            titleTask.cancel();
            player.resetTitle();
            player.sendMessage(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString("Config.Language.Teleport-canceled")));
            StatusManager.setPlayerStatus(player,false);
        } else {
            throw new TeleportTaskException();
        }
    }


    public void homeTask(String homeName, Player player,Location location) {
        this.player = player;
        this.homeName = homeName;
        this.homeLocation = location;
        task = Task.Home;
    }

    public void spawnTask(Player player) {
        this.player = player;
        task = Task.Spawn;
    }
    private void addTimeTitle() {
         titleTask = new BukkitRunnable() {
            int time = 3;

            @Override
            public void run() {
                String key = "Config.Language.Teleportation-in-progress";
                if (time == 3) {
                    player.sendTitle(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key)) + " ", String.valueOf(time), 0, 1000, 0);
                    time--;
                } else if (time == 2) {
                    player.resetTitle();
                    player.sendTitle(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key)) + " ", String.valueOf(time), 0, 1000, 0);
                    time--;
                } else if (time == 1) {
                    player.resetTitle();
                    player.sendTitle(HomePlugin.translateAlternateColorCodes(HomePlugin.getConfigurationSection().getString(key)) + " ", String.valueOf(time), 0, 1000, 0);
                    time--;
                } else if (time == 0) {
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
}
