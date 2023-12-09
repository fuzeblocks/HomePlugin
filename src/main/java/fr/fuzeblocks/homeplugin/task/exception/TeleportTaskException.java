package fr.fuzeblocks.homeplugin.task.exception;

public class TeleportTaskException extends Exception {
    public TeleportTaskException() {
        super("You can't cancel TeleportTask because TeleportTask it's null !");
    }
}
