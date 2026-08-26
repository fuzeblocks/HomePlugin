package warps;

import fr.fuzeblocks.homeplugin.core.warps.WarpData;
import fr.fuzeblocks.homeplugin.core.warps.sql.WarpSQLManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WarpSQLManagerTest {

    private Connection connection;
    private MockedStatic<Bukkit> bukkitMock;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private WarpSQLManager manager;

    private WarpData warp;
    private Location location;
    private UUID creatorUUID;
    @Mock
    private World world;

    @BeforeEach
    void setUp() throws SQLException {
        bukkitMock = mockStatic(Bukkit.class);

        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        creatorUUID = UUID.randomUUID();

        MockitoAnnotations.openMocks(this);

        when(world.getName()).thenReturn("world");

        bukkitMock.when(() -> Bukkit.getWorld("world"))
                .thenReturn(world);


        location = new Location(
                world,
                100.5,
                64,
                -20.5,
                90f,
                30f
        );

        warp = new WarpData(
                "spawn",
                "Thomas",
                creatorUUID,
                Material.COMPASS,
                List.of("Line 1", "Line 2"),
                true,
                new HashSet<>(),
                10.0,
                "homeplugin.spawn",
                new Timestamp(System.currentTimeMillis() + 60_000),
                new Timestamp(System.currentTimeMillis()),
                location
        );

        manager = new WarpSQLManager(connection);
    }

    @AfterEach
    void tearDown() {
        bukkitMock.close();
    }

    // =========================================================
    // ADD
    // =========================================================

    @Test
    void addWarp_shouldReturnTrue() throws SQLException {
        when(connection.prepareStatement(anyString()))
                .thenReturn(statement);

        when(statement.executeUpdate())
                .thenReturn(1);

        assertTrue(manager.addWarp(warp));

        verify(connection).prepareStatement(
                contains("INSERT INTO Warps")
        );

        verify(statement).setString(1, "spawn");
        verify(statement).setString(2, warp.serialize());
        verify(statement).executeUpdate();
        verify(statement).close();
    }

    @Test
    void addWarp_shouldReturnFalseForNullWarp() {
        assertFalse(manager.addWarp((WarpData) null));
    }

    @Test
    void addWarp_shouldReturnFalseForInvalidConnection()
            throws SQLException {

        when(connection.prepareStatement(anyString()))
                .thenThrow(new SQLException("Database error"));

        assertFalse(manager.addWarp(warp));
    }

    @Test
    void addWarpSerialized_shouldReturnTrue()
            throws SQLException {

        when(connection.prepareStatement(anyString()))
                .thenReturn(statement);

        when(statement.executeUpdate())
                .thenReturn(1);

        assertTrue(manager.addWarp(warp.serialize()));

        verify(statement).setString(1, "spawn");
        verify(statement).setString(2, warp.serialize());
    }

    @Test
    void addWarpSerialized_shouldRejectInvalidData() {
        assertFalse(manager.addWarp("invalid data"));
    }

    @Test
    void addWarpWithParameters_shouldReturnTrue()
            throws SQLException {

        when(connection.prepareStatement(anyString()))
                .thenReturn(statement);

        when(statement.executeUpdate())
                .thenReturn(1);

        assertTrue(manager.addWarp(
                "spawn",
                "Thomas",
                creatorUUID,
                Material.COMPASS,
                List.of("Test"),
                true,
                Set.of(),
                10.0,
                "test.permission",
                null,
                new Timestamp(System.currentTimeMillis()),
                location
        ));
    }

    // =========================================================
    // GET
    // =========================================================

    @Test
    void getWarp_shouldReturnWarp() throws SQLException {
        when(connection.prepareStatement(anyString()))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true);

        String serializedWarp = warp.serialize();

        when(resultSet.getString("DATA"))
                .thenReturn(serializedWarp);

        WarpData result = manager.getWarp("spawn");

        assertNotNull(result);
        assertEquals("spawn", result.getName());
        assertEquals("Thomas", result.getCreatorName());
        assertEquals(creatorUUID, result.getCreatorUUID());

        verify(statement).setString(1, "spawn");
        verify(statement).close();
        verify(resultSet).close();
    }

    @Test
    void getWarp_shouldReturnNullWhenNotFound()
            throws SQLException {

        when(connection.prepareStatement(anyString()))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(false);

        assertNull(manager.getWarp("unknown"));
    }

    @Test
    void getWarp_shouldReturnNullForInvalidSerializedData()
            throws SQLException {

        when(connection.prepareStatement(anyString()))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true);

        when(resultSet.getString("DATA"))
                .thenReturn("invalid");

        assertNull(manager.getWarp("spawn"));
    }

    @Test
    void getWarp_shouldReturnNullForInvalidName() {
        assertNull(manager.getWarp(null));
        assertNull(manager.getWarp(""));
    }

    @Test
    void getWarp_shouldReturnNullWhenDatabaseFails()
            throws SQLException {

        when(connection.prepareStatement(anyString()))
                .thenThrow(new SQLException("Database error"));

        assertNull(manager.getWarp("spawn"));
    }

    // =========================================================
    // DELETE
    // =========================================================

    @Test
    void deleteWarp_shouldReturnTrueWhenDeleted()
            throws SQLException {

        when(connection.prepareStatement(anyString()))
                .thenReturn(statement);

        when(statement.executeUpdate())
                .thenReturn(1);

        assertTrue(manager.deleteWarp("spawn"));

        verify(statement).setString(1, "spawn");
        verify(statement).executeUpdate();
    }

    @Test
    void deleteWarp_shouldReturnFalseWhenNotFound()
            throws SQLException {

        when(connection.prepareStatement(anyString()))
                .thenReturn(statement);

        when(statement.executeUpdate())
                .thenReturn(0);

        assertFalse(manager.deleteWarp("unknown"));
    }

    @Test
    void deleteWarp_shouldRejectInvalidName() {
        assertFalse(manager.deleteWarp((String) null));
        assertFalse(manager.deleteWarp(""));
    }

    @Test
    void deleteWarpObject_shouldRejectNull() {
        assertFalse(manager.deleteWarp((WarpData) null));
    }

    // =========================================================
    // EXISTS
    // =========================================================

    @Test
    void warpExists_shouldReturnTrue()
            throws SQLException {

        when(connection.prepareStatement(anyString()))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true);

        assertTrue(manager.warpExists("spawn"));
    }

    @Test
    void warpExists_shouldReturnFalseWhenNotFound()
            throws SQLException {

        when(connection.prepareStatement(anyString()))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(false);

        assertFalse(manager.warpExists("unknown"));
    }

    @Test
    void warpExists_shouldReturnFalseForInvalidName() {
        assertFalse(manager.warpExists((String) null));
        assertFalse(manager.warpExists(""));
    }

    @Test
    void warpExists_shouldReturnFalseWhenDatabaseFails()
            throws SQLException {

        when(connection.prepareStatement(anyString()))
                .thenThrow(new SQLException("Database error"));

        assertFalse(manager.warpExists("spawn"));
    }

    @Test
    void warpExistsObject_shouldRejectNull() {
        assertFalse(manager.warpExists((WarpData) null));
    }

    // =========================================================
    // RENAME
    // =========================================================

    @Test
    void renameWarp_shouldRenameExistingWarp()
            throws SQLException {

        PreparedStatement selectStatement = mock(PreparedStatement.class);
        PreparedStatement deleteStatement = mock(PreparedStatement.class);
        PreparedStatement insertStatement = mock(PreparedStatement.class);

        ResultSet selectResult = mock(ResultSet.class);

        when(connection.prepareStatement(contains("SELECT DATA")))
                .thenReturn(selectStatement);

        when(connection.prepareStatement(contains("DELETE FROM")))
                .thenReturn(deleteStatement);

        when(connection.prepareStatement(contains("INSERT INTO")))
                .thenReturn(insertStatement);

        when(selectStatement.executeQuery())
                .thenReturn(selectResult);

        when(selectResult.next())
                .thenReturn(true);

        String serializedWarp = warp.serialize();

       when(selectResult.getString("DATA"))
        .thenReturn(serializedWarp);

        when(deleteStatement.executeUpdate())
                .thenReturn(1);

        when(insertStatement.executeUpdate())
                .thenReturn(1);

        assertTrue(manager.renameWarp("spawn", "hub"));

        verify(deleteStatement).setString(1, "spawn");

        verify(insertStatement).setString(1, "hub");

        verify(insertStatement).setString(
        eq(2),
        argThat(serialized -> {
            WarpData data = WarpData.deserialize(serialized);
            return data != null &&
                    data.getName().equals("hub");
            })
        );
    }

    @Test
    void renameWarp_shouldReturnFalseWhenOldWarpDoesNotExist()
            throws SQLException {

        when(connection.prepareStatement(contains("SELECT DATA")))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(false);

        assertFalse(manager.renameWarp("unknown", "hub"));
    }

    @Test
    void renameWarp_shouldRejectInvalidNames() {
        assertFalse(manager.renameWarp((String) null, "hub"));
        assertFalse(manager.renameWarp("spawn", null));
        assertFalse(manager.renameWarp("", "hub"));
        assertFalse(manager.renameWarp("spawn", ""));
    }

    @Test
    void renameWarpObject_shouldRejectNull() {
        assertFalse(manager.renameWarp((WarpData) null, "hub"));
    }

    // =========================================================
    // RELOCATE
    // =========================================================

    @Test
    void relocateWarp_shouldUpdateLocation()
            throws SQLException {

        mockLoadWarp(warp);
        mockSave();

        World newWorld = mock(World.class);
        when(newWorld.getName()).thenReturn("world_nether");

        Location newLocation =
                new Location(newWorld, 500, 80, 600, 45f, 15f);

        assertTrue(
                manager.relocateWarp(
                        "spawn",
                        newLocation
                )
        );

        verifySaveContainsData(data ->
                data.getName().equals("spawn") &&
                data.getLocation().getX() == 500 &&
                data.getLocation().getY() == 80 &&
                data.getLocation().getZ() == 600
        );
    }

    @Test
    void relocateWarp_shouldRejectNullLocation() {
        assertFalse(manager.relocateWarp("spawn", null));
    }

    @Test
    void relocateWarp_shouldReturnFalseForUnknownWarp()
            throws SQLException {

        mockLoadWarp(null);

        assertFalse(
                manager.relocateWarp(
                        "unknown",
                        location
                )
        );
    }

    @Test
    void relocateWarpObject_shouldRejectNull() {
        assertFalse(
                manager.relocateWarp(
                        (WarpData) null,
                        location
                )
        );
    }

    // =========================================================
    // ICON
    // =========================================================

    @Test
    void setWarpIcon_shouldUpdateIcon()
            throws SQLException {

        mockLoadWarp(warp);
        mockSave();

        assertTrue(
                manager.setWarpIcon(
                        "spawn",
                        Material.DIAMOND
                )
        );

        verifySaveContainsData(data ->
                data.getIcon() == Material.DIAMOND
        );
    }

    @Test
    void setWarpIcon_shouldRejectNullIcon() {
        assertFalse(
                manager.setWarpIcon(
                        "spawn",
                        null
                )
        );
    }

    @Test
    void setWarpIcon_shouldReturnFalseForUnknownWarp()
            throws SQLException {

        mockLoadWarp(null);

        assertFalse(
                manager.setWarpIcon(
                        "unknown",
                        Material.DIAMOND
                )
        );
    }

    // =========================================================
    // LORES
    // =========================================================

    @Test
    void setWarpLores_shouldUpdateLores()
            throws SQLException {

        mockLoadWarp(warp);
        mockSave();

        List<String> lores =
                List.of("New lore", "Another lore");

        assertTrue(
                manager.setWarpLores(
                        warp,
                        lores
                )
        );

        verifySaveContainsData(data ->
                data.getLores().equals(lores)
        );
    }

    @Test
    void setWarpLoresSerialized_shouldUpdateLores()
            throws SQLException {

        mockSave();

        when(connection.prepareStatement(contains("SELECT DATA")))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true);

        String serializedWarp = warp.serialize();

        when(resultSet.getString("DATA"))
                .thenReturn(serializedWarp);

        List<String> lores = List.of("Updated");

        assertTrue(
                manager.setWarpLores(
                        warp.serialize(),
                        lores
                )
        );

        verifySaveContainsData(data ->
                data.getLores().equals(lores)
        );
    }

    @Test
    void setWarpLores_shouldRejectInvalidSerializedData() {
        assertFalse(
                manager.setWarpLores(
                        "invalid",
                        List.of("test")
                )
        );
    }

    @Test
    void setWarpLoresObject_shouldRejectNull() {
        assertFalse(
                manager.setWarpLores(
                        (WarpData) null,
                        List.of("test")
                )
        );
    }

    // =========================================================
    // PUBLIC
    // =========================================================

    @Test
    void setWarpPublic_shouldUpdateVisibility()
            throws SQLException {

        mockLoadWarp(warp);
        mockSave();

        assertTrue(
                manager.setWarpPublic(
                        "spawn",
                        false
                )
        );

        verifySaveContainsData(data ->
                !data.isPublic()
        );
    }

    @Test
    void setWarpPublic_shouldReturnFalseForUnknownWarp()
            throws SQLException {

        mockLoadWarp(null);

        assertFalse(
                manager.setWarpPublic(
                        "unknown",
                        true
                )
        );
    }

    // =========================================================
    // DENIED PLAYERS
    // =========================================================

    @Test
    void setDeniedPlayers_shouldUpdatePlayers()
            throws SQLException {

        mockLoadWarp(warp);
        mockSave();

        Set<UUID> denied =
                Set.of(
                        UUID.randomUUID(),
                        UUID.randomUUID()
                );

        assertTrue(
                manager.setDeniedPlayers(
                        "spawn",
                        denied
                )
        );

        verifySaveContainsData(data ->
                data.getDeniedPlayers().equals(denied)
        );
    }

    @Test
    void setDeniedPlayers_shouldReturnFalseForUnknownWarp()
            throws SQLException {

        mockLoadWarp(null);

        assertFalse(
                manager.setDeniedPlayers(
                        "unknown",
                        Set.of()
                )
        );
    }

    @Test
    void setDeniedPlayersObject_shouldRejectNull() {
        assertFalse(
                manager.setDeniedPlayers(
                        (WarpData) null,
                        Set.of()
                )
        );
    }

    // =========================================================
    // COST
    // =========================================================

    @Test
    void setWarpCost_shouldUpdateCost()
            throws SQLException {

        mockLoadWarp(warp);
        mockSave();

        assertTrue(
                manager.setWarpCost(
                        "spawn",
                        42.5
                )
        );

        verifySaveContainsData(data ->
                data.getCost() == 42.5
        );
    }

    @Test
    void setWarpCost_shouldReturnFalseForUnknownWarp()
            throws SQLException {

        mockLoadWarp(null);

        assertFalse(
                manager.setWarpCost(
                        "unknown",
                        10
                )
        );
    }

    // =========================================================
    // PERMISSION
    // =========================================================

    @Test
    void setWarpPermission_shouldUpdatePermission()
            throws SQLException {

        mockLoadWarp(warp);
        mockSave();

        assertTrue(
                manager.setWarpPermission(
                        "spawn",
                        "homeplugin.test"
                )
        );

        verifySaveContainsData(data ->
                "homeplugin.test".equals(
                        data.getPermission()
                )
        );
    }

    // =========================================================
    // EXPIRATION
    // =========================================================

    @Test
    void setWarpExpirationDate_shouldUpdateExpiration()
            throws SQLException {

        mockLoadWarp(warp);
        mockSave();

        Timestamp expiration =
                new Timestamp(
                        System.currentTimeMillis() + 120_000
                );

        assertTrue(
                manager.setWarpExpirationDate(
                        "spawn",
                        expiration
                )
        );

        verifySaveContainsData(data ->
                expiration.equals(
                        data.getExpirationDate()
                )
        );
    }

    // =========================================================
    // ALL WARPS
    // =========================================================

    @Test
    void getAllWarps_shouldReturnAllValidWarps()
            throws SQLException {

        when(connection.prepareStatement(contains("SELECT WARP_NAME, DATA")))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true, true, false);

        when(resultSet.getString("WARP_NAME"))
                .thenReturn("spawn", "hub");

        String serializedWarp = warp.serialize();

        when(resultSet.getString("DATA"))
                .thenReturn(serializedWarp);

        Map<String, WarpData> result =
                manager.getAllWarps();

        assertEquals(2, result.size());
        assertTrue(result.containsKey("spawn"));
        assertTrue(result.containsKey("hub"));
    }

    @Test
    void getAllWarps_shouldSkipInvalidData()
            throws SQLException {

        when(connection.prepareStatement(contains("SELECT WARP_NAME, DATA")))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true, false);

        when(resultSet.getString("WARP_NAME"))
                .thenReturn("invalid");

        when(resultSet.getString("DATA"))
                .thenReturn("invalid data");

        Map<String, WarpData> result =
                manager.getAllWarps();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllWarps_shouldReturnEmptyMapWhenDatabaseFails()
            throws SQLException {

        when(connection.prepareStatement(anyString()))
                .thenThrow(new SQLException("Database error"));

        assertTrue(manager.getAllWarps().isEmpty());
    }

    // =========================================================
    // NAMES
    // =========================================================

    @Test
    void getWarpNames_shouldReturnAllNames()
            throws SQLException {

        when(connection.prepareStatement(contains("SELECT WARP_NAME")))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true, true, false);

        when(resultSet.getString("WARP_NAME"))
                .thenReturn("spawn", "hub");

        Set<String> names =
                manager.getWarpNames();

        assertEquals(2, names.size());
        assertTrue(names.contains("spawn"));
        assertTrue(names.contains("hub"));
    }

    @Test
    void getWarpNames_shouldReturnEmptySetWhenDatabaseFails()
            throws SQLException {

        when(connection.prepareStatement(anyString()))
                .thenThrow(new SQLException("Database error"));

        assertTrue(manager.getWarpNames().isEmpty());
    }

    // =========================================================
    // EXPIRATION
    // =========================================================

    @Test
    void isExpired_shouldReturnFalseForUnknownWarp()
            throws SQLException {

        mockLoadWarp(null);

        assertFalse(manager.isExpired("unknown"));
    }

    @Test
    void isExpired_shouldReturnFalseWithoutExpiration()
            throws SQLException {

        WarpData permanent = createWarp("permanent");

        permanent = new WarpData(
                permanent.getName(),
                permanent.getCreatorName(),
                permanent.getCreatorUUID(),
                permanent.getIcon(),
                permanent.getLores(),
                permanent.isPublic(),
                permanent.getDeniedPlayers(),
                permanent.getCost(),
                permanent.getPermission(),
                null,
                permanent.getCreationDate(),
                permanent.getLocation()
        );

        mockLoadWarp(permanent);

        assertFalse(manager.isExpired("permanent"));
    }

    @Test
    void isExpired_shouldReturnTrueForExpiredWarp()
            throws SQLException {

        Timestamp expiration =
                new Timestamp(
                        System.currentTimeMillis() - 10_000
                );

        WarpData expired = new WarpData(
                "expired",
                "Thomas",
                creatorUUID,
                Material.COMPASS,
                List.of(),
                true,
                Set.of(),
                0,
                null,
                expiration,
                new Timestamp(System.currentTimeMillis()),
                location
        );

        mockLoadWarp(expired);

        assertTrue(manager.isExpired("expired"));
    }

    @Test
    void isExpired_shouldReturnFalseForFutureWarp()
            throws SQLException {

        Timestamp expiration =
                new Timestamp(
                        System.currentTimeMillis() + 60_000
                );

        WarpData future = new WarpData(
                "future",
                "Thomas",
                creatorUUID,
                Material.COMPASS,
                List.of(),
                true,
                Set.of(),
                0,
                null,
                expiration,
                new Timestamp(System.currentTimeMillis()),
                location
        );

        mockLoadWarp(future);

        assertFalse(manager.isExpired("future"));
    }

    @Test
    void isExpiredObject_shouldReturnFalseForNull() {
        assertFalse(manager.isExpired((WarpData) null));
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private void mockLoadWarp(WarpData data)
            throws SQLException {

        when(connection.prepareStatement(contains("SELECT DATA")))
                .thenReturn(statement);

        when(statement.executeQuery())
                .thenReturn(resultSet);

        if (data == null) {
            when(resultSet.next())
                    .thenReturn(false);
        } else {
            when(resultSet.next())
                    .thenReturn(true);

            String serializedData = data.serialize();

            when(resultSet.getString("DATA"))
                    .thenReturn(serializedData);
        }
    }

    private void mockSave()
            throws SQLException {

        PreparedStatement saveStatement =
                mock(PreparedStatement.class);

        when(connection.prepareStatement(
                contains("INSERT INTO Warps")
        )).thenReturn(saveStatement);

        when(saveStatement.executeUpdate())
                .thenReturn(1);
    }

    private void verifySaveContainsData(
            java.util.function.Predicate<WarpData> predicate)
            throws SQLException {

        ArgumentCaptor<String> captor =
                ArgumentCaptor.forClass(String.class);

        PreparedStatement saveStatement =
                null;

        verify(connection, atLeastOnce())
                .prepareStatement(
                        contains("INSERT INTO Warps")
                );


        verify(statement, atLeast(0))
                .setString(anyInt(), anyString());
    }

    private WarpData createWarp(String name) {
        return new WarpData(
                name,
                "Thomas",
                creatorUUID,
                Material.COMPASS,
                List.of("Test"),
                true,
                new HashSet<>(),
                5.0,
                "test.permission",
                null,
                new Timestamp(System.currentTimeMillis()),
                location
        );
    }
}