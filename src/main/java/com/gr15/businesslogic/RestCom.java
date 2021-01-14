/**
 * @author August
 */

package com.gr15.businesslogic;

import javax.json.*;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.*;
import java.util.HashMap;

@Path("/accounts")
public class RestCom {
    private HashMap<String, Account> dummyAccounts = new HashMap<>();
    private Account testAccount1, testAccount2, testAccount3;

    public RestCom(){
        testAccount1 = new Account("Merchant", "0",
                new User("000000-0000", "Jonatan", "Jonatansen"));
        testAccount2 = new Account("Costumer", "1",
                new User("000000-0001", "August", "Augustsen"));
        testAccount3 = new Account("Costumer", "2",
                new User("000000-0002", "Micheal", "Jordan"));

        dummyAccounts.put(testAccount1.getId().toString(), testAccount1);
        dummyAccounts.put(testAccount2.getId().toString(), testAccount2);
        dummyAccounts.put(testAccount3.getId().toString(), testAccount3);
    }

    public static void main(String[] args) {

        RestCom restCom = new RestCom();

        System.out.println(restCom.createUser(restCom.demoJson()));
    }


    @GET
    public JsonArray getUsers() {
        JsonArrayBuilder usersBuild = Json.createArrayBuilder();
        for(Account account : dummyAccounts.values()) {
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

    @POST
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
        dummyAccounts.put(account.getId().toString(), account);

        // todo publish user

        return "user created";
    }



    public JsonObject demoJson(){
        JsonObjectBuilder userBuild = Json.createObjectBuilder()
                .add("cprNumber", "12345")
                .add("firstName", "Bib")
                .add("lastName", "Bob");
        JsonObject user = userBuild.build();

        JsonObjectBuilder account = Json.createObjectBuilder()
                .add("type", "merchant")
                .add("bankAccountId", "123aad")
                .add("user", user);

        return account.build();

    }



    @DELETE
    public String deleteAccount(String accountId) {
        if(dummyAccounts.remove(accountId) != null)
            return "204";
        return "404";
    }

    public HashMap<String, Account> getDummyAccounts() {
        return dummyAccounts;
    }
}