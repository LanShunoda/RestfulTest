package com.plorial.restfultest;

import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by plorial on 9/18/16.
 */
public class FilterTest {

    @Test
    public void regexFilterTest(){
        String name = "abc";
        String regex = "^.*[dei].*$";
        boolean contains = RegexFilter.checkWithRegex(name, regex);
        Assert.assertTrue(contains);
    }

    @Test
    public void predictableEvaluateColumnIndexTest() throws SQLException {
        RegexFilter filter = new RegexFilter("^.*[1].*$","name", 2);
        boolean b = filter.evaluate(new String("2"),2);
        Assert.assertTrue(b);
    }

    @Test
    public void predictableEvaluateColumnNameTest() throws SQLException {
        RegexFilter filter = new RegexFilter("^.*[1].*$","name", 2);
        boolean b = filter.evaluate(new String("2"),"name");
        Assert.assertTrue(b);
    }
}
