package fr.fuzeblocks.homeplugin.event;

import fr.fuzeblocks.homeplugin.tpa.TpaRequest;

/**
 * The type On tpa created event.
 */
public class OnTpaCreatedEvent extends OnEventAction {
    private final TpaRequest tpaRequest;

    /**
     * Instantiates a new On tpa created event.
     *
     * @param tpaRequest the tpa request
     */
    public OnTpaCreatedEvent(TpaRequest tpaRequest) {
        super(tpaRequest);
        this.tpaRequest = tpaRequest;
    }

    /**
     * Gets tpa request.
     *
     * @return the tpa request
     */
    public TpaRequest getTpaRequest() {
        return tpaRequest;
    }
}
