package com.gr15;

import java.util.List;

public class User {
    private String cprNumber, firstName, lastName;
    private List<Account> account;

    public User(String cprNumber, String firstName, String lastName, List<Account> account){
        this.cprNumber = cprNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.account = account;
    }

    public User(){

    }

    public String getCprNumber() {
        return cprNumber;
    }

    public void setCprNumber(String cprNumber) {
        this.cprNumber = cprNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Account> getAccount() {
        return account;
    }

    public void setAccount(List<Account> account) {
        this.account = account;
    }
}
