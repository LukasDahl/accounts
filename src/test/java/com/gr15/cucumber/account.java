package com.gr15.cucumber;

import com.gr15.Account;
import com.gr15.RestCom;
import com.gr15.User;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import static org.junit.Assert.assertEquals;

public class account {
    RestCom restCom = new RestCom();
    JsonObject user, account;
    String message;

    @Given("a new user with cpr {string} first name {string} last name {string} type {string} and bankAccountId {string}")
    public void newUser(String cpr, String first, String last, String type, String bank) {
        JsonObjectBuilder userBuild = Json.createObjectBuilder()
                .add("cprNumber", cpr)
                .add("firstName", first)
                .add("lastName", last);
        this.user = userBuild.build();

        JsonObjectBuilder account = Json.createObjectBuilder()
                .add("type", type)
                .add("bankAccountId", bank)
                .add("user", user);

        this.account = account.build();

    }

    @When("the user signs up")
    public void signUp(){
        this.message = this.restCom.createUser(account);
    }

    @Then("the client get a message saying {string}")
    public void chekMessage(String expededMessage){
        assertEquals(message, expededMessage);
    }
}
