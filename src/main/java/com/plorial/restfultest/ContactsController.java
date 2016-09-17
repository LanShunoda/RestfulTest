package com.plorial.restfultest;

import com.plorial.restfultest.pojo.Contact;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by plorial on 9/17/16.
 */
@RestController
public class ContactsController {

    static List<Contact> contacts;

    static {
        initContacts();
    }

    @RequestMapping("/hello/contact")
    public Contact contact() {
        return new Contact(0 , "Name");
    }

    @RequestMapping("/hello/contacts")
    public Contact[] contacts(@RequestParam(value="nameFilter", required=false, defaultValue = "")String nameFilter) {
        return filterContacts(contacts, nameFilter);
    }

    private static Contact[] filterContacts(List<Contact> contacts, String regex) {
        for (int i = 0; i < contacts.size(); i++) {
            if(contacts.get(i).getName().matches(regex)){
                contacts.remove(i);
            }
        }
        initContacts();
        return contacts.toArray(new Contact[0]);
    }

    private static void initContacts(){
        contacts = new LinkedList<>();
        contacts.add(new Contact(1, "1"));
        contacts.add(new Contact(2, "2"));
    }
}
