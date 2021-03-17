package com.simpleJms.SpringJmsExample.config;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.simpleJms.SpringJmsExample.pojos.Book;
import com.simpleJms.SpringJmsExample.pojos.BookOrder;
import com.simpleJms.SpringJmsExample.pojos.Customer;

@EnableJms
@Configuration
public class JmsConfig {
	
	
	// Convert Text Message to Object *** we make the bean as comment to use another converter type
    // @Bean
    public MessageConverter jacksonJmsMessageConverter(){
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
    
    
    // CONFIG PROPERTY TO SET SUPPORTED XML CLASSES
    @Bean
    public XStreamMarshaller xmlMarshaler() {
    	XStreamMarshaller marshaller = new XStreamMarshaller();
    	marshaller.setSupportedClasses(Book.class, Customer.class, BookOrder.class);
    	return marshaller;
    }

    
    @Bean
    public MessageConverter xmlMarshallingMessageConverter(){
        MarshallingMessageConverter converter = new MarshallingMessageConverter(xmlMarshaler());
        converter.setTargetType(MessageType.TEXT);
        // we don't have to set Type Id Property name
        // the marsheller doesn't requier that 
        return converter;
    }

    // Create th connection instance
    @Bean
    public ActiveMQConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("admin","admin","tcp://localhost:61616");
        return factory;
    }

    
    // Init the jms Listener container
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        // Adding accepted converter type
        // factory.setMessageConverter(jacksonJmsMessageConverter());
        factory.setMessageConverter(xmlMarshallingMessageConverter());
        return factory;
    }


}
