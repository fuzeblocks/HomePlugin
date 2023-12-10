package fr.fuzeblocks.homeplugin.task;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface TaskInterface {
 void homeTask(String homeName, Player player, Location location);
 void spawnTask(Player player);
}
