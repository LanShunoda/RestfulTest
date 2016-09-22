package com.plorial.restfultest;

import com.plorial.restfultest.pojo.Contact;
import com.sun.rowset.FilteredRowSetImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.rowset.FilteredRowSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by plorial on 9/17/16.
 */
@RestController
public class ContactsController {

    public static final String USERNAME = "sa";
    public static final String JDBC_H2_NAME = "jdbc:h2:~/name";
    public static final String PASSWORD = "";
    private static JdbcTemplate jdbcTemplate;

    static {
        initDB();
    }

    @RequestMapping("/hello/contacts")
    public Contact[] contacts(@RequestParam(value="nameFilter", required=true)String nameFilter) {
        try {
            Pattern.compile(nameFilter);
        }catch (PatternSyntaxException exception){
            exception.printStackTrace();
            throw exception;
        }
//        return getContactsForNameFilter(nameFilter).toArray(new Contact[]{}); //another way to filter
        return getContactsForRegex(nameFilter).toArray(new Contact[]{});
    }


    private static void initDB(){
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.h2.Driver.class);
        dataSource.setUsername(USERNAME);
        dataSource.setUrl(JDBC_H2_NAME);
        dataSource.setPassword(PASSWORD);

        jdbcTemplate = new JdbcTemplate(dataSource);

        System.out.println("Creating tables");
        jdbcTemplate.execute("drop table contacts if exists");
        jdbcTemplate.execute("create table contacts(" + "id BIGINT, name varchar(255))");
        for(int i = 0; i < 10; i++){
            jdbcTemplate.update("INSERT INTO contacts(id,name) values(?,?)",i, String.valueOf(i));
            System.out.println("Insert " + i);
        }
    }

    public List<Contact> getContactsForNameFilter(String nameFilter){
        List<Contact> results = jdbcTemplate.query(
                "select * from contacts",
                rs -> {
                    List<Contact> contacts = new ArrayList<Contact>();
                    while (rs.next()) {
                        String name = rs.getString("name");
                        if(RegexFilter.checkWithRegex(name,nameFilter)){
                            contacts.add(new Contact(rs.getLong(1),name));
                        }
                    }
                    rs.close();
                    return contacts;
                });
        return results;
    }

    public List<Contact> getContactsForRegex(String regex){
        List<Contact> contacts = new ArrayList<Contact>();
        FilteredRowSet frs = null;
        try {
            frs = new FilteredRowSetImpl();
            frs.setUsername(USERNAME);
            frs.setPassword(PASSWORD);
            frs.setUrl(JDBC_H2_NAME);
            frs.setCommand("select * from contacts");
            frs.execute();
            RegexFilter filter = new RegexFilter(regex, "name", 2);
            frs.beforeFirst();
            frs.setFilter(filter);
            while (frs.next()){
                long id = frs.getLong("id");
                String name = frs.getString("name");
//                System.out.println(id + name);
                contacts.add(new Contact(id,name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(frs != null){
                try {
                    frs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return contacts;
    }
}
