package fr.fuzeblocks.homeplugin.plugin;


/**
 * The interface Home plugin.
 */
public interface HomePlugin {
    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets version.
     *
     * @return the version
     */
    String getVersion();

    /**
     * Gets author.
     *
     * @return the author
     */
    String getAuthor();

    /**
     * Get authors string [ ].
     *
     * @return the string [ ]
     */
    String[] getAuthors();

    /**
     * Initialize the plugin.
     */
    void initialize();

    /**
     * Stop the plugin and perform any necessary cleanup.
     */
    void stop();

    /**
     * Is sql storage enabled boolean.
     *
     * @return the boolean
     */
    boolean isSqlStorageEnabled();

    /**
     * Is placeholder api hooked boolean.
     *
     * @return the boolean
     */
    boolean isPlaceholderApiHooked();

    /**
     * Is cache enabled boolean.
     *
     * @return the boolean
     */
    boolean isCacheEnabled();

}
