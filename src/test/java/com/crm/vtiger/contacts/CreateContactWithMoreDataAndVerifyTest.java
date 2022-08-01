 package com.crm.vtiger.contacts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import com.lexnod.ObjectRepository.ContactsPage;
import com.lexnod.ObjectRepository.CreateNewContactsPage;
import com.lexnod.ObjectRepository.HomePage;
import com.lexnod.ObjectRepository.LoginPage;
import com.lexnod.ObjectRepository.OrganizationNamePage;
import com.lexnod.genericLib.ExcelFileLibrary;
import com.lexnod.genericLib.JavaUtility;
import com.lexnod.genericLib.PropertyFileLibrary;
import com.lexnod.genericLib.WebDriverCommonLibrary;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * 
 * @author ARUN
 *
 */
public class CreateContactWithMoreDataAndVerifyTest {

	public static void main(String[] args) throws Throwable {

		ExcelFileLibrary elib = new ExcelFileLibrary();
		PropertyFileLibrary plib = new PropertyFileLibrary();
		JavaUtility jlib = new JavaUtility();
		WebDriverCommonLibrary wlib = new WebDriverCommonLibrary();

		WebDriver driver = null;

		String browser = plib.getPropertyData("browser");

		if (browser.equalsIgnoreCase("firefox")) {
			// setting up browser
			WebDriverManager.firefoxdriver().setup();

			// creating object for browser
			driver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("chrome")) {
			// setting up browser
			WebDriverManager.chromedriver().setup();

			// creating object for browser
			driver = new ChromeDriver();
		}

		// maximizing the browser
		wlib.maximizeTheWindow(driver);

		// passing the url
		driver.get(plib.getPropertyData("url"));

		// passing wait condition
		wlib.waitTillPageGetsLoadImplicitlyWait(driver, 10);

		// VERIFYING V-TIGER LOGIN PAGE IS DISPLAYED OR NOT
		String loginTitle = "vtiger CRM 5 - Commercial Open Source CRM";
		if (driver.getTitle().equals(loginTitle)) {
			System.out.println("VTiger Login Page is Displayed, PASS");
		} else {
			System.out.println("VTiger Login page is not displayed, FAIL");
		}

		// giving login details and clicking on login
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(plib.getPropertyData("username"), plib.getPropertyData("password"));

		// VERIFICATION V-TIGER HOME PAGE IS DISPLAYED OR NOT
		wlib.waitForPageTitle("Administrator", driver, 10);
		System.out.println("VTiger Home page is displayed, PASS");
		
		// clicking on contacts module
		HomePage home = new HomePage(driver);
		home.clickContactsModule();

		// verify contacts page is displayed or not
		String contactTitle = "Administrator - Contacts - vtiger CRM 5 - Commercial Open Source CRM";
		if (driver.getTitle().equals(contactTitle)) {
			System.out.println("Contacts page is displayed, PASS");
		} else {
			System.out.println("Contacts page is not displayed, FAIL");
		}

		// clicking on create contact image
		ContactsPage contactsPage = new ContactsPage(driver);
		contactsPage.clickCreateContactsImage();

		CreateNewContactsPage createNewContacts = new CreateNewContactsPage(driver);
		// selecting salutation
		createNewContacts.selectSalutationDropdownValue(elib.getExcelData("AllDropDown", 1, 0));

		// entering firstname
		String firstName = elib.getExcelData("contacts", 2, 0)+jlib.getRandonNumber(100);
		createNewContacts.getFirstnameField().sendKeys(firstName);

		// entering lastname
		createNewContacts.getLastnameField().sendKeys(elib.getExcelData("contacts", 2, 1));

		// clicking on orgnization name
		createNewContacts.getOrganizationNameImage().click();
		String parentId = driver.getWindowHandle();
		wlib.switchToWindow("Accounts&action", driver);

		// searching the organization name and clicking it
		OrganizationNamePage organizationName = new OrganizationNamePage(driver);
		organizationName.getOrganizationName(elib.getExcelData("Organization", 2, 1));
		
		// switching to main window
		driver.switchTo().window(parentId);

		// clicking on save
		createNewContacts.getSaveButton().click();

		// verify whether contact is created or not
		String contact = driver.findElement(By.xpath("//span[@class='dvHeaderText']")).getText();
		if (contact.contains(firstName)) {
			System.out.println("Contact is created, PASS");
		} else {
			System.out.println("Contact is not created, FAIL");
		}

		//signout
		home.clickSignoutLink(driver);

		// close the browser
		driver.close();
	}

}
