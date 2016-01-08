package com.bkosarzycki.util.csv.model;

/**
 *
 * Holds sample CSV data
 * where 'userName' is the first column and 'lastName' is the second column.
 */
public class SampleCSVRow2 {

    private String userName;
    private String lastName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLasttName(String lasttName) {
        this.lastName = lasttName;
    }
}
