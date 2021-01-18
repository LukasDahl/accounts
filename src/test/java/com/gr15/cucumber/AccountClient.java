/**
 * @author Jonatan
 */

package com.gr15.cucumber;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class AccountClient {
    private WebTarget baseUrl;

    public AccountClient() {
        Client client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8025/");
        //baseUrl = client.target("http://g-15.compute.dtu.dk:8025/");
    }

    public String postAccount(JsonObject json) {
        Response response = baseUrl.path("accounts").request()
                .post(Entity.entity(json, MediaType.APPLICATION_JSON));
        if (String.valueOf(response.getStatus()).equals("400")){
            String responseMessage = response.readEntity(JsonObject.class).getString("errorMessage");
            return String.valueOf(response.getStatus()) + " " + responseMessage;
        }
        return String.valueOf(response.getStatus());
    }

    public JsonArray getAccounts(){
        Response response = baseUrl.path("accounts").request().get();
        return response.readEntity(JsonArray.class);
    }

    public String deleteAccount(String accountId) {
        Response response = baseUrl.path("accounts/" + accountId).request().delete();
        return String.valueOf(response.getStatus());
    }

    public JsonObject getUserWithCpr(String cprNumber) {
        Response response = baseUrl.path("accounts/" + cprNumber).request().get();
        return response.readEntity(JsonObject.class);
    }
}
