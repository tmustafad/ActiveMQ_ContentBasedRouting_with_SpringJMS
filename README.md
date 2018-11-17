#SpringBoot JMS - Content based routing with ActiveMQ 


##The Use Case
Create a Scheduler (this is not relevant with the topic of this article, this is just for simulating the load of the application ) and call the message producer based on the timestamp
Create a message
Create an header property and a value for the message
Set required properties of the message header with post message processor(replyTo etc.)
Send this message to ActiveMQ
According to the header property set before producing message,consume this message or not.
The project Structure
As you see below picture ,this is a maven project ,the skeleton is created via start.spring.io


##Maven dependencies
```
<dependencies><dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-activemq</artifactId></dependency><dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-web</artifactId></dependency>

   <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-test</artifactId><scope>test</scope></dependency>

   <dependency><groupId>org.apache.activemq</groupId><artifactId>activemq-kahadb-store</artifactId><version>${activemq.version}</version></dependency>
</dependencies>
```
In pom.xml, other than usual SpringBoot starter staff(web and test ),there is ActiveMQ and kaha db dependencies.


#The Jms Configuration
For a spring application to have a jms configuration is pretty starightforward. There should be four main components;

* JmsTemplate
* Jms Message Producer
* Jms Message Consumer
* A messaging server to put and get the messages
```
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
        @Overridepublic void handleError(Throwable throwable) {
            LOG.error(throwable.getCause().getMessage());
            throwable.printStackTrace();
        }
    };
}
```
In our configuration class , we have annotated the getJmsTemplate method with @Bean,we did so because we will autowire this bean to our producer and consumer classes.

As you can observe a ActiveMQConnectionFactory is created for the JmsTemplate bean . While createing our ActiveMQ connection ,VM transport protocol is used for not causing a network overhead as it is using direct method invocations to ActiveMQ which makes it much faster and reliable.

For default destination we get the queue name from our Constants class which has a final static variable, we may have used application.yml or db config tables for this. Because this is a simple demo ,I have chosen this way.Below is Constants class.
```
package com.turkmen.jms.util;

public class Constants {

    public static final String QUEUE_TEST = "turkmen.test";
}
```
By setting the session auto acknowledge mode to AUTO we are making ActiveMQ to make the acknowledgement automaticaly soon after a new message comes in.

The setConcurrency property is read via application.properties file . If you want to scale up your consumers , and of course if you have enough cores on your servers, you can have concurrent consumers . I have set this property to 1-5 ,this means at least one consumer is up ,and according to the load , multiple consumers can be created concurrently up to 5. The property value is captured by a private attribute via @Value  annotation as shown below.
```
@Value("${jms.concurrent.connections}")
private String numOfInstances;
```
```
jms.concurrent.connections=1-5
```