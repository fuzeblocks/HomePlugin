package fr.fuzeblocks.homeplugin.database;

/**
 * The type Database credentials.
 */
public class DatabaseCredentials {
    private final String host;
    private final String user;
    private final String pass;
    private final String database;
    private final int port;

    /**
     * Instantiates a new Database credentials.
     *
     * @param host     the host
     * @param user     the user
     * @param pass     the pass
     * @param database the database
     * @param port     the port
     */
    public DatabaseCredentials(String host, String user, String pass, String database, int port) {
        System.out.println("DatabaseCredentials");
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.database = database;
        this.port = port;
    }

    /**
     * To uri string.
     *
     * @return the string
     */
    public String toURI() {
        String builder = "jdbc:mysql://" +
                host +
                ":" +
                port +
                "/" +
                database +
                "?autoReconnect=true";

        return builder;
    }

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * Gets pass.
     *
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * Gets database.
     *
     * @return the database
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }
}
