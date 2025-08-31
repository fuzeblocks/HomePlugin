package fr.fuzeblocks.homeplugin.commands;

import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.UUID;

public class RtpCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(HomePlugin.getLanguageManager().getStringWithColor("Language.Only-a-player-can-execute", "&cSeul un joueur peut exécuter cette commande !"));
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();
        var cooldowns = HomePlugin.getCacheManager();

        if (cooldowns.hasRtpRequest(uuid)) {
            int cooldownSeconds = HomePlugin.getConfigurationSection().getInt("Rtp.cooldown-seconds", 300);
            long remaining = (cooldowns.getRtpRequest(uuid) + (cooldownSeconds * 1000L) - now) / 1000;
            if (remaining > 0) {
                player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor("Rtp.Cooldown-message", "&cVous devez attendre " + remaining + " secondes avant de refaire un RTP.").replace("%seconds%", String.valueOf(remaining)));
                return true;
            }
        }

        World world = player.getWorld();
        Location randomLocation = getRandomHighestLocation(world);
        if (randomLocation == null) {
            player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor("Rtp.Teleport-failed", "&cImpossible de trouver un endroit sûr pour téléporter."));
            return true;
        }

        player.teleport(randomLocation);
        player.sendMessage(HomePlugin.getLanguageManager().getStringWithColor("Rtp.Teleport-success", "&aTéléportation aléatoire réussie !"));
        cooldowns.addRtpRequest(uuid, now);
        return true;
    }

    private Location getRandomHighestLocation(World world) {
        Random random = new Random();
        for (int i = 0; i < 25; i++) {
            int maxRadius = HomePlugin.getConfigurationSection().getInt("Rtp.max-radius", 200);
            int x = random.nextInt(maxRadius * 2) - maxRadius;
            int z = random.nextInt(maxRadius * 2) - maxRadius;
            int y = world.getHighestBlockYAt(x, z);
            Location loc = new Location(world, x + 0.5, y, z + 0.5);
            if (loc.getBlock().getType().isSolid() && loc.add(0, 1, 0).getBlock().isPassable()) {
                return loc;
            }
        }
        return null;
    }
}
