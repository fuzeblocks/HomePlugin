package fr.fuzeblocks.homeplugin.event;

import fr.fuzeblocks.homeplugin.tpa.TpaRequest;

public class OnTpaAcceptedEvent extends OnEventAction {
    private final TpaRequest tpaRequest;
    public OnTpaAcceptedEvent(TpaRequest tpaRequest) {
        super(tpaRequest);
        this.tpaRequest = tpaRequest;
    }
    public TpaRequest getTpaRequest() {
        return tpaRequest;
    }
}
