import fr.fuzeblocks.homeplugin.core.home.yml.HomeYMLManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HomeYMLManagerTest {

    @TempDir
    Path tempDir;

    private HomeYMLManager manager;
    private Player player;
    private World world;

    private UUID playerUUID;
    private File file;

    @BeforeEach
    void setUp() {
        playerUUID = UUID.randomUUID();

        file = tempDir.resolve("homes.yml").toFile();

        player = mock(Player.class);
        world = mock(World.class);

        when(player.getUniqueId()).thenReturn(playerUUID);
        when(player.getWorld()).thenReturn(world);
        when(world.getName()).thenReturn("world");

        manager = new HomeYMLManager(file);
    }

    // -------------------------------------------------------------------------
    // addHome()
    // -------------------------------------------------------------------------

    @Test
    void addHome_shouldCreateHome() {
        Location location = new Location(world, 100, 64, 200, 90, 45);

        when(player.getLocation()).thenReturn(location);

        boolean result = manager.addHome(player, "maison");

        assertTrue(result);
        assertTrue(manager.exist(player, "maison"));
        assertEquals(1, manager.getHomeNumber(player));
    }

    @Test
    void addHome_shouldReturnFalseIfHomeAlreadyExists() {
        Location location = new Location(world, 100, 64, 200, 90, 45);

        when(player.getLocation()).thenReturn(location);

        assertTrue(manager.addHome(player, "maison"));

        boolean result = manager.addHome(player, "maison");

        assertFalse(result);
        assertEquals(1, manager.getHomeNumber(player));
    }

    // -------------------------------------------------------------------------
    // setHome()
    // -------------------------------------------------------------------------

    @Test
    void setHome_shouldCreateHome() {
        Location location = new Location(world, 10, 70, 20, 180, 30);

        boolean result = manager.setHome(player, "base", location);

        assertTrue(result);
        assertTrue(manager.exist(player, "base"));
    }

    @Test
    void setHome_shouldReturnFalseIfHomeAlreadyExists() {
        Location firstLocation = new Location(world, 10, 70, 20);
        Location secondLocation = new Location(world, 50, 80, 60);

        assertTrue(manager.setHome(player, "base", firstLocation));

        boolean result = manager.setHome(player, "base", secondLocation);

        assertFalse(result);
    }

    // -------------------------------------------------------------------------
    // exist()
    // -------------------------------------------------------------------------

    @Test
    void exist_shouldReturnTrueForExistingHome() {
        Location location = new Location(world, 10, 64, 20);

        manager.setHome(player, "maison", location);

        assertTrue(manager.exist(player, "maison"));
    }

    @Test
    void exist_shouldReturnFalseForNonExistingHome() {
        assertFalse(manager.exist(player, "maison"));
    }

    // -------------------------------------------------------------------------
    // getHomeNumber()
    // -------------------------------------------------------------------------

    @Test
    void getHomeNumber_shouldReturnZeroWhenPlayerHasNoHomes() {
        assertEquals(0, manager.getHomeNumber(player));
    }

    @Test
    void getHomeNumber_shouldReturnCorrectNumber() {
        manager.setHome(player, "maison", new Location(world, 10, 64, 20));
        manager.setHome(player, "base", new Location(world, 30, 70, 40));
        manager.setHome(player, "spawn", new Location(world, 50, 80, 60));

        assertEquals(3, manager.getHomeNumber(player));
    }

    // -------------------------------------------------------------------------
    // getHomesName()
    // -------------------------------------------------------------------------

    @Test
    void getHomesName_shouldReturnEmptyListWhenNoHomesExist() {
        List<String> homes = manager.getHomesName(player);

        assertTrue(homes.isEmpty());
    }

    @Test
    void getHomesName_shouldReturnAllHomeNames() {
        manager.setHome(player, "maison", new Location(world, 10, 64, 20));
        manager.setHome(player, "base", new Location(world, 30, 70, 40));
        manager.setHome(player, "spawn", new Location(world, 50, 80, 60));

        List<String> homes = manager.getHomesName(player);

        assertEquals(3, homes.size());
        assertTrue(homes.contains("maison"));
        assertTrue(homes.contains("base"));
        assertTrue(homes.contains("spawn"));
    }

    // -------------------------------------------------------------------------
    // getHomeLocation()
    // -------------------------------------------------------------------------

    @Test
    void getHomeLocation_shouldReturnHomeLocation() {
        Location expected = new Location(world, 100, 64, 200, 90, 45);

        manager.setHome(player, "maison", expected);


        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        String key = playerUUID + ".Home.maison";

        assertEquals(100, yaml.getDouble(key + ".X"));
        assertEquals(64, yaml.getDouble(key + ".Y"));
        assertEquals(200, yaml.getDouble(key + ".Z"));
        assertEquals(90, yaml.getDouble(key + ".YAW"));
        assertEquals(45, yaml.getDouble(key + ".PITCH"));
        assertEquals("world", yaml.getString(key + ".World"));
    }

    @Test
    void getHomeLocation_shouldReturnNullForNonExistingHome() {
        Location result = manager.getHomeLocation(player, "inexistant");

        assertNull(result);
    }

    // -------------------------------------------------------------------------
    // relocateHome()
    // -------------------------------------------------------------------------

    @Test
    void relocateHome_shouldUpdateLocation() {
        Location initial = new Location(world, 10, 64, 20);
        Location newLocation = new Location(world, 100, 80, 200, 90, 45);

        manager.setHome(player, "maison", initial);

        boolean result = manager.relocateHome(player, "maison", newLocation);

        assertTrue(result);

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        String key = playerUUID + ".Home.maison";

        assertEquals(100, yaml.getDouble(key + ".X"));
        assertEquals(80, yaml.getDouble(key + ".Y"));
        assertEquals(200, yaml.getDouble(key + ".Z"));
        assertEquals(90, yaml.getDouble(key + ".YAW"));
        assertEquals(45, yaml.getDouble(key + ".PITCH"));
    }

    @Test
    void relocateHome_shouldReturnFalseForNonExistingHome() {
        Location location = new Location(world, 100, 80, 200);

        boolean result = manager.relocateHome(player, "inexistant", location);

        assertFalse(result);
    }

    // -------------------------------------------------------------------------
    // renameHome()
    // -------------------------------------------------------------------------

    @Test
    void renameHome_shouldRenameExistingHome() {
        Location location = new Location(world, 100, 64, 200, 90, 45);

        manager.setHome(player, "ancienne", location);

        boolean result = manager.renameHome(
                player,
                "ancienne",
                "nouvelle"
        );

        assertTrue(result);

        assertFalse(manager.exist(player, "ancienne"));
        assertTrue(manager.exist(player, "nouvelle"));
        assertEquals(1, manager.getHomeNumber(player));
    }

    @Test
    void renameHome_shouldReturnFalseIfOldHomeDoesNotExist() {
        boolean result = manager.renameHome(
                player,
                "inexistante",
                "nouvelle"
        );

        assertFalse(result);
    }

    @Test
    void renameHome_shouldReturnFalseIfNewNameAlreadyExists() {
        Location location1 = new Location(world, 10, 64, 20);
        Location location2 = new Location(world, 30, 70, 40);

        manager.setHome(player, "maison", location1);
        manager.setHome(player, "base", location2);

        boolean result = manager.renameHome(
                player,
                "maison",
                "base"
        );

        assertFalse(result);

        assertTrue(manager.exist(player, "maison"));
        assertTrue(manager.exist(player, "base"));
    }

    // -------------------------------------------------------------------------
    // deleteHome()
    // -------------------------------------------------------------------------

    @Test
    void deleteHome_shouldDeleteExistingHome() {
        Location location = new Location(world, 100, 64, 200);

        manager.setHome(player, "maison", location);

        boolean result = manager.deleteHome(player, "maison");

        assertTrue(result);
        assertFalse(manager.exist(player, "maison"));
        assertEquals(0, manager.getHomeNumber(player));
    }

    @Test
    void deleteHome_shouldReturnFalseForNonExistingHome() {
        boolean result = manager.deleteHome(player, "inexistant");

        assertFalse(result);
    }

    // -------------------------------------------------------------------------
    // getHomesLocation()
    // -------------------------------------------------------------------------

    @Test
    void getHomesLocation_shouldReturnEmptyListWhenNoHomesExist() {
        List<Location> homes = manager.getHomesLocation(player);

        assertTrue(homes.isEmpty());
    }

    // -------------------------------------------------------------------------
    // Fichier YAML
    // -------------------------------------------------------------------------

    @Test
    void homesShouldBePersistedToFile() {
        Location location = new Location(world, 100, 64, 200);

        manager.setHome(player, "maison", location);

        assertTrue(file.exists());

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        String key = playerUUID + ".Home.maison";

        assertTrue(yaml.contains(key));
        assertEquals(100, yaml.getDouble(key + ".X"));
        assertEquals(64, yaml.getDouble(key + ".Y"));
        assertEquals(200, yaml.getDouble(key + ".Z"));
    }
}