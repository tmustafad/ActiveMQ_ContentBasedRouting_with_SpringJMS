#SpringBoot JMS - Content based routing with ActiveMQ 


## Install and run

Once you import project into your workspace as exsisting maven project, all you need to do install the maven project and run it.

You can run the project via JmsSelectorWithActiveMqApplication class which is the starting point for all . 

## The Use Case
* Create a Scheduler (this is not relevant with the topic of this article, this is just for simulating the load of the application ) and call the message producer based on the timestamp
* Create a message
* Create an header property and a value for the message
* Set required properties of the message header with post message processor(replyTo etc.)
* Send this message to ActiveMQ
* According to the header property set before producing message,consume this message or not.

