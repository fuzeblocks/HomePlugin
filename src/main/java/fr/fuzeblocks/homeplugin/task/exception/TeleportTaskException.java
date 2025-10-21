package fr.fuzeblocks.homeplugin.task.exception;

/**
 * The type Teleport task exception.
 */
public class TeleportTaskException extends Exception {
    /**
     * Instantiates a new Teleport task exception.
     */
    public TeleportTaskException() {
        super("You can't cancel TeleportTask because TeleportTask it's null !");
    }
}
