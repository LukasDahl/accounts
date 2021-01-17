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
import javax.ws.rs.core.Response;

@Path("/accounts")
public class RestCom {

    private final AccountManager accountManager = AccountManager.getInstance();

    @GET
    public Response getUsers() {
        return accountManager.getUsers();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserWithCpr(@PathParam("id") String userCpr) {
        // TODO MAKE JSON
        return accountManager.getUserWithCpr(userCpr);
    }

    @POST
    public Response createUser(JsonObject jsonObject) {
        return accountManager.createUser(jsonObject);
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteAccount(@PathParam("id") String accountId) throws QueueException {
        return accountManager.deleteAccount(accountId);
    }
}