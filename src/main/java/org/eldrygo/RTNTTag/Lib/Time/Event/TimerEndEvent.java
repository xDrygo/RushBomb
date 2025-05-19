package org.eldrygo.RTNTTag.Lib.Time.Event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TimerEndEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final String timerId;

    public TimerEndEvent(String timerId) {
        this.timerId = timerId;
    }

    public String getTimerId() {
        return timerId;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}