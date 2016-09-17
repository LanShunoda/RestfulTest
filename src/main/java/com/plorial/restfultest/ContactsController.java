package com.plorial.restfultest;

import com.plorial.restfultest.pojo.Contact;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plorial on 9/17/16.
 */
@RestController
public class ContactsController {

    private static JdbcTemplate jdbcTemplate;

    static {
        initContacts();
    }

    @RequestMapping("/hello/contact")
    public Contact contact() {
        return new Contact(0 , "Name");
    }

    @RequestMapping("/hello/contacts")
    public Contact[] contacts(@RequestParam(value="nameFilter", required=false, defaultValue = "")String nameFilter) {
        return filterContacts(getAllContactsFromDB(), nameFilter);
    }

    private static Contact[] filterContacts(List<Contact> contacts, String regex) {
        for (int i = 0; i < contacts.size(); i++) {
            if(contacts.get(i).getName().matches(regex)){
                contacts.remove(i);
            }
        }
        return contacts.toArray(new Contact[0]);
    }

    private static void initContacts(){
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(1, "1"));
        contacts.add(new Contact(2, "2"));
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUsername("sa");
        dataSource.setUrl("jdbc:h2:~/name");
        dataSource.setPassword("");


        jdbcTemplate = new JdbcTemplate(dataSource);

        System.out.println("Creating tables");
        jdbcTemplate.execute("drop table contacts if exists");
        jdbcTemplate.execute("create table contacts(" + "id serial, name varchar(255))");
        for (Contact c : contacts){
            jdbcTemplate.update("INSERT INTO contacts(id,name) values(?,?)",c.getId(), c.getName());
        }
    }

    public List<Contact> getAllContactsFromDB() {
        List<Contact> results = jdbcTemplate.query(
                "select * from contacts",
                (rs, rowNum) -> {
                    return new Contact(rs.getLong("id"), rs.getString("name"));
                });
        return results;
    }
}
