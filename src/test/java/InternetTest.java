import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class InternetTest {

    private WebDriver driver;
    private final static String BASE_URL = "http://the-internet.herokuapp.com/";
    private final static String TEXT_ENTER = "Hello world!!!";

    @BeforeMethod
    public void setup() {
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);

        driver.get(BASE_URL);

    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    //TODO Dynamic Loading Example 1
    @Test
    public void elementGotVisibleTest(){
        driver.findElement(By.linkText("Dynamic Loading")).click();
        driver.findElement(By.partialLinkText("Example 1")).click();
        WebElement startButton = driver.findElement(By.cssSelector("#start>button"));
        WebElement finishBlock = driver.findElement(By.id("finish"));
        startButton.click();

        Assert.assertFalse(startButton.isDisplayed(), "startButton did not disappear");

        WebDriverWait wait = new WebDriverWait(driver, 12);
        wait.until(ExpectedConditions.visibilityOf(finishBlock));

        Assert.assertTrue(finishBlock.isDisplayed(), "finishBlock is invisible");
        Assert.assertEquals(finishBlock.getText(), "Hello World!");
    }

    //TODO Dynamic Loading Example 2
    @Test
    public void elementAppearedTest() {
        driver.findElement(By.linkText("Dynamic Loading")).click();
        driver.findElement(By.partialLinkText("Example 2")).click();
        WebElement startButton = driver.findElement(By.cssSelector("#start>button"));
        startButton.click();

        Assert.assertFalse(startButton.isDisplayed(), "startButton did not disappear");

        By finish = By.id("finish");
        WebDriverWait wait = new WebDriverWait(driver, 12);
        wait.until(ExpectedConditions.visibilityOfElementLocated(finish));
        WebElement finishBlock = driver.findElement(finish);

        Assert.assertTrue(finishBlock.isDisplayed(), "finishBlock is invisible");
        Assert.assertEquals(finishBlock.getText(), "Hello World!");
    }

    //TODO Form Authentication (loginTest)
    @Test
    public void loginInTest() {
        driver.findElement(By.linkText("Form Authentication")).click();
        WebElement loginInButton = driver.findElement(By.cssSelector(".radius"));

        Assert.assertTrue(loginInButton.isDisplayed(), "button is invisible");

        driver.findElement(By.cssSelector("#username")).sendKeys("tomsmith");
        driver.findElement(By.cssSelector("#password")).sendKeys("SuperSecretPassword!");
        loginInButton.click();
        WebElement loginOutButton = driver.findElement(By.cssSelector(".button.secondary.radius"));

        Assert.assertTrue(loginOutButton.isDisplayed(), "something went wrong, button is invisible");
        Assert.assertEquals("http://the-internet.herokuapp.com/secure", driver.getCurrentUrl());
        Assert.assertTrue(driver.findElement(By.cssSelector("#flash")).getText().contains("You logged into a secure area!"), "text is not found, ou are not logged in");
    }

    //TODO Form Authentication (loginTest + logoutTest)
    @Test
    public void loginOutTest() throws InterruptedException {
        //homework

        Assert.assertTrue(driver.findElement(By.linkText("Form Authentication")).isDisplayed(), "Form Authentication is invisible");
        driver.findElement(By.linkText("Form Authentication")).click();

        WebElement loginInButton = driver.findElement(By.cssSelector(".radius"));
        Assert.assertTrue(loginInButton.isDisplayed(), "button is invisible");

        driver.findElement(By.cssSelector("#username")).sendKeys("tomsmith");
        driver.findElement(By.cssSelector("#password")).sendKeys("SuperSecretPassword!");

        loginInButton.click();
        Assert.assertEquals("http://the-internet.herokuapp.com/secure", driver.getCurrentUrl());
        Assert.assertTrue(driver.findElement(By.cssSelector("#flash")).getText().contains("You logged into a secure area!"), "text is not found, ou are not logged in");

        WebElement loginOutButton = driver.findElement(By.cssSelector(".button.secondary.radius"));
        Assert.assertTrue(loginOutButton.isDisplayed(), "something went wrong, button is invisible");

        loginOutButton.click();
//        line for test asserts
//        driver.findElement(By.cssSelector("#username")).sendKeys("tomsmith");
//        driver.findElement(By.cssSelector("#password")).sendKeys("SuperSecretPassword!");

        Assert.assertEquals("http://the-internet.herokuapp.com/login", driver.getCurrentUrl());
        Assert.assertTrue(driver.findElement(By.cssSelector("#flash")).getText().contains("You logged out of the secure area!"), "text is not found, ou are not logged out");

        Assert.assertTrue(driver.findElement(By.cssSelector("input[id='username']")).isDisplayed(), "username field is invisible"); //test
        Assert.assertTrue(driver.findElement(By.cssSelector("input[id='password']")).isDisplayed(), "password field is invisible");

        Assert.assertEquals(driver.findElement(By.cssSelector("input[id='username']")).getAttribute("value"), "", "field username includes text");
        Assert.assertEquals(driver.findElement(By.cssSelector("input[id='password']")).getAttribute("value"), "", "field password includes text");

    }

    //TODO Checkboxes
    @Test
    public void checkBoxTest() throws InterruptedException {
        driver.findElement(By.linkText("Checkboxes")).click();
        Assert.assertEquals("http://the-internet.herokuapp.com/checkboxes", driver.getCurrentUrl());
        List<WebElement> checkboxes = driver.findElements(By.cssSelector("input[type='checkbox']"));
        Helper.check(checkboxes.get(0));
        Helper.unCheck(checkboxes.get(1));
        Assert.assertTrue(checkboxes.get(0).isSelected());
        Assert.assertFalse(checkboxes.get(1).isSelected());
    }

    //TODO Dropdown
    @Test
    public void dropDownTest() {

        Assert.assertTrue(driver.findElement(By.linkText("Dropdown")).isDisplayed(), "Dropdown is invisible");
        driver.findElement(By.linkText("Dropdown")).click();
        Assert.assertEquals("http://the-internet.herokuapp.com/dropdown", driver.getCurrentUrl());

        WebElement select = driver.findElement(By.id("dropdown"));
        Select dropDown = new Select(select);

        List<WebElement> options = dropDown.getOptions();

        Assert.assertEquals(options.size(), 3);
        Assert.assertFalse(dropDown.isMultiple());
        Assert.assertEquals(dropDown.getFirstSelectedOption().getText(), "Please select an option");
        Assert.assertEquals(options.get(0).getText(), "Please select an option");
        Assert.assertFalse(options.get(0).isEnabled());
        Assert.assertEquals(options.get(1).getText(), "Option 1");
        Assert.assertEquals(options.get(2).getText(), "Option 2");

        dropDown.selectByValue("1");
        Assert.assertEquals(dropDown.getFirstSelectedOption().getText(), "Option 1");
    }

    //TODO JavaScript Alerts jsAlertsTest
    @Test
    public void jsAlertsTest() throws InterruptedException {
        Assert.assertTrue(driver.findElement(By.linkText("JavaScript Alerts")).isDisplayed(), "JavaScript Alerts is invisible");
        driver.findElement(By.linkText("JavaScript Alerts")).click();

        WebElement button = driver.findElement(By.cssSelector("button[onclick='jsAlert()']"));
        button.click();

        Alert alert = driver.switchTo().alert();
        Assert.assertEquals(alert.getText(), "I am a JS Alert");
        alert.accept();
        Assert.assertFalse(Helper.isAlertPresent(driver), "Alert is present");
        Thread.sleep(3000);
        Assert.assertEquals(driver.findElement(By.id("result")).getText(), "You successfuly clicked an alert");
    }

    //TODO JavaScript Alerts jsConfirmTest
    @Test
    public void jsConfirmTest() {
        Assert.assertTrue(driver.findElement(By.linkText("JavaScript Alerts")).isDisplayed(), "JavaScript Alerts is invisible");
        driver.findElement(By.linkText("JavaScript Alerts")).click();

        WebElement button = driver.findElement(By.cssSelector("button[onclick='jsConfirm()']"));
        button.click();

        Alert alert = driver.switchTo().alert();
        Assert.assertEquals(alert.getText(), "I am a JS Confirm");
        alert.dismiss();
        Assert.assertFalse(Helper.isAlertPresent(driver), "Alert is present");
        Assert.assertEquals(driver.findElement(By.id("result")).getText(), "You clicked: Cancel");
    }

    //TODO JavaScript Alerts jsPromptTest
    @Test
    public void jsPromptTest() {

        Assert.assertTrue(driver.findElement(By.linkText("JavaScript Alerts")).isDisplayed(), "JavaScript Alerts is invisible");
        driver.findElement(By.linkText("JavaScript Alerts")).click();

        WebElement button = driver.findElement(By.cssSelector("button[onclick='jsPrompt()']"));
        button.click();

        Alert alert = driver.switchTo().alert();
        Assert.assertEquals(alert.getText(), "I am a JS prompt");

        alert.sendKeys(TEXT_ENTER);
        alert.accept();

        Assert.assertFalse(Helper.isAlertPresent(driver), "Alert is present");
        Assert.assertEquals(driver.findElement(By.id("result")).getText(), "You entered: " + TEXT_ENTER);
    }

    //TODO JavaScript Alerts DLC
    ////попробовать сделать выбор чекбокса в окне алерта
    @Test
    public void jsPromptTestTest() {

        Assert.assertTrue(driver.findElement(By.linkText("JavaScript Alerts")).isDisplayed(), "JavaScript Alerts is invisible");
        driver.findElement(By.linkText("JavaScript Alerts")).click();

        WebElement button = driver.findElement(By.cssSelector("button[onclick='jsPrompt()']"));
        button.click();

        Alert alert = driver.switchTo().alert();
        Assert.assertEquals(alert.getText(), "I am a JS prompt");

        alert.sendKeys(TEXT_ENTER);
        alert.accept();

        button.click();
        driver.switchTo().alert();
        (new Actions(driver)).sendKeys(Keys.TAB).sendKeys(Keys.SPACE).perform();
        Assert.assertFalse(Helper.isAlertPresent(driver), "Alert is present");
        Assert.assertEquals(driver.findElement(By.id("result")).getText(), "You entered: " + TEXT_ENTER);
    }

    //TODO Frames (frame)
    @Test
    public void frameTest() {
        Assert.assertTrue(driver.findElement(By.linkText("Frames")).isDisplayed(), "Frames");
        driver.findElement(By.linkText("Frames")).click();

        Assert.assertTrue(driver.findElement(By.linkText("Nested Frames")).isDisplayed(), "Nested Frames");
        driver.findElement(By.linkText("Nested Frames")).click();

        driver.switchTo().frame("frame-bottom");
        Assert.assertEquals(driver.findElement(By.tagName("body")).getText().trim(), "BOTTOM");

        driver
                .switchTo().defaultContent()
                .switchTo().frame("frame-top")
                .switchTo().frame("frame-left");
        Assert.assertEquals(driver.findElement(By.tagName("body")).getText().trim(), "LEFT");

        driver
                .switchTo().parentFrame()
                .switchTo().frame("frame-middle");
        Assert.assertEquals(driver.findElement(By.id("content")).getText().trim(), "MIDDLE");

        driver
                .switchTo().parentFrame()
                .switchTo().frame("frame-right");
        Assert.assertEquals(driver.findElement(By.tagName("body")).getText().trim(), "RIGHT");
    }

    //TODO Frames (iframe)
    @Test
    //!!!!!!!!!!!!! доделать дома !!!!!!!!!!!!!!!!!!!!!!!!
    public void iFrameTest() throws InterruptedException {
        Assert.assertTrue(driver.findElement(By.linkText("Frames")).isDisplayed(), "Frames");
        driver.findElement(By.linkText("Frames")).click();

        Assert.assertTrue(driver.findElement(By.linkText("iFrame")).isDisplayed(), "iFrame");
        driver.findElement(By.linkText("iFrame")).click();

//        ((JavascriptExecutor)driver).executeScript()

        driver.manage().window().setSize(new Dimension(640, 480));

//        driver.findElement(By.cssSelector("#mceu_9 > button")).click();
        driver.switchTo().frame("mce_0_ifr");
        WebElement contextBox = driver.findElement(By.id("tinymce"));
        (new Actions(driver))
                .contextClick(contextBox)
                .sendKeys(Keys.ARROW_DOWN)
                .sendKeys(Keys.ARROW_DOWN)
                .sendKeys(Keys.ARROW_DOWN)
                .sendKeys(Keys.ARROW_DOWN)
                .sendKeys(Keys.ARROW_DOWN)
                .sendKeys(Keys.ARROW_DOWN)
                .sendKeys(Keys.ENTER)
                .sendKeys(Keys.DELETE)
                .perform();

        driver.switchTo().defaultContent();
        driver.findElement(By.cssSelector("#mceu_9 > button")).click();
        (new Actions(driver))
                .sendKeys("item 0")
                .sendKeys(Keys.ENTER)
                .sendKeys("item 1")
                .sendKeys(Keys.ENTER)
                .sendKeys("item 2")
                .perform();

        Assert.assertEquals(driver.findElement(By.id("mceu_29-0")).getText(), "ul");
        Assert.assertEquals(driver.findElement(By.id("mceu_29-1")).getText(), "li");

        driver.switchTo().frame("mce_0_ifr");
        List<WebElement> webElementList = driver.findElements(By.cssSelector("#tinymce>ul>li"));

        Assert.assertEquals(webElementList.get(0).getText(), "item 0");
        Assert.assertEquals(webElementList.get(1).getText(), "item 1");
        Assert.assertEquals(webElementList.get(2).getText(), "item 2");
    }
}
