/**
 * @author August
 */

package com.gr15.cucumber;

import com.gr15.businesslogic.Account;
import com.gr15.businesslogic.RestCom;
import com.gr15.businesslogic.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import javax.json.*;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class account {
    private RestCom restCom = new RestCom();
    private JsonObject user, account;
    private JsonArray accounts;
    private String message;
    private int numberOfAccounts;
    private HashMap<String, Account> accountsMap = new HashMap<>();

    public account() {
        Account testAccount1 = new Account("Merchant", "0",
                new User("000000-0000", "Jonatan", "Jonatansen"));
        Account testAccount2 = new Account("Costumer", "1",
                new User("000000-0001", "August", "Augustsen"));
        Account testAccount3 = new Account("Costumer", "2",
                new User("000000-0002", "Micheal", "Jordan"));

        accountsMap.put(testAccount1.getId().toString(), testAccount1);
        accountsMap.put(testAccount2.getId().toString(), testAccount2);
        accountsMap.put(testAccount3.getId().toString(), testAccount3);
    }

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
        this.message = this.restCom.createUser(account, accountsMap);
    }

    @When("the user gets the list of accounts")
    public void getAccounts() {
        accounts = this.restCom.getUsers(accountsMap);
    }

    @When("the user deletes its account")
    public void deleteUser() {
        restCom.deleteAccount(user.getString("cprNumber"), accountsMap);
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

    @Given("there are accounts in the database")
    public void accountExists() {
        numberOfAccounts = accountsMap.size();
        Assert.assertTrue(numberOfAccounts > 0);
    }

    @When("an account is deleted")
    public void deleteAccount() {
        //accountsMap.get(accountsMap.values().toArray()[0]).getId().toString()
        // TODO - get one of the ids
        Assert.assertEquals("204", restCom.deleteAccount("0", accountsMap));
    }

    @Then("There should be one account less")
    public void checkForOneLessAccount(){
        Assert.assertTrue(numberOfAccounts > accountsMap.size());
    }
}
