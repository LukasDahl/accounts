package com.gr15.cucumber;

import com.gr15.RestCom;
import io.cucumber.java.en.*;
import org.junit.Assert;

import java.util.UUID;

public class DeleteUser {
    RestCom restCom = new RestCom();
    int numberOfUsers;

    @Given("there are users in the program")
    public void usersExists() {
        numberOfUsers = restCom.getDummyAccounts().size();
        Assert.assertTrue(numberOfUsers > 0);
    }

    @When("a user is deleted")
    public void deleteUser() {
        Assert.assertEquals("204", restCom.deleteAccount("000000-0000"));
    }

    @Then("There should be one user less")
    public void checkForOneLessUser(){
        Assert.assertTrue(numberOfUsers > restCom.getDummyAccounts().size());
    }
}
