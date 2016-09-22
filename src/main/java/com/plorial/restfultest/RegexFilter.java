package com.plorial.restfultest;

import javax.sql.RowSet;
import javax.sql.rowset.FilteredRowSet;
import javax.sql.rowset.Predicate;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by plorial on 9/18/16.
 */
public class RegexFilter implements Predicate{

    private Pattern pattern;
    private String columnName;
    private int columnIndex;

    public RegexFilter(String regex, String columnName, int columnIndex){
        pattern = Pattern.compile(regex);
        this.columnName = columnName;
        this.columnIndex = columnIndex;
    }

    public static boolean checkWithRegex(String name, String regex){
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(name);
        return !m.matches();
    }

    @Override
    public boolean evaluate(RowSet rs) {
        boolean evaluation = false;
        if (rs == null) {
            return false;
        }
        FilteredRowSet frs = (FilteredRowSet) rs;
        try {
            String name = frs.getString(columnName);
            evaluation = !pattern.matcher(name).matches();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return evaluation;
    }

    @Override
    public boolean evaluate(Object value, int column) throws SQLException {
        boolean evaluation = false;
        if(column == columnIndex) {
            String name = (String) value;
            evaluation = !pattern.matcher(name).matches();
        }
        return evaluation;
    }

    @Override
    public boolean evaluate(Object value, String columnName) throws SQLException {
        boolean evaluation = false;
        if (columnName.equalsIgnoreCase(this.columnName)){
            String name = (String) value;
            evaluation = !pattern.matcher(name).matches();
        }
        return evaluation;
    }
}
