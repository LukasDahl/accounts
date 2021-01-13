Feature: Account


  Scenario:  Create users
    Given a user with cpr "123" first name "bob" last name "bib" type "Costumer" and bankAccountId "9999"
    When the user signs up
    Then the sign up is successful

  Scenario:  Create users no cpr
    Given a user with first name "john" last name "bib" type "Costumer" and bankAccountId "9998"
    When the user signs up
    Then the client get a message saying "400 error: missing cprNumber"

  Scenario:  Create users and see users
    Given a user with cpr "124" first name "sob" last name "sib" type "Merchant" and bankAccountId "9997"
    And a successful sign up by the user
    When the user gets the list of accounts
    Then there is a account with the users information

  Scenario:  Create users and see users
    Given a user with cpr "125" first name "no" last name "more" type "Costumer" and bankAccountId "9996"
    And the user is in the system
    When the user deletes its account
    And the user gets the list of accounts
    Then there is no more an account with the users information


#  Scenario Outline:  Create users
#    Given a new user with cpr <cpr> first name <first name> last name <last name> type <type> and bankAccountId <bankAccountId>
#    When the user signs up
#    Then the client get a message saying <message>
#
#    Examples:
#      | cpr   | first name | last name | type       | bankAccountId | message        |
#      | "123" | "bob"      | "bib"     | "Costumer" | "9999"        | "user created" |


