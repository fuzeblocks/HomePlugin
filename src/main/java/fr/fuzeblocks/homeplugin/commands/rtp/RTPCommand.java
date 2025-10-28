package fr.fuzeblocks.homeplugin.commands.rtp;

import fr.fuzeblocks.homeplugin.HomePlugin;
import fr.fuzeblocks.homeplugin.economy.EconomyManager;
import fr.fuzeblocks.homeplugin.event.OnRtpEvent;
import fr.fuzeblocks.homeplugin.language.LanguageManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Random Teleport (RTP) command.
 * Teleports the player to a random safe location within a defined radius.
 */
public class RTPCommand implements CommandExecutor {
    private final static String CONFIG_RTP = "Config.Rtp.";
    private final static String LANGUAGE = "Language.";
    private final static String MESSAGE_RTP = "Rtp.";
    private final static int COOLDOWN_SECONDS = Math.max(0, HomePlugin.getConfigurationSection().getInt(CONFIG_RTP + "Cooldown-Seconds", 300));
    private final static int MAX_RADIUS = Math.max(1, HomePlugin.getConfigurationSection().getInt(CONFIG_RTP + "Max-Radius", 200));
    private final LanguageManager languageManager = HomePlugin.getLanguageManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(languageManager.getStringWithColor(LANGUAGE + "Only-a-player-can-execute", "&cSeul un joueur peut exécuter cette commande !"));
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();


        var cooldowns = HomePlugin.getCacheManager();
        if (cooldowns.hasRtpRequest(uuid)) {
            long remaining = (cooldowns.getRtpRequest(uuid) + (COOLDOWN_SECONDS * 1000L) - now) / 1000;
            if (remaining > 0) {
                player.sendMessage(
                        languageManager
                                .getStringWithColor(MESSAGE_RTP + "Cooldown-message", "&cVous devez attendre %seconds% secondes avant de refaire un RTP.")
                                .replace("%seconds%", String.valueOf(remaining))
                );
                return true;
            }
        }


        Location randomLocation = getRandomSafeLocation(player.getLocation());
        if (randomLocation == null) {
            player.sendMessage(languageManager.getStringWithColor(MESSAGE_RTP + "Teleport-failed", "&cImpossible de trouver un endroit sûr pour téléporter."));
            return true;
        }


        OnRtpEvent event = new OnRtpEvent(player, randomLocation);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled() || event.getPlayer() == null) {
            player.sendMessage(languageManager.getStringWithColor(MESSAGE_RTP + "Cancelled", "&cTéléportation aléatoire annulée par une règle ou un autre plugin."));
            return true;
        }

        Location destination = event.getLocation();
        if (!player.isOnline()) {
            return true;
        }


        double price = EconomyManager.getRtpPrice();
        if (price > 0 && !EconomyManager.pay(player, price)) {
            return true;
        }


        player.teleport(destination);
        player.sendMessage(languageManager.getStringWithColor(MESSAGE_RTP + "Teleport-success", "&aTéléportation aléatoire réussie !"));
        HomePlugin.getCacheManager().addRtpRequest(uuid, System.currentTimeMillis());
        return true;
    }

    /**
     * Searches for a random safe location within a defined radius around the base position.
     * Makes up to 100 attempts to find a location where:
     * - The block below is solid and safe
     * - The blocks at player level and above are air
     *
     * @param baseLocation The starting position (current player location)
     * @return A safe location or null if none was found after 100 attempts
     */
    @Nullable
    private Location getRandomSafeLocation(Location baseLocation) {
        final int MAX_ATTEMPTS = 100;
        final World world = baseLocation.getWorld();

        WorldBorder border = world.getWorldBorder();
        double borderCenterX = border.getCenter().getX();
        double borderCenterZ = border.getCenter().getZ();
        double borderRadius = border.getSize() / 2.0;

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {

            int[] coords = randomIntPositionInCircle(baseLocation, MAX_RADIUS);
            int randomX = coords[0];
            int randomZ = coords[1];


            if (!isWithinWorldBorder(randomX, randomZ, borderCenterX, borderCenterZ, borderRadius)) {
                continue;
            }

            Location safeLocation = findSafeLocationAtColumn(world, randomX, randomZ, baseLocation);
            if (safeLocation != null) {
                return safeLocation;
            }
        }


        return null;
    }

    /**
     * Scans a vertical column (x, z) from the top of the world down to the bottom
     * to find the first safe location for teleportation.
     *
     * Compatible with all Minecraft versions thanks to world.getMaxHeight() and getMinHeight().
     *
     * @param world The world to search in
     * @param x X coordinate of the column
     * @param z Z coordinate of the column
     * @param baseLocation Base location to retrieve yaw/pitch
     * @return A safe location or null if none is found
     */
    @Nullable
    private Location findSafeLocationAtColumn(World world, int x, int z, Location baseLocation) {
        int maxY;
        try {
              maxY = world.getMaxHeight();
          } catch (NoSuchMethodError e) {
                maxY = 255;
        }
        int minY;
        try {
           minY = world.getMinHeight();
        } catch (NoSuchMethodError e) {
            minY = 0;
        }


        for (int y = maxY; y >= minY; y--) {
            Block blockAtY = world.getBlockAt(x, y, z);


            if (!blockAtY.getType().isSolid()) {
                continue;
            }


            if (y + 2 > maxY) {
                continue;
            }

            Block blockAbove1 = world.getBlockAt(x, y + 1, z);
            Block blockAbove2 = world.getBlockAt(x, y + 2, z);


            if (checkIfMaterialIsSafe(blockAtY.getType()) &&
                    isAirBlock(blockAbove1.getType()) &&
                    isAirBlock(blockAbove2.getType())) {

                return new Location(world, x + 0.5, y + 1, z + 0.5,
                        baseLocation.getYaw(), baseLocation.getPitch());
            }
        }

        return null;
    }

    /**
     * Check if a material is air (compatible avec 1.14+)
     *
     * @param material the material to check
     * @return true if air
     */
    private boolean isAirBlock(Material material) {
        try {
            return material.isAir();
        } catch (NoSuchMethodError e) {
            return material == Material.AIR ||
                    material == Material.CAVE_AIR ||
                    material == Material.VOID_AIR;
        }
    }

    /**
     * Generates a random position in a 2D circle (uniform distribution).
     * The position is calculated around the base location on the horizontal plane (x, z).
     *
     * @param baseLocation The center position (player position)
     * @param radius The maximum radius of the circle
     * @return An array [x, z] with integer coordinates
     */
    private int[] randomIntPositionInCircle(Location baseLocation, double radius) {

        double theta = ThreadLocalRandom.current().nextDouble(0, 2 * Math.PI);


        double r = Math.sqrt(ThreadLocalRandom.current().nextDouble()) * radius;


        double offsetX = r * Math.cos(theta);
        double offsetZ = r * Math.sin(theta);

        int x = (int) Math.round(baseLocation.getX() + offsetX);
        int z = (int) Math.round(baseLocation.getZ() + offsetZ);

        return new int[] { x, z };
    }

    /**
     * Checks if a position (x, z) is inside the WorldBorder.
     *
     * @param x X coordinate to check
     * @param z Z coordinate to check
     * @param centerX WorldBorder center X
     * @param centerZ WorldBorder center Z
     * @param radius WorldBorder radius
     * @return true if the position is within the border, false otherwise
     */
    private boolean isWithinWorldBorder(int x, int z, double centerX, double centerZ, double radius) {
        return Math.abs(x - centerX) <= radius && Math.abs(z - centerZ) <= radius;
    }

    /**
     * Checks if a material is safe for teleportation.
     * Returns false for dangerous materials (lava, fire, cactus, etc.).
     *
     * @param material The material to check
     * @return true if the material is safe, false otherwise
     */
    private boolean checkIfMaterialIsSafe(Material material) {
        // Dangerous materials that cause damage or are unstable
        if (material == Material.LAVA ||
                material == Material.WATER ||
                material == Material.CACTUS ||
                material == Material.FIRE ||
                material == Material.MAGMA_BLOCK ||
                material == Material.SWEET_BERRY_BUSH ||
                material == Material.VOID_AIR ||
                material == Material.CAVE_AIR ||
                material == Material.WITHER_ROSE) {
            return false;
        }

        // Try to check newer materials (1.17+) without breaking on older versions
        try {
            if (material == Material.valueOf("SOUL_FIRE") ||
                    material == Material.valueOf("POWDER_SNOW") ||
                    material == Material.valueOf("POINTED_DRIPSTONE") ||
                    material == Material.valueOf("SCULK_SENSOR") ||
                    material == Material.valueOf("SCULK_SHRIEKER")) {
                return false;
            }
        } catch (IllegalArgumentException ignored) {
            // Material doesn't exist in this version, skip
        }

        return true;
    }
}