package fr.fuzeblocks.homeplugin.task;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.task.exception.TeleportTaskException;
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
    private Location location;


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
        player.teleport(location);
        player.sendMessage("§aVous vous êtes téléporté à votre home : " + homeName);
        player.resetTitle();
        StatusManager.setPlayerStatus(player, false);
        player.playEffect(getHomeManager().getHomeLocation(player, homeName), Effect.MOBSPAWNER_FLAMES, 5000);
    }

    private void teleportSpawn() {
        player.teleport(getSpawnManager().getSpawn());
        player.sendMessage("§aVous vous êtes téléporté au spawn");
        StatusManager.setPlayerStatus(player, false);
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
            player.sendMessage("§cLa téléportation a été annulée car vous avez bougé.");
            StatusManager.setPlayerStatus(player,false);
        } else {
            throw new TeleportTaskException();
        }
    }


    public void homeTask(String homeName, Player player,Location location) {
        this.player = player;
        this.homeName = homeName;
        this.location = location;
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
                if (time == 3) {
                    player.sendTitle("§eTeleportation dans :", String.valueOf(time), 0, 1000, 0);
                    time--;
                } else if (time == 2) {
                    player.resetTitle();
                    player.sendTitle("§eTeleportation dans :", String.valueOf(time), 0, 1000, 0);
                    time--;
                } else if (time == 1) {
                    player.resetTitle();
                    player.sendTitle("§eTeleportation dans :", String.valueOf(time), 0, 1000, 0);
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
