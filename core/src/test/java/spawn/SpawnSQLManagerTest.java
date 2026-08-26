package spawn;

import fr.fuzeblocks.homeplugin.core.spawn.sql.SpawnSQLManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class SpawnSQLManagerTest {

    private Player player;
    private World world;
    private Location location;
    private SpawnSQLManager manager;

    private UUID worldUUID;
    private UUID playerUUID;

    private MockedStatic<Bukkit> bukkit;

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() {
        player = mock(Player.class);
        world = mock(World.class);

        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        bukkit = mockStatic(Bukkit.class);

        playerUUID = UUID.randomUUID();
        worldUUID = UUID.randomUUID();

        location = new Location(
                world,
                100.0,
                64.0,
                100.0,
                0.0f,
                0.0f
        );

        when(player.getUniqueId()).thenReturn(playerUUID);
        when(player.getWorld()).thenReturn(world);
        when(player.getLocation()).thenReturn(location);

        when(world.getUID()).thenReturn(worldUUID);
        when(world.getName()).thenReturn("world");

        bukkit.when(() -> Bukkit.getWorld("world"))
                .thenReturn(world);

        bukkit.when(() -> Bukkit.getWorld(worldUUID))
                .thenReturn(world);

        bukkit.when(() -> Bukkit.getPlayer(playerUUID))
                .thenReturn(player);

        manager = new SpawnSQLManager(connection);
    }

    @AfterEach
    void tearDown() {
        if (bukkit != null) {
            bukkit.close();
        }
    }

    @Test
    void setSpawn_ShouldReturnTrueWhenSpawnIsSet() throws SQLException {
        String sql =
                "INSERT INTO SpawnPlugin (X, Y, Z, YAW, PITCH, WORLD) VALUES (?, ?, ?, ?, ?, ?)";

        when(connection.prepareStatement(sql))
                .thenReturn(statement);

        when(statement.executeUpdate())
                .thenReturn(1);

        Assertions.assertTrue(manager.setSpawn(location));

        verify(connection).prepareStatement(sql);

        verify(statement).setDouble(1, 100.0);
        verify(statement).setDouble(2, 64.0);
        verify(statement).setDouble(3, 100.0);
        verify(statement).setFloat(4, 0.0f);
        verify(statement).setFloat(5, 0.0f);
        verify(statement).setString(6, "world");

        verify(statement).executeUpdate();
    }

    @Test
    void setSpawn_ShouldReturnFalseWhenSQLExceptionOccurs() throws SQLException {
        String sql =
                "INSERT INTO SpawnPlugin (X, Y, Z, YAW, PITCH, WORLD) VALUES (?, ?, ?, ?, ?, ?)";

        when(connection.prepareStatement(sql))
                .thenThrow(new SQLException());

        Assertions.assertFalse(manager.setSpawn(location));
    }

    @Test
    void getSpawn_ShouldReturnNullWhenNoSpawnIsSet() throws SQLException {
        String sql =
                "SELECT * FROM `SpawnPlugin` WHERE `WORLD` = ?";

        when(connection.prepareStatement(sql))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(false);

        Assertions.assertNull(manager.getSpawn(world));

        verify(statement).setString(1, "world");
        verify(statement).executeQuery();
    }

    @Test
    void getSpawn_ShouldReturnCorrectLocationWhenSpawnIsSet() throws SQLException {
        String sql =
                "SELECT * FROM `SpawnPlugin` WHERE `WORLD` = ?";

        when(connection.prepareStatement(sql))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getString("WORLD"))
                .thenReturn("world");

        when(resultSet.getDouble("X"))
                .thenReturn(100.0);

        when(resultSet.getDouble("Y"))
                .thenReturn(64.0);

        when(resultSet.getDouble("Z"))
                .thenReturn(100.0);

        when(resultSet.getFloat("YAW"))
                .thenReturn(0.0f);

        when(resultSet.getFloat("PITCH"))
                .thenReturn(0.0f);

        Location spawnLocation = manager.getSpawn(world);

        Assertions.assertNotNull(spawnLocation);
        Assertions.assertNotNull(spawnLocation.getWorld());

        Assertions.assertEquals(worldUUID, spawnLocation.getWorld().getUID());
        Assertions.assertEquals(100.0, spawnLocation.getX());
        Assertions.assertEquals(64.0, spawnLocation.getY());
        Assertions.assertEquals(100.0, spawnLocation.getZ());
        Assertions.assertEquals(0.0f, spawnLocation.getYaw());
        Assertions.assertEquals(0.0f, spawnLocation.getPitch());

        verify(statement).setString(1, "world");
        verify(statement).executeQuery();
    }

    @Test
    void hasSpawn_ShouldReturnFalseWhenNoSpawnIsSet() throws SQLException {
        when(connection.prepareStatement(
                "SELECT * FROM `SpawnPlugin` WHERE `WORLD` = ?"
        )).thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(false);

        Assertions.assertFalse(manager.hasSpawn(world));
    }

    @Test
    void hasSpawn_ShouldReturnTrueWhenSpawnIsSet() throws SQLException {
        when(connection.prepareStatement(
                "SELECT * FROM `SpawnPlugin` WHERE `WORLD` = ?"
        )).thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getString("WORLD"))
                .thenReturn("world");

        Assertions.assertTrue(manager.hasSpawn(world));
    }

    @Test
    void removeSpawn_ShouldReturnTrueWhenSpawnIsRemoved() throws SQLException {
        String sql =
                "DELETE FROM `SpawnPlugin` WHERE `WORLD` = ?";

        when(connection.prepareStatement(sql))
                .thenReturn(statement);

        when(statement.executeUpdate())
                .thenReturn(1);

        Assertions.assertTrue(manager.removeSpawn(world));

        verify(connection).prepareStatement(sql);
        verify(statement).setString(1, "world");
        verify(statement).executeUpdate();
    }

    @Test
    void removeSpawn_ShouldReturnFalseWhenSQLExceptionOccurs() throws SQLException {
        String sql =
                "DELETE FROM `SpawnPlugin` WHERE `WORLD` = ?";

        when(connection.prepareStatement(sql))
                .thenThrow(new SQLException());

        Assertions.assertFalse(manager.removeSpawn(world));
    }

}