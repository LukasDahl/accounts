/**
 * @author August
 */

package com.gr15.businesslogic;

import com.gr15.businesslogic.model.Account;
import com.gr15.businesslogic.model.User;

import javax.json.*;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;

@Path("/accounts")
public class RestCom {

    private AccountManager accountManager;

    public RestCom(){
        accountManager = new AccountManager();
        accountManager = accountManager.getInstance();
    }

    @GET
    public JsonArray getUsers() {
        JsonArrayBuilder usersBuild = Json.createArrayBuilder();
        for(Account account : accountManager.getAccounts().values()) {
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



    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getUserWithCpr(@QueryParam("userCpr") String userCpr) {
        Account account = null;

        for(Account temp_account: accountManager.getAccounts().values()){
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
        accountManager.getAccounts().put(account.getId().toString(), account);

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
        if(accountManager.getAccounts().remove(accountId) != null)
            return "204";
        return "404";
    }
}