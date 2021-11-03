package com.wtkj.oa.common.interceptors;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class MyLogFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent iLoggingEvent) {
        switch (iLoggingEvent.getLoggerName()) {
            case "org.apache.kafka.clients.NetworkClient": {
                if(iLoggingEvent.getLevel().equals(Level.WARN)){
                    return FilterReply.DENY;
                }
            }
            default: {
                return FilterReply.ACCEPT;
            }
        }
    }
}
