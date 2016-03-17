package com.clv.jmslistener.integration;

import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Created by Usuario on 16/03/2016.
 */
@Component("eventListener")
public class EventListener implements MessageListener {

    public void onMessage(Message message) {

    }
}
