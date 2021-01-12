package com.gr15;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/hello-resteasy")
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

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String createUser() {



        User user = new User();



        return "Hello RESTEasy";
    }

    @DELETE
    public String deleteUser(@PathParam(MediaType.APPLICATION_JSON) String userCprNumber) {
        for (int i = 0; i < dummyUsers.size(); i++) {
            if (dummyUsers.get(i).getCprNumber().equals(userCprNumber)) {
                dummyUsers.remove(i);
                return "204";
            }
        }
        return "404";
    }

    public List<User> getDummyUsers() {
        return dummyUsers;
    }
}