Feature: Delete

  Scenario: Delete
    Given there are users in the service
    When a user is deleted
    Then There should be one user less
