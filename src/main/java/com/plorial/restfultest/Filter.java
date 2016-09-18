package com.plorial.restfultest;

import com.plorial.restfultest.pojo.Contact;

import java.util.List;

/**
 * Created by plorial on 9/18/16.
 */
public class Filter {

    private Filter(){}

    public static Contact[] filterContacts(List<Contact> contacts, String regex) {
        for (int i = 0; i < contacts.size(); i++) {
            if(contacts.get(i).getName().matches(regex)){
                contacts.remove(i);
            }
        }
        return contacts.toArray(new Contact[0]);
    }
}
