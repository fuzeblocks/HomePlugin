package fr.fuzeblocks.homeplugin.event;

import fr.fuzeblocks.homeplugin.tpa.TpaRequest;

public class OnTpaDeniedEvent extends OnEventAction {
    private final TpaRequest tpaRequest;
    public OnTpaDeniedEvent(TpaRequest tpaRequest) {
        super(tpaRequest);
        this.tpaRequest = tpaRequest;
    }
    public TpaRequest getTpaRequest() {
        return tpaRequest;
    }
}
