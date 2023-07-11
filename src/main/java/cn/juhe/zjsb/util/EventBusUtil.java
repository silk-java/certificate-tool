package cn.juhe.zjsb.util;

import com.google.common.eventbus.EventBus;

public class EventBusUtil {
    private EventBusUtil() {
    }

    private final static EventBus EVENT_BUS = new EventBus();

    public static EventBus getDefault() {
        return EVENT_BUS;

    }
}