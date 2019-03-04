Feature: Gmail

  #This scenario is just for testing purposes
  Scenario: Testing
    Given I am logged in
    When I click the ‘Compose’ button
    And I enter a valid email in the ‘to’ section
    Then We Gucci

  Scenario: Attaching one image and sending to recipient
    Given I am logged in
    When I click the ‘Compose’ button
    And I enter a valid email in the ‘to’ section
    And I click the ‘Attach files’ button
    When I select the file I want to send
    And I click the ‘Send’ button
    Then the email shall be sent

  Scenario: Attaching multiple images and sending to recipient
    Given I am logged in
    When I click the ‘Compose’ button
    And I enter a valid email in the ‘to’ section
    And I click the ‘Attach files’ button
    When I select the files I want to send
    And I click the ‘Send’ button
    Then the email shall be sent

  Scenario: Attaching an image that is too large
    Given I am logged in
    When I click the ‘Compose’ button
    And I enter a valid email in the ‘to’ section
    And I click the ‘Attach files’ button
    When I select the files I want to send
    But the cumulative size of the files exceed the attachment limit
    And I click the ‘Send’ button
    Then the email shall be sent

  Scenario: Attaching an image and sending to an invalid recipient
    Given I am logged in
    When I click the ‘Compose’ button
    And I enter an invalid email in the ‘to’ section
    And I click the ‘Attach files’ button
    When I select the files I want to send
    And I click the ‘Send’ button
    Then an error message shall be returned
    And the email shall not be sent
