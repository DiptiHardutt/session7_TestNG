package session7_TestNG;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Practice {

	WebDriver driver;
	String browser;
	String environment;

	// Login Data
	String userName;
	String password;

	// Element List
	By USERNAME_FIELD = By.cssSelector("#user_name");
	By PASSWORD_FIELD = By.xpath("//input[@name='password']");
	By LOGIN_FIELD = By.cssSelector("button[id='login_submit']");
	By DASHBOARD_HEADER_FIELD = By.xpath("//strong[contains (text(),'Dashboard')]");
	By CUSTOMER_FIELD = By.xpath("//span[normalize-space()='Customers']");
	By ADD_CUSTOMER_FIELD = By.xpath("//span[contains(text(),'Add Customer')]");
	By NEW_CUSTOMER_HEADER_FIELD = By.xpath("//strong[contains(text(),'New Customer')]");
	By FULL_NAME_FIELD = By.name("name");
	By COMPANY_FIELD = By.cssSelector("select[name='company_name']");
	By EMAIL_FIELD = By.name("email");
	By PHONE_FIELD = By.id("phone");
	By ADDRESS_FIELD = By.name("address");
	By CITY_FIELD = By.name("city");
	By ZIP_FIELD = By.id("port");
	By ZIP_LESS_CHAR_ERROR_FIELD = By.xpath("//p[contains(text(),'less than 5 digits')]");
	By ZIP_MORE_CHAR_ERROR_FIELD = By.xpath("//p[contains(text(),'more than 9 digits')]");
	
	
	By COUNTRY_FIELD = By.cssSelector("select[name='country']");
	By GROUP_FIELD = By.id("customer_group");
	By SAVE_FIELD = By.id("save_btn");
	By LIST_CUSTOMER_FIELD = By.cssSelector("a[title='List Customers']");
	By SEARCH_FIELD = By.name("globalsearch");

	// Test Data / Mock Data
	String fullName ="abc" +randomNum(999);
	String companyName = "Techfios";
	String email = "abc@gmail.com";
	String phone = "2345788";
	String address = "test address";
	String city = "Zambia";
	String zipCodeMin = "1234";
	String zipCodeMax = "1234789054";
	String correctZipCode = "123654";
	String expZipLessCharText = "Error: Do not allow less than 5 digits";
	String expZipMoreCharText = "Error: Do not allow more than 9 digits";
	String country = "Zambia";
	String group = "Selenium";
			
	

	@BeforeClass
	public void readConfiguration() {
		// You can read the file with the following:-
		// Buffered Reader //Input Stream // File Reader //Scanner
		try {
			Properties prop = new Properties();
			InputStream input = new FileInputStream("config.properties");
			prop.load(input);
			browser = prop.getProperty("browser");
			environment = prop.getProperty("url");
			userName = prop.getProperty("userName");
			password = prop.getProperty("password");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@BeforeMethod
	public void init() {
		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", "drivers\\geckodriver.exe");
			driver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("edge")) {
			System.setProperty("webdriver.edge.driver", "drivers\\msedgedriver.exe");
		} else {
			System.out.println("Please provide the valid browser");
		}
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		driver.get(environment);

	}
	
//	@Test
	public void loginTest() {
		driver.findElement(USERNAME_FIELD).sendKeys(userName);
		driver.findElement(PASSWORD_FIELD).sendKeys(password);
		driver.findElement(LOGIN_FIELD).click();
		Assert.assertTrue(driver.findElement(DASHBOARD_HEADER_FIELD).isDisplayed(), "Dashbooard field not availbable");
				
	}

	@Test
	public void addCustomer() {
		loginTest();
		driver.findElement(CUSTOMER_FIELD).click();
		driver.findElement(ADD_CUSTOMER_FIELD).click();
		Assert.assertTrue(driver.findElement(NEW_CUSTOMER_HEADER_FIELD).isDisplayed(), "New Customer window not available");
		//call randomNum method by providing 3 digit int in argument
		driver.findElement(FULL_NAME_FIELD).sendKeys(fullName);
		
		//cal selectDropdown method and pass the webelement and visibleText in the arguments
		selectDropdown(driver.findElement(COMPANY_FIELD), companyName);
		
		driver.findElement(EMAIL_FIELD).sendKeys(randomNum(9999)+ email);
		driver.findElement(PHONE_FIELD).sendKeys(phone + randomNum(999));
		driver.findElement(ADDRESS_FIELD).sendKeys(address);
		driver.findElement(CITY_FIELD).sendKeys(city);
		
		zipCodeMinValidation(zipCodeMin,expZipLessCharText);
		zipCodeMaxValidation(zipCodeMax,expZipMoreCharText);
		driver.findElement(FULL_NAME_FIELD).click();
		driver.findElement(ZIP_FIELD).clear();
		driver.findElement(ZIP_FIELD).sendKeys(correctZipCode);
		
		selectDropdown(driver.findElement(COUNTRY_FIELD), country);
		selectDropdown(driver.findElement(GROUP_FIELD), group);
		driver.findElement(SAVE_FIELD).click();
		driver.findElement(LIST_CUSTOMER_FIELD).click();
		driver.findElement(SEARCH_FIELD).sendKeys(fullName);
		Actions actions = new Actions(driver);
		actions.sendKeys(Keys.ENTER).build().perform();
		
	}
	
	
	public void zipCodeMinValidation(String zipcodeMin, String expZipLessCharText  ) {
		driver.findElement(ZIP_FIELD).sendKeys(zipcodeMin);
		driver.findElement(SAVE_FIELD).click();
		Assert.assertEquals(driver.findElement(ZIP_LESS_CHAR_ERROR_FIELD).getText() ,expZipLessCharText, "Msg don't match");
	
	}
	
	public void zipCodeMaxValidation(String zipcodeMax, String expZipMoreCharText) {
		driver.findElement(FULL_NAME_FIELD).click();
		driver.findElement(ZIP_FIELD).clear();
		driver.findElement(ZIP_FIELD).sendKeys(zipcodeMax);
		driver.findElement(SAVE_FIELD).click();
		Assert.assertEquals(driver.findElement(ZIP_MORE_CHAR_ERROR_FIELD).getText(), expZipMoreCharText, "Msg don't match");
	
	}
	
	public int randomNum(int boundNumber) {
		Random random = new Random();
		int genRandomNum= random.nextInt(boundNumber);
		return genRandomNum;
	}
	
	public void selectDropdown(WebElement webelement, String visibleText) {
		Select sel = new Select(webelement);
		sel.selectByVisibleText(visibleText);
	}
	//@AfterMethod
	public void tearDown() {
		driver.close();
		driver.quit();
	}
}
