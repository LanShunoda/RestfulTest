package com.plorial.restfultest.pojo;

/**
 * Created by plorial on 9/17/16.
 */
public class Contact {

    private long id;
    private String name;

    public Contact(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
