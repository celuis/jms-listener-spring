# JMS Sender with Spring

## Synopsis

This project is an example of a JMS listener, the peculiarity is that connects to resources hosted and deployed in Weblogic. 
Uses Spring 3.2.3 RELEASE and is wired XML-less.

## Preparation
Find class: _JmsAppConfig_ and change property _PROVIDER_URL_ on LOC 59, to the URL where the resources are deployed in weblogic.

```language
        @Bean
        public Properties jndiProperties(){
             Properties properties = new Properties();
             properties.put("java.naming.factory.initial", WLInitialContextFactory.class.getName());
             properties.put("java.naming.provider.url", "t3://PROVIDER_URL:PORT"); //JNDI connection factory name stored in weblogic.
             return properties;
        }
```

In the same class go find LOC 39 and place the JNDI connection factory name stored in weblogic.

```language
       @Bean
       public QueueConnectionFactory queueConnectionFactory(){
           return (QueueConnectionFactory)lookupResource("CONNECTION_FACTORY_JNDI"); //JNDI connection factory name stored in weblogic.
       }
```

In LOC 48 place the JNDI queue name stored in weblogic.
```language
       @Bean
       public Queue queueDestination(){
           return (Queue)lookupResource("QUEUE_JNDI"); //JNDI queue name stored in weblogic.
       }
```

## Installation

After you have change the resources, use: 

`mvn clean install`

## Deploy

Deploy the generated WAR in a Weblogic Application Server.