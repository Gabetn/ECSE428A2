Feature: Gmail

  #This scenario is just for testing purposes
  Scenario Outline: Testing
    Given I am logged in
    When I click the ‘Compose’ button
    And I enter a valid <valid email> in the ‘to’ section
    And I click the ‘Attach files’ button
    When I select the file <file> I want to send
    Then We Gucci

    Examples:
    | valid email    | file   |
    | abcd.com       | C:\Users\Gabriel\Documents\ecse428a2\pics\paper.jpg |
    #| efgh@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\satelite.jpg |


  Scenario: Attaching one image and sending to recipient
    Given I am logged in
    When I click the ‘Compose’ button
    And I enter a valid <valid email> in the ‘to’ section
    And I click the ‘Attach files’ button
    When I select the file <file> I want to send
    And I click the ‘Send’ button
    Then the email shall be sent

  Scenario: Attaching multiple images and sending to recipient
    Given I am logged in
    When I click the ‘Compose’ button
    And I enter a valid <valid email> in the ‘to’ section
    And I click the ‘Attach files’ button
    When I select the files I want to send
    And I click the ‘Send’ button
    Then the email shall be sent

  Scenario: Attaching an image that is too large
    Given I am logged in
    When I click the ‘Compose’ button
    And I enter a valid <valid email> in the ‘to’ section
    And I click the ‘Attach files’ button
    When I select the files I want to send
    But the cumulative size of the files exceed the attachment limit
    And I click the ‘Send’ button
    Then the email shall be sent

  Scenario Outline: Attaching an image and sending to an invalid recipient
    Given I am logged in
    When I click the ‘Compose’ button
    And I enter an invalid <invalid email> in the ‘to’ section
    And I click the ‘Attach files’ button
    When I select the files I want to send
    And I click the ‘Send’ button
    Then an error message shall be returned
    And the email shall not be sent
    Examples:
      | invalid email |
      | abcd.com |
      | efgh.com |
      | ijkl.com |