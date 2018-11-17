package com.turkmen.jms.config;


import com.turkmen.jms.util.Constants;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.util.ErrorHandler;

import javax.jms.Session;

@Configuration
@EnableJms
@ComponentScan({"com.turkmen.jms"})
public class JmsConfig {

    private static final Logger LOG = LoggerFactory.getLogger(JmsConfig.class);

    @Value("${jms.concurrent.connections}")
    private String numOfInstances;


    @Bean
    public JmsTemplate getJmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        jmsTemplate.setDefaultDestination(new ActiveMQQueue(Constants.QUEUE_TEST));
        jmsTemplate.setDeliveryPersistent(false);
        jmsTemplate.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);

        return jmsTemplate;
    }


    @Bean
    public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setPubSubDomain(false);
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setConcurrency(this.numOfInstances);
        factory.setErrorHandler(getErrorHandler());
        return factory;
    }

    private ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL("vm://localhost");

        return factory;
    }


    private ErrorHandler getErrorHandler() {
        return new ErrorHandler() {
            @Override
            public void handleError(Throwable throwable) {
                LOG.error(throwable.getCause().getMessage());
                throwable.printStackTrace();
            }
        };
    }
}
