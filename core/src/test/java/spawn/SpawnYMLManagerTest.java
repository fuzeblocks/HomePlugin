package spawn;

import fr.fuzeblocks.homeplugin.core.spawn.yml.SpawnYMLManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

import static org.mockito.Mockito.*;

class SpawnYMLManagerTest {

    @TempDir
    Path tempDir;

    private File file;
    private World world;
    private Location location;
    private SpawnYMLManager manager;

    private UUID worldUUID;

    private MockedStatic<Bukkit> bukkit;

    @BeforeEach
    void setUp() {
        file = tempDir.resolve("spawn.yml").toFile();

        world = mock(World.class);

        worldUUID = UUID.randomUUID();

        location = new Location(
                world,
                100.0,
                64.0,
                100.0,
                0.0f,
                0.0f
        );

        when(world.getUID()).thenReturn(worldUUID);
        when(world.getName()).thenReturn("world");

        bukkit = mockStatic(Bukkit.class);

        bukkit.when(() -> Bukkit.getWorld("world"))
                .thenReturn(world);

        manager = new SpawnYMLManager(file);
    }

    @AfterEach
    void tearDown() {
        if (bukkit != null) {
            bukkit.close();
        }
    }

    @Test
    void setSpawn_ShouldReturnTrueWhenSpawnIsSet() {
        boolean result = manager.setSpawn(location);

        Assertions.assertTrue(result);

        YamlConfiguration savedYaml =
                YamlConfiguration.loadConfiguration(file);

        String key = "Spawn." + worldUUID;

        Assertions.assertTrue(savedYaml.contains(key));

        Assertions.assertEquals(100.0, savedYaml.getDouble(key + ".X"));
        Assertions.assertEquals(64.0, savedYaml.getDouble(key + ".Y"));
        Assertions.assertEquals(100.0, savedYaml.getDouble(key + ".Z"));
        Assertions.assertEquals(0.0, savedYaml.getDouble(key + ".YAW"));
        Assertions.assertEquals(0.0, savedYaml.getDouble(key + ".PITCH"));
        Assertions.assertEquals("world", savedYaml.getString(key + ".World"));
    }

    @Test
    void getSpawn_ShouldReturnNullWhenNoSpawnIsSet() {
        Location spawnLocation = manager.getSpawn(world);
        Assertions.assertNull(spawnLocation);
    }

    @Test
    void getSpawn_ShouldReturnCorrectLocationWhenSpawnIsSet() {
        manager.setSpawn(location);

        Location spawnLocation = manager.getSpawn(world);

        Assertions.assertNotNull(spawnLocation);
        Assertions.assertNotNull(spawnLocation.getWorld());
        Assertions.assertEquals(worldUUID, spawnLocation.getWorld().getUID());
        Assertions.assertEquals(location.getX(), spawnLocation.getX());
        Assertions.assertEquals(location.getY(), spawnLocation.getY());
        Assertions.assertEquals(location.getZ(), spawnLocation.getZ());
        Assertions.assertEquals(location.getYaw(), spawnLocation.getYaw());
        Assertions.assertEquals(location.getPitch(), spawnLocation.getPitch());
    }

    @Test
    void hasSpawn_ShouldReturnFalseWhenNoSpawnIsSet() {
        Assertions.assertFalse(manager.hasSpawn(world));
    }

    @Test
    void hasSpawn_ShouldReturnTrueWhenSpawnIsSet() {
        manager.setSpawn(location);
        Assertions.assertTrue(manager.hasSpawn(world));
    }

    @Test
    void removeSpawn_ShouldReturnTrueWhenSpawnIsRemoved() {
        manager.setSpawn(location);
        boolean result = manager.removeSpawn(world);

        Assertions.assertTrue(result);
        Assertions.assertFalse(manager.hasSpawn(world));

        YamlConfiguration savedYaml =
                YamlConfiguration.loadConfiguration(file);

        Assertions.assertFalse(
                savedYaml.contains("Spawn." + worldUUID)
        );
    }
}