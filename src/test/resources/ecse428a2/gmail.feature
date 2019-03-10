Feature: Gmail

  Scenario Outline: Attaching to an email (with text) image(s) and sending to recipient
    Given I am logged in
    When I compose an email
    And I enter a valid <valid email> recipient
    And I enter subject <text>
    When I select the file <file> I want to send
    And I send the email
    Then the email shall be sent
    And the email is in the sent folder with <valid email> recipient, and subject <text>

    Examples:
    #Values for body/Subject: body, subject, both
    #Values for file: small, medium, large, small \n medium
      | valid email                    | file      | text |
      | GTNECSE4281@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg | One |
      | GTNECSE4282@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg | Two |
      | GTNECSE4283@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\medium.jpg | Three|
      | GTNECSE4284@yahoo.com | C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg | Four  |
      | GTNECSE4285@yahoo.com | C:\Users\Gabriel\Documents\ecse428a2\pics\medium.jpg \n C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg| Five |

  #Sending image(s) without a subject and body
  Scenario Outline: Sending image(s) without a subject and body
    Given I am logged in
    When I compose an email
    And I enter a valid <valid email> recipient
    When I select the file <file> I want to send
    And I send the email
    Then the system shall warn me that there is no subject nor body
    When I acknowledge the warning
    Then the email shall be sent
    And the email is in the sent folder with only <valid email> recipient

    Examples:
    #Values for file: small, medium, large, small \n medium
      | valid email                    | file      |
      | GTNECSE4281@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg   |
      | GTNECSE4282@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\medium.jpg  |
      | GTNECSE4283@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\large.jpg   |
      | GTNECSE4284@yahoo.com | C:\Users\Gabriel\Documents\ecse428a2\pics\medium.jpg \n C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg|
      | GTNECSE4285@yahoo.com | C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg |


  Scenario Outline: Attaching an image that is too large
    Given I am logged in
    When I compose an email
    And I enter a valid <valid email> recipient
    And I enter subject <text>
    When I select the file <file> I want to send
    But the cumulative size of the files exceed the attachment limit
    Then the system shall attach the file(s) <file> as google drive link(s)
    When I send the email
    And I confirm drive permissions for <valid email>
    Then the email shall be sent
    And the email is in the sent folder with <valid email> recipient, and subject <text>

    Examples:
    #Values for body/Subject: body, subject, both
    #Values for file: xl2, xl1 \n large2, small \n xl2, large \n large2, small \n medium \n large \xl1
      | valid email                    | file      | text |
      | GTNECSE4281@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\xl2.jpg | One |
      | GTNECSE4282@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\xl1.jpg \n C:\Users\Gabriel\Documents\ecse428a2\pics\large2.jpg | Two |
      | GTNECSE4283@gmail.com | C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg \n C:\Users\Gabriel\Documents\ecse428a2\pics\xl2.jpg | Three|
      | GTNECSE4284@yahoo.com | C:\Users\Gabriel\Documents\ecse428a2\pics\large.jpg \n C:\Users\Gabriel\Documents\ecse428a2\pics\large2.jpg | Four |
      | GTNECSE4285@yahoo.com | C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg \n C:\Users\Gabriel\Documents\ecse428a2\pics\medium.jpg \n C:\Users\Gabriel\Documents\ecse428a2\pics\large.jpg \n C:\Users\Gabriel\Documents\ecse428a2\pics\xl1.jpg | Five |

  Scenario Outline: Attaching an image and sending to an invalid recipient
    Given I am logged in
    When I compose an email
    And I input an invalid <invalid email> recipient
    And I enter subject <text>
    When I select the file <file> I want to send
    And I send the email
    Then an error message shall be returned
    And the email shall not be sent

    Examples:
    #Values for body/Subject: body, subject, both
    #Values for file: small, medium, large, small \n medium
      | invalid email                    | file      | text |
      | GTNECSE4281.com | C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg | One |
      | GTNECSE4282.com | C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg | Two |
      | GTNECSE4283.com | C:\Users\Gabriel\Documents\ecse428a2\pics\medium.jpg | Three|
      | GTNECSE4284.com | C:\Users\Gabriel\Documents\ecse428a2\pics\large.jpg | Four |
      | GTNECSE4285.com | C:\Users\Gabriel\Documents\ecse428a2\pics\medium.jpg \n C:\Users\Gabriel\Documents\ecse428a2\pics\small.jpg| Five |