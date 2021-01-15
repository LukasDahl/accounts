/**
 * @author Jonatan
 */

package com.gr15.businesslogic.model;

import java.util.UUID;

public class Account {
    private String type, bankAccountId;
    private UUID id;
    private User user;

    public Account(String type, String bankAccountId, User user){
        this.id = UUID.randomUUID();
        this.type = type;
        this.bankAccountId = bankAccountId;
        this.user = user;
    }

    public Account(){

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }
}
