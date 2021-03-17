package com.simpleJms.SpringJmsExample.service.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.simpleJms.SpringJmsExample.pojos.BookOrder;

@Service
public class WarehouseReceiverService {

	private static final Logger log = LoggerFactory.getLogger(WarehouseReceiverService.class);
	
	@JmsListener(destination = "book.order.queue")
    public void receive(BookOrder bookOrder){
		log.info("Message received!");
		log.info("Message is == " + bookOrder);
    }
	
}
