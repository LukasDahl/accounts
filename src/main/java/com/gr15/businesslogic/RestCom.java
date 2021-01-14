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
    private Account testAccount1, testAccount2, testAccount3;

    public RestCom(){

    }

    @GET
    public JsonArray getUsers(HashMap<String, Account> accounts) {
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

    @POST
    public String createUser(JsonObject jsonObject, HashMap<String, Account> accounts) {

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
    public String deleteAccount(String accountId, HashMap<String, Account> accounts) {
        if(accounts.remove(accountId) != null)
            return "204";
        return "404";
    }
}