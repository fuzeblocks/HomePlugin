package fr.fuzeblocks.homeplugin.task;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.status.Status;
import fr.fuzeblocks.homeplugin.status.StatusManager;
import fr.fuzeblocks.homeplugin.task.exception.TeleportTaskException;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Timer;
import java.util.TimerTask;

import static fr.fuzeblocks.homeplugin.HomePlugin.homeManager;
import static fr.fuzeblocks.homeplugin.HomePlugin.spawnManager;

public class TaskManager extends BukkitRunnable implements TaskInterface {
    private Task task;
    private Player player;
    private BukkitTask teleportTask;
    private HomePlugin plugin;
    private String homeName;


    public Task getTask() {
        return task;
    }

    public TaskManager(HomePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (task.equals(Task.Home)) {
            addTimeTitle();
            teleportHome();
        } else if (task.equals(Task.Spawn)) {
            addTimeTitle();
            teleportSpawn();
        }
    }

    private void teleportHome() {
        player.teleport(homeManager.getHomeLocation(player, homeName));
        player.sendMessage("§aVous vous êtes téléporté à votre home : " + homeName);
        player.playEffect(homeManager.getHomeLocation(player, homeName), Effect.MOBSPAWNER_FLAMES, 5000);
        StatusManager.setPlayerStatus(player, Status.FALSE);
    }

    private void teleportSpawn() {
        player.teleport(spawnManager.getSpawn());
        player.sendMessage("§aVous vous êtes téléporté au spawn");
        StatusManager.setPlayerStatus(player, Status.FALSE);
    }

    public void startTeleportTask() {
        teleportTask = runTaskLater(plugin, 20L * 3L);
    }

    public void cancelTeleportTask() throws TeleportTaskException {
        if (teleportTask != null && !teleportTask.isCancelled()) {
            teleportTask.cancel();
            player.sendMessage("§cLa téléportation a été annulée car vous avez bougé.");
            StatusManager.setPlayerStatus(player,Status.FALSE);
        } else {
            throw new TeleportTaskException();
        }
    }

    @Override
    public void homeTask(String homeName, Player player) {
        this.player = player;
        this.homeName = homeName;
        task = Task.Home;
    }

    @Override
    public void spawnTask(Player player) {
        this.player = player;
        task = Task.Spawn;
    }
    private void addTimeTitle() {
        new BukkitRunnable() {
            int time = 3;
            @Override
            public void run() {
                if (time == 3) {
                    player.sendTitle("§eTeleportation dans :", String.valueOf(time),100,1000,100);
                    time--;
                }
                if (time == 2) {
                    player.sendTitle("§eTeleportation dans :", String.valueOf(time),100,1000,100);
                    player.resetTitle();
                    time--;
                }
                if (time == 1) {
                    player.sendTitle("§eTeleportation dans :", String.valueOf(time),100,1000,100);
                    player.resetTitle();
                    time--;
                }
                if (time == 0) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin,0L,20L);
    }
}
