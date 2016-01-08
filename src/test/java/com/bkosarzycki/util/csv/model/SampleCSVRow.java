package com.bkosarzycki.util.csv.model;

/**
 *
 * Holds sample CSV data
 * where 'userName' is the first column and 'firstName' is the second column.
 */
public class SampleCSVRow {

    private String userName;
    private String firstName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
