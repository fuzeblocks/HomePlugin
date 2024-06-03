package fr.fuzeblocks.homeplugin.plugin;

public interface HomePlugin {
    String getName();
    String getVersion();
    String getAuthor();
    String[] getAuthors();

    /**
     * Initialize the plugin.
     *
     * @return true if initialization is successful, false otherwise
     */
    boolean initialize();

    /**
     * Stop the plugin and perform any necessary cleanup.
     *
     * @return true if the plugin was successfully stopped, false otherwise
     */
    boolean stop();
}
