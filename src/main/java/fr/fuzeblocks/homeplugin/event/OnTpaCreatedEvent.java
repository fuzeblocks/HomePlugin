package fr.fuzeblocks.homeplugin.event;

import fr.fuzeblocks.homeplugin.tpa.TpaRequest;

public class OnTpaCreatedEvent extends OnEventAction {
    private final TpaRequest tpaRequest;
    public OnTpaCreatedEvent(TpaRequest tpaRequest) {
        super(tpaRequest);
        this.tpaRequest = tpaRequest;
    }
    public TpaRequest getTpaRequest() {
        return tpaRequest;
    }
}
