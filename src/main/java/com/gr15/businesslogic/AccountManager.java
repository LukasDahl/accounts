/**
 * @author Jonatan
 */

package com.gr15.businesslogic;

import com.gr15.businesslogic.exceptions.QueueException;
import com.gr15.businesslogic.model.Account;
import com.gr15.businesslogic.model.User;
import com.gr15.messaging.rabbitmq.RabbitMqSender;

import javax.json.*;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

public class AccountManager {
    private HashMap<String, Account> accounts = new HashMap<>();
    private static AccountManager instance = null;
    private final QueueService queueService = new QueueService(new RabbitMqSender());

    private AccountManager() {
        Account testAccount1 = new Account("Costumer", "f2f2e491-26c8-4e86-b084-d2faf560bcdb",
                new User("000000-0000", "Jonatan", "Jonatansen"));
        Account testAccount2 = new Account("Merchant", "77f3674a-3515-4c2e-b5c3-067c8f09f9b3",
                new User("000000-0001", "August", "Augustsen"));
        Account testAccount3 = new Account("Costumer", "2",
                new User("000000-0002", "Micheal", "Jordan"),
                "8ea0bd16-86eb-498c-9fb7-f8b26c8d76bb");

        accounts.put(testAccount1.getId().toString(), testAccount1);
        accounts.put(testAccount2.getId().toString(), testAccount2);
        accounts.put(testAccount3.getId().toString(), testAccount3);
    }

    public static AccountManager getInstance(){
        if (instance == null) instance = new AccountManager();
        return instance;
    }

    public Account validateAccount(String accountId) {
        return accounts.get(accountId);
    }

    public Response getUsers() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for(Account account : accounts.values()) {
            JsonObject userJson = Json.createObjectBuilder()
                    .add("cprNumber", account.getUser().getCprNumber())
                    .add("firstName", account.getUser().getFirstName())
                    .add("lastName", account.getUser().getLastName())
                    .build();

            JsonObject accountJson = Json.createObjectBuilder()
                    .add("type", account.getType())
                    .add("bankAccountId", account.getBankAccountId())
                    .add("id", account.getId().toString())
                    .add("user", userJson)
                    .build();

            arrayBuilder.add(accountJson);
        }
        JsonArray array = arrayBuilder.build();
        System.out.println(array);
        return Response.status(200).entity(array).type(MediaType.APPLICATION_JSON).build();
    }

    public Response getUserWithCpr(String userCpr) {
        Account account = null;
        System.out.println("sCpr: " + userCpr);

        for(Account temp_account: accounts.values()){
            if (temp_account.getUser().getCprNumber().equals(userCpr)) {
                account = temp_account;
                System.out.println("Found account");
            }
        }

        if (account == null){
            System.out.println("Could not find account with that cprNumber");
            throw new BadRequestException(Response.status(400).build());
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
        System.out.println("Build account");
        return Response.status(200).entity(accountBuild.build()).type(MediaType.APPLICATION_JSON).build();
    }

    public Response createUser(JsonObject jsonObject) {
        System.out.println("Creating user");
        String type, bankAccountID, cpr, first, last;
        JsonObject response;
        try {
            System.out.println("Json");
            last = jsonObject.getJsonObject("user").getString("lastName");
            first = jsonObject.getJsonObject("user").getString("firstName");
            cpr = jsonObject.getJsonObject("user").getString("cprNumber");
            System.out.println("Cpr: " + cpr);
            bankAccountID = jsonObject.getString("bankAccountId");
            type = jsonObject.getString("type");
            System.out.println("Put user into Json");
        } catch (NullPointerException e){
            response = Json.createObjectBuilder()
                    .add("errorMessage", "Missing element(s)").build();
            throw new BadRequestException(Response.status(400).entity(response).type(MediaType.APPLICATION_JSON).build());
        }
        Account createdAccount = new Account(type, bankAccountID, new User(cpr, first, last));
        accounts.put(createdAccount.getId().toString(), createdAccount);
        response = Json.createObjectBuilder().add("cprNumber", cpr).build();
        System.out.println("User created");
        return Response.status(201).entity(response).type(MediaType.APPLICATION_JSON).build();
    }

    public Response deleteAccount(String accountId) throws QueueException {
        Response response;
        System.out.println("Started deleting");
        if(accounts.remove(accountId) != null) {
            //queueService.publishDeleteAccountEvent(accountId);
            System.out.println("Found account and deleted it");
            response = Response.status(204).build();
        } else {
            System.out.println("Found no account");
            response = Response.status(404).build();
        }
        return response;
    }
}
