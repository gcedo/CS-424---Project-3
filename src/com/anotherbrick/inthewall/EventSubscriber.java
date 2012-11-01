package com.anotherbrick.inthewall;

public interface EventSubscriber {

    public void eventReceived(String eventName, Object data);

}
