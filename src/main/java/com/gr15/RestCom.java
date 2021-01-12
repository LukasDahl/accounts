package com.gr15;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/accounts")
public class RestCom {
    private List<User> dummyUsers = new ArrayList<>();

    public RestCom(){
        List<Account> user1 = new ArrayList<>();
        List<Account> user3 = new ArrayList<>();
        List<Account> user2 = new ArrayList<>();

        user1.add(new Account("0", "Costumer", "10"));
        user1.add(new Account("1", "Merchant", "11"));
        user2.add(new Account("2", "Costumer", "12"));
        user3.add(new Account("3", "Merchant", "13"));

        dummyUsers.add(new User("000000-0000", "Jonatan", "Jonatansen", user1));
        dummyUsers.add(new User("000000-0001", "August", "Augustsen", user2));
        dummyUsers.add(new User("000000-0002", "Micheal", "Jordan", user3));
    }

    public static void main(String[] args) {

        RestCom restCom = new RestCom();

        System.out.println(restCom.createUser(restCom.demoJson()));
    }


    @POST
    public String createUser(JsonObject jsonObject) {

        User user = new User();
        String type, bankAccountID;

        try {
            user.setLastName(jsonObject.getJsonObject("user").get("lastName").toString());
        } catch (NullPointerException e){
            return "400 error: missing lastName";
        }

        try {
            user.setFirstName(jsonObject.getJsonObject("user").get("firstName").toString());
        } catch (NullPointerException e){
            return "400 error: missing firstName";
        }

        try {
            user.setCprNumber(jsonObject.getJsonObject("user").get("cprNumber").toString());
        } catch (NullPointerException e){
            return "400 error: missing cprNumber";
        }

        try {
            bankAccountID = jsonObject.get("bankAccountId").toString();
        } catch (NullPointerException e){
            return "400 error: missing bankAccountId";
        }

        try {
            type = jsonObject.get("type").toString();
        } catch (NullPointerException e){
            return "400 error: missing type";
        }

        user.getAccount().add(new Account(type, bankAccountID));
        dummyUsers.add(user);

        return "200";
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
    public String deleteUser(@PathParam(MediaType.APPLICATION_JSON) String userCprNumber) {
        for (int i = 0; i < dummyUsers.size(); i++) {
            if (dummyUsers.get(i).getCprNumber().equals(userCprNumber)) {
                dummyUsers.remove(i);
                return "200";
            }
        }
        return "404";
    }
}