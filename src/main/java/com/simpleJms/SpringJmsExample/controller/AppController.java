package com.simpleJms.SpringJmsExample.controller;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.simpleJms.SpringJmsExample.pojos.Book;
import com.simpleJms.SpringJmsExample.pojos.BookOrder;
import com.simpleJms.SpringJmsExample.pojos.Customer;
import com.simpleJms.SpringJmsExample.service.jms.BookOrderService;

@RestController
public class AppController {
    
    @Autowired
    private BookOrderService bookOrderService;
   
    @GetMapping("/")
    public String appHome(ModelMap map){
        
        
        map.addAttribute("customers", customers);
        map.addAttribute("books", books);
        return "index";
    }
    
    @GetMapping(path = "/process/order")
    public String processOrder(@RequestParam String orderId,
							   @RequestParam String customerId,
							   @RequestParam String bookId )throws JsonMappingException, JsonParseException, IOException {
								        
        try {
            bookOrderService.send(build(customerId, bookId, orderId));
        } catch (Exception exception) {
            return "Error occurred!" + exception.getLocalizedMessage();
        }
        return "Order sent to warehouse for bookId = " + bookId + " from customerId = " + customerId + " successfully processed!";
    }
    
//--------------------------------------------------------------------------------
    
    //DATA FOR TEST
    List<Book> books = Arrays.asList(
            new Book("jpw-1234", "Lord of the Flies"),
            new Book("uyh-2345", "Being and Nothingness"),
            new Book("iuhj-87654","At Sea and Lost"));

    List<Customer> customers = Arrays.asList(
                    new Customer("mr-1234", "Michael Rodgers"),
                    new Customer("jp-2345", "Jeff Peek"),
                    new Customer("sm-8765", "Steve McClarney")
                    );
    
    
    //METHODE TO INIT BOOKORDER  
    private BookOrder build(String customerId, String bookId, String orderId){
        Book myBook = null;
        Customer myCustomer = null;
        
        for(Book bk : books){
            if(bk.getBookId().equalsIgnoreCase(bookId)){
                myBook = bk;
            }
        }
        if(null == myBook){
            throw new IllegalArgumentException("Book selected does not exist in inventory!");
        }
        
        for(Customer ct : customers){
            if(ct.getCustomerId().equalsIgnoreCase(customerId)){
                myCustomer = ct;
            }
        }
        
        if(null == myCustomer){
            throw new IllegalArgumentException("Customer selected does not appear to be valid!");
        }
        
        return new BookOrder(orderId, myBook, myCustomer);
    }
    
    
}


