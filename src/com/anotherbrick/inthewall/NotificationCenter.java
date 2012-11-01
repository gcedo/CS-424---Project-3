package com.anotherbrick.inthewall;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationCenter {

    private static NotificationCenter instance;

    private HashMap<String, ArrayList<EventSubscriber>> subscribers;

    public static NotificationCenter getInstance() {
	if (instance == null)
	    instance = new NotificationCenter();
	return instance;
    }

    private NotificationCenter() {
	subscribers = new HashMap<String, ArrayList<EventSubscriber>>();
    }

    public void registerToEvent(String eventName,
	    EventSubscriber eventSubscriber) {
	if (!subscribers.containsKey(eventName)) {
	    subscribers.put(eventName, new ArrayList<EventSubscriber>());
	}
	if (!subscribers.get(eventName).contains(eventSubscriber)) {
	    subscribers.get(eventName).add(eventSubscriber);
	}
    }

    public void notifyEvent(String eventName) {
	notifyEvent(eventName, null);
    }

    public void notifyEvent(String eventName, Object data) {
	for (EventSubscriber es : subscribers.get(eventName)) {
	    es.eventReceived(eventName, data);
	}
    }
}
