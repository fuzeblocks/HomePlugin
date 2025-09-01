package fr.fuzeblocks.homeplugin.database;

public class DatabaseCredentials {
    private final String host;
    private final String user;
    private final String pass;
    private final String database;
    private final int port;

    public DatabaseCredentials(String host, String user, String pass, String database, int port) {
        System.out.println("DatabaseCredentials");
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.database = database;
        this.port = port;
    }

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

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getDatabase() {
        return database;
    }

    public int getPort() {
        return port;
    }
}
