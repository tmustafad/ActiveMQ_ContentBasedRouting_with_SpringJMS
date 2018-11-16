package com.turkmen.jms.consumer;


import com.turkmen.jms.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;

@Component
public class SecondJmsConsumer {

    private static Logger LOG = LoggerFactory.getLogger(SecondJmsConsumer.class);

    @JmsListener(containerFactory = "defaultJmsListenerContainerFactory", destination = Constants.QUEUE_TEST, selector = "selector = 'second'")
    public void consume(TextMessage message) throws JMSException {
        LOG.info("in consumer second....");

        LOG.info("Consumed by second  : " + message.getText());

    }




}
