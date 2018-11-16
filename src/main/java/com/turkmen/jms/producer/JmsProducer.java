package com.turkmen.jms.producer;


import com.turkmen.jms.util.Constants;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class JmsProducer {

    private static Logger LOG = LoggerFactory.getLogger(JmsProducer.class);

    @Autowired
    JmsTemplate jmsTemplate;


    private String destination = Constants.QUEUE_TEST;


    public void sendMessage() {
        LOG.info("Sending message to queue......");
        String message = "Turkmen Jms Example";

        jmsTemplate.convertAndSend(this.destination, message, messagePostProcessor);
    }


    MessagePostProcessor messagePostProcessor = message -> {
        message.setJMSReplyTo(new ActiveMQQueue(Constants.QUEUE_TEST));
        if (LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() % 2 == 0)
            message.setStringProperty("selector", "first");
        else
            message.setStringProperty("selector", "second");
        return message;
    };


}
