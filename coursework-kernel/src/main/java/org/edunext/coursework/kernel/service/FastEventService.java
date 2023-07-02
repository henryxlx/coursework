package org.edunext.coursework.kernel.service;

import com.jetwinner.webfast.event.FastEventHandler;
import com.jetwinner.webfast.event.ServiceEvent;

/**
 * @author xulixin
 */
public interface FastEventService {

    default void dispatchEvent(String eventName, ServiceEvent serviceEvent) {
        FastEventHandler.getDefault().dispatchEvent(eventName, serviceEvent);
    }
}
