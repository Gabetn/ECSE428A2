package ecse428a2;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent; //Used for mocking user key strokes for windows file explorer
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

public class StepDefinitions {

    // Variables
    private final String PRODUCT_URL = "https://www.amazon.ca/Monoprice-115365-Select-Mini-Printer/dp/B01FL49VZE/ref=sr_1_1?ie=UTF8&qid=1488132110&sr=8-1&keywords=3d+printer";
    private final String PRODUCT_NAME = "Monoprice 115365 Monoprice Select Mini 3D Printer";
    private final String DELETE_BTN_NAME = "submit.delete.C3NLW69582M4B4";
    private final String CART_URL = "https://www.amazon.ca/gp/cart/view.html/ref=nav_cart";
    private final String ADD_TO_CART_BTN = "add-to-cart-button";
    private final String ACTIVE_CART = "sc-active-cart";
    private final String CHECKOUT_BTN = "sc-proceed-to-checkout";

    // MY VARIABLES
    private WebDriver driver;
    private final String PATH_TO_CHROME_DRIVER = "C:\\Users\\Gabriel\\Documents\\Applications\\chromedriver_win32\\chromedriver.exe";
    private final String LOG_IN = "https://accounts.google.com/AccountChooser?service=mail&continue=https://mail.google.com/mail/";
    private final String EMAIL = "GabrielNegashECSE428@gmail.com";
    private final String PASSWORD = "ECSE428A2";
    private final String CONFIRMATION = "Message Sent";

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
        String expected = "https://mail.google.com/mail/#inbox";
        new WebDriverWait(driver, 10)
                .until(ExpectedConditions.urlToBe(expected));

        String currURL = driver.getCurrentUrl();
        Assert.assertEquals(currURL,expected);
    }

    // When
    @When("^I click the ‘Compose’ button$")
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

    @And("^I enter a valid ([^\"]*) in the ‘to’ section$")
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
    @And("^I enter a invalid ([^\"]*) in the ‘to’ section$")
    public void enterInvalidEmail(String invalidEmail) throws Throwable { //TODO: how to use scenario outline
        try {
            System.out.println("Attempting to find Recipients Field... ");
            WebElement textField = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//textarea[contains(@name,'to')]"))); //TODO assumes english
            System.out.print("Found!\n");

            System.out.println("Entering email...");
            enterText(textField,invalidEmail,Keys.ENTER); //TODO replace text w/ outline value
            System.out.println("Entered");
        } catch (Exception e) {
            System.out.println("No Recipient field found");
        }
    }

    @And("^I enter ([^\"@]*) in ([^\"@]*) section$")
    public void enterContent(String text, String location) throws Throwable { //TODO: how to use scenario outline
        try {
            WebElement textField = findElement(location);
            System.out.println("Entering text...");
            enterText(textField,text,Keys.TAB); //TODO replace text w/ outline value
            System.out.println("Entered");
            //Note: In case of both, first subject is done by default, then body
            if (location.equals("both")){
                textField = findElement("body");
                System.out.println("Entering body...");
                enterText(textField,text,Keys.ENTER);
            }
        } catch (Exception e) {
            System.out.println("No Recipient field found");
        }
    }

    // When
    @And("^I click the ‘Attach files’ button$")
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

    @When("^I select the file ([^\"]*) I want to send$")
    public void selectFile(String fPath) throws Throwable { //TODO: how to use scenario outline
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

        Thread.sleep(3000); //TODO

    }

    @And("^I click the ‘Send’ button$")
    public void iPressSend() throws Throwable {
        try {
            System.out.println("Attempting to find Send button... ");
            WebElement btn = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[contains(text(),'Send')]"))); //TODO assumes english
            System.out.print("Found!\n");
            btn.click();
            System.out.println("Clicking Send button.");
        } catch (Exception e) {
            System.out.println("No Send button found");
        }
    }

    //Then
    @Then("^the email shall be sent$")
    public void isEmailSent() throws Throwable {
        System.out.println("Attempting to find Confirmation ...");
        //NOTE: Assumes span is only visible within html after email sent as opposed to simply being hidden
        WebElement dialog = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//span[contains(text(),'Message Sent')]"))); //TODO assumes english
        System.out.print("Found!\n");

        Assert.assertEquals(dialog.getText(),CONFIRMATION);
        Thread.sleep(5000); //For user visibility purposes during execution
        driver.quit();
    }

    //----------------------------------------OLD--------------------------------------------
    @Then("^We Gucci$")
    public void trivialEnd() throws Throwable {
        Assert.assertTrue(true);
        //driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
        Thread.sleep(5000);
        driver.quit();
    }


    /**
     *     Helper functions
      */
    private void setupSeleniumWebDrivers() throws MalformedURLException {
        if (driver == null) {
            System.out.println("Setting up ChromeDriver... ");
            System.setProperty("webdriver.chrome.driver", PATH_TO_CHROME_DRIVER);
            driver = new ChromeDriver();
            System.out.print("Done!\n");
        }
    }

    private void enterText(WebElement textField,String str,Keys fin){
        //textField.sendKeys(Keys.TAB);
        textField.clear();
        textField.sendKeys(str);
        textField.sendKeys(fin);
    }

    private WebElement findElement(String type){
        WebElement el=null;

        if(type.equals("body")){
            System.out.println("Attempting to find "+type+" Field... ");
            el = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[contains(@aria-label,'Message Body')]"))); //NOTE assumes english
            System.out.print("Found!\n");
        } else { //subject
            System.out.println("Attempting to find subject Field");
            el = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.elementToBeClickable(
                            By.xpath("//input[contains(@aria-label,'Subject')]"))); //NOTE assumes english
            System.out.print("Found!\n");
        }
        return el;
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



}
