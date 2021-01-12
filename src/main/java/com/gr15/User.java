package com.gr15;

public class User {
    private String cprNumber, firstName, lastName;
    private Account account;

    public User(String cprNumber, String firstName, String lastName, Account account){
        this.cprNumber = cprNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.account = account;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
