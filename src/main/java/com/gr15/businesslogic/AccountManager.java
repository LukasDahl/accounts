package com.gr15.businesslogic;

import com.gr15.businesslogic.model.Account;
import com.gr15.businesslogic.model.User;

import java.util.HashMap;

public class AccountManager {
    private HashMap<String, Account> accounts = new HashMap<>();
    private static AccountManager instance = null;

    public AccountManager() {
        Account testAccount1 = new Account("Merchant", "0",
                new User("000000-0000", "Jonatan", "Jonatansen"));
        Account testAccount2 = new Account("Costumer", "1",
                new User("000000-0001", "August", "Augustsen"));
        Account testAccount3 = new Account("Costumer", "2",
                new User("000000-0002", "Micheal", "Jordan"));

        accounts.put(testAccount1.getId().toString(), testAccount1);
        accounts.put(testAccount2.getId().toString(), testAccount2);
        accounts.put(testAccount3.getId().toString(), testAccount3);
    }

    public static AccountManager getInstance(){
        if (instance == null){
            instance = new AccountManager();
        }
        return instance;
    }

    public HashMap<String, Account> getAccounts() {
        return accounts;
    }

    public Account validateAccount(String accountId) {
        return accounts.get(accountId);
    }

}
