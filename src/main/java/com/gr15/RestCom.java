package com.gr15;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello-resteasy")
public class RestCom {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String createUser() {



        User user = new User();



        return "Hello RESTEasy";
    }
}