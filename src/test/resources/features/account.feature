Feature: Account




  Scenario Outline:  Create users
    Given a new user with cpr <cpr> first name <first name> last name <last name> type <type> and bankAccountId <bankAccountId>
    When the user signs up
    Then the client get a message saying <message>

    Examples:
      | cpr   | first name | last name | type       | bankAccountId | message        |
      | "123" | "bob"      | "bib"     | "Costumer" | "9999"        | "user created" |


