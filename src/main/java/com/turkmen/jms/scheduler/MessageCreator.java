package com.turkmen.jms.scheduler;


import com.turkmen.jms.producer.JmsProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MessageCreator {

    @Autowired
    JmsProducer jmsProducer;

    private static final Logger LOG = LoggerFactory.getLogger(MessageCreator.class);


    @Scheduled(fixedDelay = 3000)
    public void createMessage() {
        LOG.info("message creator started.....");
        jmsProducer.sendMessage();
        LOG.info("message created...... ");
    }

}
