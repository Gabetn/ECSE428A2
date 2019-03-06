Feature: Gmail

  Scenario Outline: Attaching to an email (with text) image(s) and sending to recipient
    Given I am logged in
    When I click the ‘Compose’ button
    And I enter a valid <valid email> in the ‘to’ section
    And I enter <text> in <section> section
    And I click the ‘Attach files’ button
    When I select the file <file> I want to send
    And I click the ‘Send’ button
    Then the email shall be sent

    Examples:
    #Values for body/Subject: body, subject, both
    #Values for file: small, medium, large, small \n medium
      | valid email                    | file      | text | section|
      | GabrielNegashECSE428@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg | One | body |
      | GTNECSE4281@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg | Two | body |
      | GTNECSE4282@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\medium.jpg | Three| subject |
      | GTNECSE4283@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\large.jpg | Four | both |
      | GTNECSE4284@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\medium.jpg \n C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg| Five | subject |

  Scenario Outline: Sending image(s) without a subject and body
    Given I am logged in
    When I click the ‘Compose’ button
    And I enter a valid <valid email> in the ‘to’ section
    And I click the ‘Attach files’ button
    When I select the file <file> I want to send
    And I click the ‘Send’ button
    Then the system shall warn me that there is no subject nor body
    When I click the ‘Ok’ button
    Then the email shall be sent
    Examples:
    #Values for file: small, medium, large, small \n medium
      | valid email                    | file      |
      | GabrielNegashECSE428@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg |
      | GTNECSE4281@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg   |
      | GTNECSE4282@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\medium.jpg  |
      | GTNECSE4283@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\large.jpg   |
      | GTNECSE4284@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\medium.jpg \n C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg|


  Scenario Outline: Attaching an image that is too large
    Given I am logged in
    When I click the ‘Compose’ button
    And I enter a valid <valid email> in the ‘to’ section
    And I enter <text> in <section> section
    And I click the ‘Attach files’ button
    When I select the file <file> I want to send
    But the cumulative size of the files exceed the attachment limit
    Then the system shall attach the file(s) <file> as google drive link(s)
    When I click the ‘Send’ button
    And I confirm drive permissions for <valid email>
    Then the email shall be sent

    Examples:
    #Values for body/Subject: body, subject, both
    #Values for file: xl2, xl1 \n large2, small \n xl2, large \n large2, small \n medium \n large \xl1
      | valid email                    | file      | text | section|
      | GabrielNegashECSE428@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\xl2.jpg | One | body |
      | GTNECSE4281@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\xl1.jpg \n C:\Users\Gabriel\Documents\ecse428a2\pics\large2.jpg | Two | body |
      | GTNECSE4282@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg \n C:\Users\Gabriel\Documents\ecse428a2\pics\xl2.jpg | Three| subject |
      | GTNECSE4283@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\large.jpg \n C:\Users\Gabriel\Documents\ecse428a2\pics\large2.jpg | Four | both |
      | GTNECSE4284@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg \n C:\Users\Gabriel\Documents\ecse428a2\pics\medium.jpg \n C:\Users\Gabriel\Documents\ecse428a2\pics\large.jpg \n C:\Users\Gabriel\Documents\ecse428a2\pics\xl1.jpg | Five | subject |

  Scenario Outline: Attaching an image and sending to an invalid recipient
    Given I am logged in
    When I click the ‘Compose’ button
    And I input an invalid <invalid email> in the ‘to’ section
    And I enter <text> in <section> section
    And I click the ‘Attach files’ button
    When I select the file <file> I want to send
    And I click the ‘Send’ button
    Then an error message shall be returned
    And the email shall not be sent

    Examples:
    #Values for body/Subject: body, subject, both
    #Values for file: small, medium, large, small \n medium
      | invalid email                    | file      | text | section|
      | GabrielNegashECSE428.com | C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg | One | body |
      | GTNECSE4281.com | C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg | Two | body |
      | GTNECSE4282.com | C:\Users\Gabriel\Documents\ecse428a2\pics\medium.jpg | Three| subject |
      | GTNECSE4283.com | C:\Users\Gabriel\Documents\ecse428a2\pics\large.jpg | Four | both |
      | GTNECSE4284.com | C:\Users\Gabriel\Documents\ecse428a2\pics\medium.jpg \n C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg| Five | subject |