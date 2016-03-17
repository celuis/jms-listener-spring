package com.clv.jmslistener.config;

import com.clv.jmslistener.exception.JMSErrorHandler;
import com.clv.jmslistener.integration.EventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.destination.JndiDestinationResolver;
import org.springframework.jndi.JndiTemplate;
import weblogic.jndi.WLInitialContextFactory;

import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * Created by Usuario on 16/03/2016.
 */
@Configuration
@ComponentScan(basePackages = {"com.clv.jmslistener"})
public class JmsAppConfig {

    @Autowired
    private EventListener eventListener;

    @Autowired
    private JMSErrorHandler jmsErrorHandler;

    /**
     * Create connection factory.
     * @return
     */
    @Bean
    public QueueConnectionFactory queueConnectionFactory(){
        return (QueueConnectionFactory)lookupResource("CONNECTION_FACTORY_JNDI"); //JNDI connection factory name stored in weblogic.
    }

    /**
     * Create Queue.
     * @return
     */
    @Bean
    public Queue queueDestination(){
        return (Queue)lookupResource("QUEUE_JNDI"); //JNDI queue name stored in weblogic.
    }

    /**
     * Create InitialContext.
     * @return
     */
    @Bean
    public Properties jndiProperties(){
        Properties properties = new Properties();
        properties.put("java.naming.factory.initial", WLInitialContextFactory.class.getName());
        properties.put("java.naming.provider.url", "t3://PROVIDER_URL:PORT"); //JNDI connection factory name stored in weblogic.
        return properties;
    }

    /**
     * Create InitialContext.
     * @return
     */
    @Bean
    public JndiTemplate jndiTemplate(){
        JndiTemplate jndiTemplate = new JndiTemplate();
        jndiTemplate.setEnvironment(this.jndiProperties());
        return jndiTemplate;
    }

    /**
     * Create DestinationResolver
     * @return
     */
    @Bean
    public JndiDestinationResolver jndiDestinationResolver(){
        JndiDestinationResolver jndiDestinationResolver = new JndiDestinationResolver();
        jndiDestinationResolver.setJndiTemplate(this.jndiTemplate());
        jndiDestinationResolver.setCache(true);
        return jndiDestinationResolver;
    }

    @Bean
    public DefaultMessageListenerContainer jmsContainer(){
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(this.queueConnectionFactory());
        container.setDestinationResolver(this.jndiDestinationResolver());
        container.setDestination(this.queueDestination());
        container.setConcurrentConsumers(1);
        container.setMessageListener(eventListener);
        container.setSessionAcknowledgeModeName("AUTO_ACKNOWLEDGE");
        container.setCacheLevelName("CACHE_CONSUMER");
        container.setErrorHandler(jmsErrorHandler);
        return container;
    }

    /**
     * Lookup the resource stored in weblogic.
     * @param resource
     * @return
     */
    private Object lookupResource(String resource){
        try {
            InitialContext initialContext = new InitialContext(this.jndiProperties());
            Object o = initialContext.lookup(resource);
            initialContext.close();
            return o;

        } catch (NamingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
