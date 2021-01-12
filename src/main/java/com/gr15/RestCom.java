package com.gr15;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/hello-resteasy")
public class RestCom {
    private List<User> dummyUsers = new ArrayList<>();

    public RestCom(){
        dummyUsers.add(new User("000000-0000", "Jonatan", "Jonatansen",
                new Account("0", "costumer", "10")));
        dummyUsers.add(new User("000000-0001", "Hans", "Hansen",
                new Account("0", "costumer", "11")));
        dummyUsers.add(new User("000000-0002", "Micheal", "Jordan",
                new Account("0", "merchant", "12")));
        dummyUsers.add(new User("000000-0003", "August", "Augustsen",
                new Account("0", "costumer", "13")));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String createUser() {



        User user = new User();



        return "Hello RESTEasy";
    }

    @DELETE
    public String deleteUser(@PathParam(MediaType.APPLICATION_JSON) String userId) {





        return "200";
    }
}