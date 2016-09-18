package com.plorial.restfultest;

import com.plorial.restfultest.pojo.Contact;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plorial on 9/18/16.
 */
public class FilterTest {

    List<Contact> contacts;

    @Before
    public void init(){
        contacts = new ArrayList<>();
        contacts.add(new Contact(1, "1"));
        contacts.add(new Contact(2, "2"));
    }

    @Test
    public void filterTest(){
        String regex = "^.*[2].*$";
        Contact[] result = Filter.filterContacts(contacts, regex);
        Assert.assertEquals(1,result[0].getId());
    }
}
