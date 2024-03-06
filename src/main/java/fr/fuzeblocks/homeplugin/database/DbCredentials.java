package fr.fuzeblocks.homeplugin.database;

public class DbCredentials {
    private final String host;
    private final String user;
    private final String pass;
    private final String databasename;
    private final int port;

    public DbCredentials(String host, String user, String pass, String databasename, int port) {
        System.out.println("DbCredentials");
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.databasename = databasename;
        this.port = port;
    }
    public String toURI() {
        String builder = "jdbc:mysql://" +
                host +
                ":" +
                port +
                "/" +
                databasename +
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

    public String getDatabasename() {
        return databasename;
    }

    public int getPort() {
        return port;
    }
}
