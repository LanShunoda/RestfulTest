package com.plorial.restfultest;

import com.plorial.restfultest.pojo.Contact;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by plorial on 9/17/16.
 */
@RestController
public class ContactsController {

    private static JdbcTemplate jdbcTemplate;

    static {
        initDB();
    }

    @RequestMapping("/hello/contacts")
    public Contact[] contacts(@RequestParam(value="nameFilter", required=false, defaultValue = "")String nameFilter) {
        return Filter.filterContacts(getAllContactsFromDB(), nameFilter);
    }

    private static void initDB(){
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUsername("sa");
        dataSource.setUrl("jdbc:h2:~/name");
        dataSource.setPassword("");

        jdbcTemplate = new JdbcTemplate(dataSource);

        System.out.println("Creating tables");
        jdbcTemplate.execute("drop table contacts if exists");
        jdbcTemplate.execute("create table contacts(" + "id BIGINT, name varchar(255))");
        for(int i = 0; i < 1_000; i++){
            jdbcTemplate.update("INSERT INTO contacts(id,name) values(?,?)",i, String.valueOf(i));
            System.out.println("Insert " + i);
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
