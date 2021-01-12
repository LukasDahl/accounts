package org.acme;

public class Account {
    public String id, type, bankAccountId;

    public Account(String id, String type, String bankAccountId){
        this.id = id;
        this.type = type;
        this.bankAccountId = bankAccountId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
