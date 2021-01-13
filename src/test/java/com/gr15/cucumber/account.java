package com.gr15.cucumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gr15.Account;
import com.gr15.RestCom;
import com.gr15.User;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import javax.json.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class account {
    RestCom restCom = new RestCom();
    JsonObject user, account;
    JsonArray accounts;
    String message;

    @Given("a user with cpr {string} first name {string} last name {string} type {string} and bankAccountId {string}")
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

    @Given("a user with first name {string} last name {string} type {string} and bankAccountId {string}")
    public void newUserNoCpr(String first, String last, String type, String bank) {
        JsonObjectBuilder userBuild = Json.createObjectBuilder()
                .add("firstName", first)
                .add("lastName", last);
        this.user = userBuild.build();

        JsonObjectBuilder account = Json.createObjectBuilder()
                .add("type", type)
                .add("bankAccountId", bank)
                .add("user", user);

        this.account = account.build();

    }

    @Given("a successful sign up by the user")
    public void signUpSuccessful() {
        signUp();
        signUpTrue();
    }

    @Given("the user is in the system")
    public void userInSystem() {
        signUp();
        signUpTrue();
        getAccounts();
        checkAccounts();
    }

    @When("the user signs up")
    public void signUp() {
        this.message = this.restCom.createUser(account);
    }

    @When("the user gets the list of accounts")
    public void getAccounts() {
        accounts = this.restCom.getUsers();
    }

    @When("the user deletes its account")
    public void deleteUser() {
        restCom.deleteUser(user.getString("cprNumber"));
    }

    @Then("the client get a message saying {string}")
    public void chekMessage(String expededMessage) {
        assertEquals(message, expededMessage);
    }


    @Then("the sign up is successful")
    public void signUpTrue() {
        assertEquals("user created", message);
    }

    @Then("there is a account with the users information")
    public void checkAccounts() {
        boolean accountFound = false;

        for (JsonValue jsonVal : accounts) {
            JsonObject jsonObject = jsonVal.asJsonObject();

            if (jsonObject.get("user").toString().equals(user.toString())
                    && jsonObject.getString("type").equals(account.getString("type"))
                    && jsonObject.getString("bankAccountId").equals(account.getString("bankAccountId"))

            )
                accountFound = true;
        }

        assertTrue(accountFound);
    }

    @Then("there is no more an account with the users information")
    public void checkAccountsFalse() {
        boolean accountFound = true;


        for (JsonValue jsonVal : accounts) {
            JsonObject jsonObject = jsonVal.asJsonObject();

            if (jsonObject.get("user").toString().equals(user.toString())
                    && jsonObject.getString("type").equals(account.getString("type"))
                    && jsonObject.getString("bankAccountId").equals(account.getString("bankAccountId"))

            )
                accountFound = false;
        }

        assertTrue(accountFound);
    }
}
