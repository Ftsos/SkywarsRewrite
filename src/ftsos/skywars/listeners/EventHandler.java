package ftsos.skywars.listeners;

import org.bukkit.event.Cancellable;


import java.util.List;

public class EventHandler {

    public List<EventListener> listeners;
    public EventHandler() {
        //this.events = events;
    }

    public EventHandler(List<EventListener> listeners) {
        this.listeners = listeners;
    }

    public void handle(Cancellable event){
        for (EventListener listener : listeners) {
            listener.handle(event);
        }
    }



    public List<EventListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<EventListener> listeners) {
        this.listeners = listeners;
    }

    public void addListener(EventListener listener) {
        this.listeners.add(listener);
    }
}
