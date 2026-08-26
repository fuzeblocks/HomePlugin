import fr.fuzeblocks.homeplugin.core.home.sql.HomeSQLManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HomeSQLManagerTest {

    private HomeSQLManager manager;

    private Player player;
    private World world;

    private UUID playerUUID;

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        playerUUID = UUID.randomUUID();

        player = mock(Player.class);
        world = mock(World.class);

        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        when(player.getUniqueId()).thenReturn(playerUUID);
        when(player.getWorld()).thenReturn(world);
        when(world.getName()).thenReturn("world");

        manager = new HomeSQLManager(connection);
    }

    // -------------------------------------------------------------------------
    // addHome()
    // -------------------------------------------------------------------------

    @Test
    void addHome_shouldCreateHome() throws SQLException {

        Location location =
                new Location(world, 100, 64, 200, 90, 45);

        when(player.getLocation()).thenReturn(location);

        String sql =
                "INSERT INTO HomePlugin (player_uuid, HOME_NAME, X, Y, Z, PITCH, YAW, WORLD) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        when(connection.prepareStatement(sql))
                .thenReturn(statement);

        when(statement.executeUpdate())
                .thenReturn(1);

        boolean result = manager.addHome(player, "maison");

        assertTrue(result);

        verify(connection).prepareStatement(sql);

        verify(statement).setString(1, playerUUID.toString());
        verify(statement).setString(2, "maison");

        verify(statement).setDouble(3, 100);
        verify(statement).setDouble(4, 64);
        verify(statement).setDouble(5, 200);

        verify(statement).setFloat(6, 45);
        verify(statement).setFloat(7, 90);

        verify(statement).setString(8, "world");

        verify(statement).executeUpdate();
    }

    // -------------------------------------------------------------------------
    // addHome() - SQLException
    // -------------------------------------------------------------------------

    @Test
    void addHome_shouldReturnFalseWhenDatabaseFails() throws SQLException {

        Location location =
                new Location(world, 100, 64, 200, 90, 45);

        when(player.getLocation()).thenReturn(location);

        String sql =
                "INSERT INTO HomePlugin (player_uuid, HOME_NAME, X, Y, Z, PITCH, YAW, WORLD) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        when(connection.prepareStatement(sql))
                .thenThrow(new SQLException("Database error"));

        boolean result = manager.addHome(player, "maison");

        assertFalse(result);
    }

    // -------------------------------------------------------------------------
    // setHome()
    // -------------------------------------------------------------------------

    @Test
    void setHome_shouldCreateHome() throws SQLException {

        Location location =
                new Location(world, 10, 70, 20, 180, 30);

        String sql =
                "INSERT INTO HomePlugin (player_uuid, HOME_NAME, X, Y, Z, PITCH, YAW, WORLD) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        when(connection.prepareStatement(sql))
                .thenReturn(statement);

        boolean result =
                manager.setHome(player, "base", location);

        assertTrue(result);

        verify(statement).setString(1, playerUUID.toString());
        verify(statement).setString(2, "base");

        verify(statement).setDouble(3, 10);
        verify(statement).setDouble(4, 70);
        verify(statement).setDouble(5, 20);

        verify(statement).setFloat(6, 30);
        verify(statement).setFloat(7, 180);

        verify(statement).setString(8, "world");

        verify(statement).executeUpdate();
    }

    // -------------------------------------------------------------------------
    // getHomeNumber()
    // -------------------------------------------------------------------------

    @Test
    void getHomeNumber_shouldReturnNumberOfHomes() throws SQLException {

        String sql =
                "SELECT COUNT(*) FROM HomePlugin WHERE player_uuid = ?";

        when(connection.prepareStatement(sql))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getInt(1))
                .thenReturn(3);

        int result = manager.getHomeNumber(player);

        assertEquals(3, result);

        verify(connection).prepareStatement(sql);
        verify(statement).setString(1, playerUUID.toString());
        verify(statement).executeQuery();
        verify(resultSet).getInt(1);
    }

    @Test
    void getHomeNumber_shouldReturnZeroWhenDatabaseFails() throws SQLException {

        String sql =
                "SELECT COUNT(*) FROM HomePlugin WHERE player_uuid = ?";

        when(connection.prepareStatement(sql))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenThrow(new SQLException("Database error"));

        int result = manager.getHomeNumber(player);

        assertEquals(0, result);
    }

    // -------------------------------------------------------------------------
    // getHomesName()
    // -------------------------------------------------------------------------

    @Test
    void getHomesName_shouldReturnAllHomeNames() throws SQLException {

        String sql =
                "SELECT HOME_NAME FROM HomePlugin WHERE player_uuid = ?";

        when(connection.prepareStatement(sql))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true, true, true, false);

        when(resultSet.getString("HOME_NAME"))
                .thenReturn("maison", "base", "spawn");

        List<String> homes =
                manager.getHomesName(player);

        assertEquals(3, homes.size());

        assertEquals(
                List.of("maison", "base", "spawn"),
                homes
        );

        verify(statement).setString(1, playerUUID.toString());
        verify(statement).executeQuery();
    }

    @Test
    void getHomesName_shouldReturnEmptyListWhenNoHomesExist()
            throws SQLException {

        String sql =
                "SELECT HOME_NAME FROM HomePlugin WHERE player_uuid = ?";

        when(connection.prepareStatement(sql))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(false);

        List<String> homes =
                manager.getHomesName(player);

        assertTrue(homes.isEmpty());
    }

    // -------------------------------------------------------------------------
    // getHomeLocation()
    // -------------------------------------------------------------------------

    @Test
    void getHomeLocation_shouldReturnHomeLocation()
            throws SQLException {

        String sql =
                "SELECT * FROM HomePlugin WHERE player_uuid = ? AND HOME_NAME = ?";

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
                .thenReturn(200.0);

        when(resultSet.getFloat("YAW"))
                .thenReturn(90.0f);

        when(resultSet.getFloat("PITCH"))
                .thenReturn(45.0f);

        // Bukkit.getWorld("world") doit retourner notre mock
        try (var mockedBukkit = mockStatic(Bukkit.class)) {

            mockedBukkit
                    .when(() -> Bukkit.getWorld("world"))
                    .thenReturn(world);

            Location result =
                    manager.getHomeLocation(player, "maison");

            assertNotNull(result);

            assertEquals(100, result.getX());
            assertEquals(64, result.getY());
            assertEquals(200, result.getZ());

            assertEquals(90, result.getYaw());
            assertEquals(45, result.getPitch());

            assertSame(world, result.getWorld());

            verify(connection).prepareStatement(sql);

            verify(statement)
                    .setString(1, playerUUID.toString());

            verify(statement)
                    .setString(2, "maison");

            verify(statement).executeQuery();
        }
    }

    @Test
    void getHomeLocation_shouldReturnNullWhenHomeDoesNotExist()
            throws SQLException {

        String sql =
                "SELECT * FROM HomePlugin WHERE player_uuid = ? AND HOME_NAME = ?";

        when(connection.prepareStatement(sql))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(false);

        Location result =
                manager.getHomeLocation(player, "inexistant");

        assertNull(result);
    }

    // -------------------------------------------------------------------------
    // exist()
    // -------------------------------------------------------------------------

    @Test
    void exist_shouldReturnTrueWhenHomeExists()
            throws SQLException {

        String sql =
                "SELECT * FROM HomePlugin WHERE player_uuid = ? AND HOME_NAME = ?";

        when(connection.prepareStatement(sql))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true);

        boolean result =
                manager.exist(player, "maison");

        assertTrue(result);

        verify(statement)
                .setString(1, playerUUID.toString());

        verify(statement)
                .setString(2, "maison");

        verify(statement).executeQuery();
    }

    @Test
    void exist_shouldReturnFalseWhenHomeDoesNotExist()
            throws SQLException {

        String sql =
                "SELECT * FROM HomePlugin WHERE player_uuid = ? AND HOME_NAME = ?";

        when(connection.prepareStatement(sql))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(false);

        boolean result =
                manager.exist(player, "maison");

        assertFalse(result);
    }

    @Test
    void exist_shouldReturnFalseWhenDatabaseFails()
            throws SQLException {

        String sql =
                "SELECT * FROM HomePlugin WHERE player_uuid = ? AND HOME_NAME = ?";

        when(connection.prepareStatement(sql))
                .thenThrow(new SQLException("Database error"));

        boolean result =
                manager.exist(player, "maison");

        assertFalse(result);
    }

    // -------------------------------------------------------------------------
    // deleteHome()
    // -------------------------------------------------------------------------

    @Test
    void deleteHome_shouldReturnTrue()
            throws SQLException {

        String sql =
                "DELETE FROM HomePlugin WHERE player_uuid = ? AND HOME_NAME = ?";

        when(connection.prepareStatement(sql))
                .thenReturn(statement);

        when(statement.executeUpdate())
                .thenReturn(1);

        boolean result =
                manager.deleteHome(player, "maison");

        assertTrue(result);

        verify(statement)
                .setString(1, playerUUID.toString());

        verify(statement)
                .setString(2, "maison");

        verify(statement).executeUpdate();
    }

    @Test
    void deleteHome_shouldReturnFalseWhenDatabaseFails()
            throws SQLException {

        String sql =
                "DELETE FROM HomePlugin WHERE player_uuid = ? AND HOME_NAME = ?";

        when(connection.prepareStatement(sql))
                .thenThrow(new SQLException("Database error"));

        boolean result =
                manager.deleteHome(player, "maison");

        assertFalse(result);
    }
}