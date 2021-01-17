/**
 * @author August
 */

package com.gr15.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import javax.json.*;

import static org.junit.Assert.*;

public class account {
    private AccountClient accountClient = new AccountClient();
    private JsonObject user, account, foundAccount;
    private JsonArray accounts;
    private String message;
    private int numberOfAccounts;


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

    @Given("the user with cpr {string} is in the system")
    public void userInSystem(String userCpr) {
        signUp();
        signUpTrue();
        foundAccount = accountClient.getUserWithCpr(userCpr);
    }

    @Given("there are accounts in the database with id {string}")
    public void accountWithIdInSystem(String accountId) {
        boolean inSys = false;
        getAccounts();
        for (JsonValue jsonVal : accounts) {
            JsonObject jsonObject = jsonVal.asJsonObject();
            System.out.println(jsonObject.getString("id"));
            if (jsonObject.getString("id").equals(accountId)) {
                inSys = true;
                this.foundAccount = jsonObject;
            }
        }
        assertTrue(inSys);
    }



    @When("the user signs up")
    public void signUp() {
        message = accountClient.postAccount(account);
    }

    @When("the user gets the list of accounts")
    public void getAccounts() {
        accounts = accountClient.getAccounts();
    }

    @When("the user deletes its account")
    public void deleteUser() {
        Assert.assertEquals("204", accountClient.deleteAccount(foundAccount.getString("id")));
    }

    @When("an account with id {string} is deleted")
    public void deleteUserWithId(String accountId) {
        Assert.assertEquals("204", accountClient.deleteAccount(accountId));
    }

    @When("the user gets its account by giving its cpr {string}")
    public void getUserWithCpr(String userCpr) {
        this.foundAccount = accountClient.getUserWithCpr(userCpr);
    }

    @Then("the client get a message saying {string}")
    public void chekMessage(String expectedMessage) {
        assertEquals(expectedMessage, message);
    }

    @Then("Then the user gets its account")
    public void chekAccount() {
        boolean accountFound = false;

        if (foundAccount.get("user").toString().equals(user.toString())
                && foundAccount.getString("type").equals(account.getString("type"))
                && foundAccount.getString("bankAccountId").equals(account.getString("bankAccountId"))
        )
            accountFound = true;
        assertTrue(accountFound);
    }


    @Then("there is no more an account with id {string}")
    public void accountWithIdNotInSystem(String accountId) {
        boolean inSys = true;
        getAccounts();
        for (JsonValue jsonVal : accounts) {
            JsonObject jsonObject = jsonVal.asJsonObject();
            if (jsonObject.getString("id").equals(accountId))
                inSys = false;
        }
        assertTrue(inSys);
    }

    @Then("the sign up is successful")
    public void signUpTrue() {
        System.out.println(message);
        assertEquals("201", message);
    }

    @Then("there is a account with the users information")
    public void checkAccounts() {
        boolean accountFound = false;
        System.out.println(accounts.toString());

        for (JsonValue jsonVal : accounts) {
            JsonObject jsonObject = jsonVal.asJsonObject();

            System.out.println(jsonObject.toString());

            if (jsonObject.get("user").toString().equals(user.toString())
                    && jsonObject.getString("type").equals(account.getString("type"))
                    && jsonObject.getString("bankAccountId").equals(account.getString("bankAccountId"))

            ){
                accountFound = true;
                this.foundAccount = jsonObject;
            }

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
        numberOfAccounts = accountClient.getAccounts().size();
        Assert.assertTrue(numberOfAccounts > 0);
    }

    @Then("There should be one account less")
    public void checkForOneLessAccount(){
        Assert.assertTrue(numberOfAccounts > accountClient.getAccounts().size());
    }
}
