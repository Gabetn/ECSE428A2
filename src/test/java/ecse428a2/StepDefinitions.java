package ecse428a2;

import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.internal.WebElementToJsonConverter;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent; //Used for mocking user key strokes for windows file explorer
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StepDefinitions {

    // MY VARIABLES
    private WebDriver driver;
    private WebDriverWait waiter;
    private Alert alert;
    private final String PATH_TO_CHROME_DRIVER = "C:\\Users\\Gabriel\\Documents\\Applications\\chromedriver_win32\\chromedriver.exe";
    private final String LOG_IN = "https://accounts.google.com/AccountChooser?service=mail&continue=https://mail.google.com/mail/";
    private final String EMAIL = "GabrielNegashECSE428@gmail.com";
    private final String PASSWORD = "ECSE428A2";
    private final String CONFIRMATION = "Message sent.";
    private final String WARNING = "Send this message without a subject or text in the body?";

    @Before //TODO
    public void setUpTests(Scenario scenario){
        //I.e. if failed to login to sent tab
        if(scenario.getName().equals("Attaching an image and sending to an invalid recipient")){
            driver = null;
            return;
        }

        System.out.println("SetUp");
        if(!setUpHelper()){
            System.out.println("Failed to setUpHelper!"); //
            Assert.fail(); }

        goToSent();
        //Verify number of emails sent = 0
        WebElement message = waiter.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(text(),'No sent messages!')]")));
        //int count = driver.findElements(By.xpath("//div[contains(@aria-label,'Show more messages')]")).size();
        System.out.println("Message is: \t"+message.getText());
        Assert.assertTrue(message!=null);
        //NOTE: assumes this div is only visible when an email in the inbox is present
        driver.quit();
        driver = null; //reset driver
    }

    // Given
    @Given("^I am logged in$")
    public void givenLoggedIn() throws Throwable {
        setupSeleniumWebDrivers();

        goTo(LOG_IN);
        System.out.println("Entering email...");

        WebElement textField = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("identifierId")));
        System.out.print("Found!\n");

        enterText(textField,EMAIL,Keys.ENTER);

        System.out.println("Entering password...");
        textField = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.name("password")));
        System.out.print("Found!\n");

        enterText(textField,PASSWORD,Keys.ENTER);
        //ENSURE PROPER REDIRECT
        //https://mail.google.com/mail/#inbox
        String expected = "https://mail.google.com/mail/u/0/#inbox";//Note: assumes url doesn't change
        new WebDriverWait(driver, 15)
                .until(ExpectedConditions.urlToBe(expected));

        String currURL = driver.getCurrentUrl();
        System.out.print("Check2!\n");
        Assert.assertEquals(currURL,expected);
    }

    // When
    @When("^I compose an email$")
    public void iPressCompose() throws Throwable {
        try {
            System.out.println("Attempting to find Compose button... ");
            WebElement btn = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[contains(text(),'Compose')]"))); //TODO assumes english
            System.out.print("Found!\n");
            btn.click();
            System.out.println("Clicking Compose button.");//NOTE: Assumes that menu not minimized to only '+' symbol
        } catch (Exception e) {
            System.out.println("No Compose button found");
        }
    }

    @And("^I enter a valid ([^\"]*) recipient$")
    public void enterValidEmail(String validEmail) throws Throwable { //TODO: how to use scenario outline
        try {
            System.out.println("Attempting to find Recipients Field... ");
            WebElement textField = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//textarea[contains(@name,'to')]"))); //TODO assumes english
            System.out.print("Found!\n");

            System.out.println("Entering email...");
            enterText(textField,validEmail,Keys.ENTER); //TODO replace text w/ outline value
            System.out.println("Entered");
        } catch (Exception e) {
            System.out.println("No Recipient field found");
        }
    }
    @And("^I input an invalid ([^\"]*) recipient$")
    public void enterInvalidEmail(String invalidEmail) throws Throwable { //TODO: how to use scenario outline
        try {
            System.out.println("Attempting to find Recipients Field... ");
            WebElement textField = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//textarea[contains(@name,'to')]"))); //TODO assumes english
            System.out.print("Found!\n");

            System.out.println("Entering email...");
            enterText(textField,invalidEmail,Keys.TAB); //NOTE: assumes no autocorrection of email
            System.out.println("Entered");
        } catch (Exception e) {
            System.out.println("No Recipient field found");
        }
    }

    @And("^I enter subject ([^\"@]*)$")
    public void enterContent(String text) throws Throwable { //TODO: how to use scenario outline
        try {
            System.out.println("Attempting to find subject Field");
            WebElement textField = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//input[contains(@aria-label,'Subject')]"))); //NOTE assumes english
            System.out.print("Found!\n");
            enterText(textField, text, Keys.TAB); //TODO replace text w/ outline value
        } catch (Exception e) {
                System.out.println("No Recipient field found");
            }
    }



    @When("^I select the file ([^\"]*) I want to send$")
    public void selectFile(String fPath) throws Throwable { //TODO: how to use scenario outline
        iPressAttach();

        String[] names = fPath.split("\n");
        StringBuilder sb = new StringBuilder();
        for(String s : names){
            sb.append("\""+s.trim()+"\" ");
        }
        fPath = sb.toString();
        System.out.println(fPath);
        Robot r = new Robot();
        //copy to clipboard
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection( fPath ); //TODO: replace with fPath
        cb.setContents(stringSelection, stringSelection);
        //Paste to file explorer
        r.keyPress(KeyEvent.VK_CONTROL);
        r.keyPress(KeyEvent.VK_V);
        r.keyRelease(KeyEvent.VK_V);
        r.keyRelease(KeyEvent.VK_CONTROL);
        //Click Enter
        r.keyPress(KeyEvent.VK_ENTER);    // confirm by pressing Enter in the end
        r.keyRelease(KeyEvent.VK_ENTER);

        //Thread.sleep(3000); //TODO

    }

    @And("^I send the email$")
    public void iPressSend() throws Throwable {
        try {
            System.out.println("Attempting to find Send button... ");
            WebElement btn = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[contains(text(),'Send')]"))); //TODO assumes english
            System.out.print("Found!\n");
            //Thread.sleep(5000);
            btn.click();
            System.out.println("Clicking Send button.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("No Send button found");
        }
    }

    //Then
    @Then("^the email shall be sent$")
    public void isEmailSent() throws Throwable {
        System.out.println("Attempting to find Confirmation ...");
        //NOTE: Assumes span is only visible within html after email sent as opposed to simply being hidden
        WebElement dialog = (new WebDriverWait(driver, 20))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//span[contains(text(),'Message sent.')]"))); //NOTE assumes english
        System.out.print("Found!\n");
        System.out.println("Dialog Text: "+dialog.getText());
        Assert.assertEquals(dialog.getText(),CONFIRMATION); //NOTE: assumes confirmation message doesn't change
        //Thread.sleep(5000); //For user visibility purposes during execution

    }

    @Then("^the email is in the sent folder with ([^\"]*) recipient, and subject ([^\"@]*)$")
    public void checkSentFolder(String email, String subject) throws Throwable {
        goToSent();
        System.out.println("In Sent");
        email = email.substring(0,email.indexOf('@'));
        String ePath  = "//span[contains(@name,'"+email+"')]";
        String sPath  = "//span[contains(text(),'"+subject+"')]";
        Assert.assertTrue(foundByXPath(ePath) && foundByXPath(sPath));
        System.out.println("DONE!");
        //driver.quit();
    }

    @Then("^the email is in the sent folder with only ([^\"]*) recipient$")
    public void checkSentFolder(String email) throws Throwable {
        goToSent();
        email = email.substring(0,email.indexOf('@'));
        String ePath  = "//span[contains(@name,'"+email+"')]";
        Assert.assertTrue(foundByXPath(ePath));
        System.out.println("DONE!");
        //driver.quit();
    }


    @Then("^the system shall warn me that there is no subject nor body$")
    public void isAlerted() throws Throwable {
        System.out.println("Attempting to find alert ...");
        //NOTE: Assumes span is only visible within html after email sent as opposed to simply being hidden
        alert = driver.switchTo().alert(); //NOTE assumes english
        System.out.print("Found!\n");
        System.out.println("Alert Text: "+alert.getText());
        Assert.assertEquals(alert.getText(),WARNING); //NOTE: assumes confirmation message doesn't change
    }

    @Then("^the system shall attach the file\\(s\\) ([^\"]*) as google drive link\\(s\\)$")
    public void isAttached(String fPath) throws Throwable {
        String[] names = fPath.split("\n");
        WebElement link;
        boolean allFound = true;
        int i=0;
        for(String file : names){
            file = file.trim();
            int last = file.lastIndexOf('\\');
            System.out.println("Original: "+file+"\n \tlast: "+last+" remaining: "+(file.length()-last));
            file = file.substring(last+1);
            System.out.println(file);
            try{
                String query = "//span[contains(text(),'"+file+"')]";
                ////span[contains(text(),'Message sent.')]
                link = (new WebDriverWait(driver, 15))
                        .until(ExpectedConditions.elementToBeClickable(
                                By.xpath(query)));
                                //By.xpath("//div[contains(@class,'gmail_drive_chip')]["+i+"]"+query))); //NOTE assumes english
                System.out.print("Found!\n");
            } catch (Exception e) {
                allFound = false;
                System.out.println("File "+file+" not found as google drive link");
                break;
            }
        }
        Assert.assertTrue(allFound);
    }

    @And("^I confirm drive permissions for ([^\"]*)$")
    public void iGivePermissions(String validEmail) throws Throwable {
        if(validEmail.equals(EMAIL)){
            //in case of sending email to self, gmail does not prompt drive permissions
            //NOTE: dependent on implementation of drive.
        }else{
            WebElement iFrame = (new WebDriverWait(driver, 15))
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//iframe[contains(@class,'Qr-Mr-Jz-avO')]"))); //NOTE assumes doesn't change

            driver.switchTo().frame(iFrame);
            try {
                System.out.println("Attempting to find drive confirmation button... ");
                WebElement btn = (new WebDriverWait(driver, 10))
                        .until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//span[contains(text(),'Send')]"))); //NOTE assumes english
                System.out.print("Found!\n");
                Thread.sleep(1000);
                btn.click();
                System.out.println("Confirming");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            driver.switchTo().defaultContent(); //TODO: ensure works, might need to replace with frame(0);
        }

    }



    @When("^I acknowledge the warning$")
    public void iPressOk() throws Throwable {
        System.out.println("Attempting to find alert... ");
        if(alert != null){
            alert.accept();
        } else{
            System.out.println("Relocating alert... ");
            driver.switchTo().alert().accept();
        }
        System.out.println("Accepted alert... ");
    }

    @But("^the cumulative size of the files exceed the attachment limit$")
    public void isTooBig() throws Throwable {
        System.out.println("Attempting to find message ...");
        //NOTE: Assumes span is only visible within html after email sent as opposed to simply being hidden
        WebElement message = (new WebDriverWait(driver, 15))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[contains(@class,'HyIydd')]"))); //NOTE assumes english
        System.out.print("Found!\n");
        String s = message.getText();
        //Wait until the images are fully loaded
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[contains(@class,'HyIydd')]")));

        //Validate div actually contains warning message
        Assert.assertTrue(searchForText(s,"larger than"));
    }

    @Then("^an error message shall be returned$")
    public void isError() throws Throwable {
        System.out.println("Attempting to find error ...");
        WebElement error = (new WebDriverWait(driver, 15))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//span[contains(text(),'Error')]"))); //NOTE assumes english
        System.out.print("Found!\n");
        Assert.assertTrue(searchForText(error.getText(),"Error"));
    }

    @Then("^the email shall not be sent$")
    public void isEmailNotSent() throws Throwable {
        System.out.println("Acknowledging Error ...");
        WebElement ok = (new WebDriverWait(driver, 5))
                .until(ExpectedConditions.elementToBeClickable(
                        By.name("ok"))); //NOTE assumes english
        System.out.print("Found!\n");
        ok.click();
        //I.e. assert that the confirmation message does not show up, thus the email was not sent
        Assert.assertTrue(driver.findElements(By.xpath("//span[contains(text(),'Message sent.')]")).isEmpty());
        //Thread.sleep(5000); //For user visibility purposes during execution
       // driver.quit();
    }

    @After //TODO
    public void tearDownTests(Scenario scenario) throws InterruptedException {
        //I.e. if failed to login to sent tab
        //driver = null; //reset driver
/*        if(!setupTearDownHelper()){
            System.out.println("Failed to setup @After");
            driver.quit();
            Assert.fail();
        }*/
        //No email sent thus nothing to clean
        if(scenario.getName().equals("Attaching an image and sending to an invalid recipient")){
            driver.quit();
            return;
        }
        System.out.println("TearDown");
        goToSent();
        //DELETE any sent emails
        if(!driver.findElements(By.xpath("//div[contains(@aria-label,'Show more messages')]")).isEmpty()){
            //Click select all button
            System.out.println("Finding select button");

            //List<WebElement> boxes = driver.findElements(By.xpath("//div[contains(@class,'T-Jo-auh')]"));
            //boxes.get(1).click();
            List<WebElement> selector = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'T-Jo-auh')]")));
            System.out.println("Selector size:\t"+selector.size());
            WebElement parent = selector.get(1).findElement(By.xpath(".."));
            System.out.println(parent.getAttribute("innerHTML"));
            parent = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(parent));
            System.out.println("click");
            parent.click();
            //.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class,'G-asx T-I-J3 J-J5-Ji')]")));
            //driver.findElements(By.xpath("//span[contains(@role,'checkbox')]")).get(0);
            //.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(@class,'T-Jo J-J5-Ji')]")));

            //selector.click();
            //driver.findElement(By.xpath("//li[@class='select2-search-field']//input[@class='select2-input' and contains(@id,'id_autogen')]")).click();

            //Click delete button
            System.out.println("Finding delete button");
            WebElement delete = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@aria-label,'Delete')]")));
            delete.click();
            Thread.sleep(1000);
        }
        driver.quit();
    }

    //----------------------------------------OLD--------------------------------------------

    /**
     *     Helper functions
      */
    private void setupSeleniumWebDrivers() throws MalformedURLException {
        if (driver == null) {
            System.out.println("Setting up ChromeDriver... ");
            System.setProperty("webdriver.chrome.driver", PATH_TO_CHROME_DRIVER);
            driver = new ChromeDriver();
            waiter = new WebDriverWait(driver, 10);
            System.out.print("Done!\n");
        }
    }

    private void enterText(WebElement textField,String str,Keys fin) throws InterruptedException {
        //textField.sendKeys(Keys.TAB);
        textField.clear();
        textField.sendKeys(str);
        Thread.sleep(300);
        textField.sendKeys(fin);
    }

    public void iPressAttach() throws Throwable {
        try {
            System.out.println("Attempting to find Attach button... ");
            WebElement btn = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[contains(@class,'a1 aaA aMZ')]"))); //Note: assumes class name doesn't change
            System.out.print("Found!\n");
            btn.click();
            Thread.sleep(1000); //TODO: deal with timing
            System.out.println("Clicking Attach button.");
        } catch (Exception e) {
            System.out.println("No Attach button found");
        }
    }

    private boolean foundByXPath(String path) throws Throwable{
        try{
            System.out.println("Finding: \t"+path);
            WebElement textField = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath(path)));
            System.out.println("Found");
            return true;
        }catch (Exception e){
            System.out.println("Not Found by Xpath: \t"+path);
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean searchForText(String text, String textToFind) { //TODO: is this necessary?
        return text.contains(textToFind);
    }

    private void goTo(String url) {
        if (driver != null) {
            System.out.println("Going to " + url);
            driver.get(url);
        }
    }

    //This helper goes to the sent tab
    private boolean setUpHelper(){
        try{
            givenLoggedIn();
        } catch (Throwable throwable) {
            System.out.println("Failed to login");
            throwable.printStackTrace();
            return false;
        }
        return true;
    }

    private void goToSent(){
        //SWITCH TO SENT TAB
        System.out.println("Attempting to find Send tab... ");
        WebElement tab = (new WebDriverWait(driver, 15))
                .until(ExpectedConditions.elementToBeClickable(
                        By.linkText("Sent"))); //TODO assumes english
        System.out.print("Found!\n");
        System.out.print("Clicking!\n");
        tab.click();
    }
}
