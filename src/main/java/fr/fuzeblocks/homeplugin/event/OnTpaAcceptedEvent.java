package fr.fuzeblocks.homeplugin.event;

import fr.fuzeblocks.homeplugin.tpa.TpaRequest;

/**
 * The type On tpa accepted event.
 */
public class OnTpaAcceptedEvent extends OnEventAction {
    private final TpaRequest tpaRequest;

    /**
     * Instantiates a new On tpa accepted event.
     *
     * @param tpaRequest the tpa request
     */
    public OnTpaAcceptedEvent(TpaRequest tpaRequest) {
        super(tpaRequest); // sets from = sender loc, to = target loc
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