package fr.fuzeblocks.homeplugin.core.event;

import fr.fuzeblocks.homeplugin.core.tpa.TpaRequest;

/**
 * The type On tpa denied event.
 */
public class OnTpaDeniedEvent extends OnEventAction {
    private final TpaRequest tpaRequest;

    /**
     * Instantiates a new On tpa denied event.
     *
     * @param tpaRequest the tpa request
     */
    public OnTpaDeniedEvent(TpaRequest tpaRequest) {
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