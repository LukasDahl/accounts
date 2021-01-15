/**
 * @author Jonatan
 */

package com.gr15.businesslogic;

import com.gr15.businesslogic.model.Account;
import com.gr15.businesslogic.model.User;

import javax.json.*;
import javax.ws.rs.QueryParam;
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
                new User("000000-0002", "Micheal", "Jordan"),
                "8ea0bd16-86eb-498c-9fb7-f8b26c8d76bb");

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


    public JsonArray getUsers() {
        JsonArrayBuilder usersBuild = Json.createArrayBuilder();
        for(Account account : accounts.values()) {
            JsonObjectBuilder userBuild = Json.createObjectBuilder()
                    .add("cprNumber", account.getUser().getCprNumber())
                    .add("firstName", account.getUser().getFirstName())
                    .add("lastName", account.getUser().getLastName());
            JsonObject userJson = userBuild.build();

            JsonObjectBuilder accountBuild = Json.createObjectBuilder()
                    .add("type", account.getType())
                    .add("bankAccountId", account.getBankAccountId())
                    .add("id", account.getId().toString())
                    .add("user", userJson);
            usersBuild.add(accountBuild.build());
        }

        return usersBuild.build();
    }


    public JsonObject getUserWithCpr(String userCpr) {
        Account account = null;

        for(Account temp_account: accounts.values()){
            if (temp_account.getUser().getCprNumber().equals(userCpr))
                account = temp_account;
        }
        // todo make error message
        if (account == null){
            return null;
        }

        JsonObjectBuilder userBuild = Json.createObjectBuilder()
                .add("cprNumber", account.getUser().getCprNumber())
                .add("firstName", account.getUser().getFirstName())
                .add("lastName", account.getUser().getLastName());
        JsonObject userJson = userBuild.build();

        JsonObjectBuilder accountBuild = Json.createObjectBuilder()
                .add("type", account.getType())
                .add("bankAccountId", account.getBankAccountId())
                .add("id", account.getId().toString())
                .add("user", userJson);

        return accountBuild.build();
    }

    public String createUser(JsonObject jsonObject) {

        String type, bankAccountID, cpr, first, last;

        try {
            last = jsonObject.getJsonObject("user").getString("lastName");
        } catch (NullPointerException e){
            return "400 error: missing lastName";
        }

        try {
            first = jsonObject.getJsonObject("user").getString("firstName");
        } catch (NullPointerException e){
            return "400 error: missing firstName";
        }

        try {
            cpr = jsonObject.getJsonObject("user").getString("cprNumber");
        } catch (NullPointerException e){
            return "400 error: missing cprNumber";
        }

        try {
            bankAccountID = jsonObject.getString("bankAccountId");
        } catch (NullPointerException e){
            return "400 error: missing bankAccountId";
        }

        try {
            type = jsonObject.getString("type");
        } catch (NullPointerException e){
            return "400 error: missing type";
        }
        Account account = new Account(type, bankAccountID,
                new User(cpr, first, last));
        accounts.put(account.getId().toString(), account);

        return "user created";
    }


    public String deleteAccount(String accountId) {
        if(accounts.remove(accountId) != null)
            return "204";
        return "404";
    }

}
