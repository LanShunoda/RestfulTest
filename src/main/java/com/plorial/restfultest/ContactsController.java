package com.plorial.restfultest;

import com.plorial.restfultest.pojo.Contact;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by plorial on 9/17/16.
 */
@RestController
public class ContactsController {

    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/hello/contact")
    public Contact contact() {
        return new Contact(counter.incrementAndGet(), "Name");
    }
}
