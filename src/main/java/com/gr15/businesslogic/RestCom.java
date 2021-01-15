/**
 * @author August
 */

package com.gr15.businesslogic;

import com.gr15.businesslogic.exceptions.QueueException;

import javax.json.*;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/accounts")
public class RestCom {

    private AccountManager accountManager;

    public RestCom(){
        accountManager = AccountManager.getInstance();
    }

    @GET
    public JsonArray getUsers() {
        return accountManager.getUsers();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getUserWithCpr(@QueryParam("userCpr") String userCpr) {
        return accountManager.getUserWithCpr(userCpr);
    }

    @POST
    public String createUser(JsonObject jsonObject) {
        return accountManager.createUser(jsonObject);
    }

    @DELETE
    public String deleteAccount(String accountId) throws QueueException {
        return accountManager.deleteAccount(accountId);
    }
}