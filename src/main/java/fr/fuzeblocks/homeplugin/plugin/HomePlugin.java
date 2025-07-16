package fr.fuzeblocks.homeplugin.plugin;


public interface HomePlugin {
    String getName();
    String getVersion();
    String getAuthor();
    String[] getAuthors();

    /**
     * Initialize the plugin.
     */
    void initialize();

    /**
     * Stop the plugin and perform any necessary cleanup.
     */
     void stop();

    boolean isSqlStorageEnabled();
    boolean isPlaceholderApiHooked();
    boolean isCacheEnabled();

}
