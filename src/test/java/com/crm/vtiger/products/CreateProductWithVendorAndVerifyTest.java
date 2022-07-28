package com.crm.vtiger.products;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import com.lexnod.genericLib.ExcelFileLibrary;
import com.lexnod.genericLib.JavaUtility;
import com.lexnod.genericLib.PropertyFileLibrary;
import com.lexnod.genericLib.WebDriverCommonLibrary;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CreateProductWithVendorAndVerifyTest {

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
		driver.findElement(By.name("user_name")).sendKeys(plib.getPropertyData("username"));
		driver.findElement(By.name("user_password")).sendKeys(plib.getPropertyData("password"));
		driver.findElement(By.id("submitButton")).submit();

		// VERIFICATION V-TIGER HOME PAGE IS DISPLAYED OR NOT
		wlib.waitForPageTitle("Administrator", driver, 10);
		System.out.println("VTiger Home page is displayed, PASS");
		
		//mouse hover to more
		WebElement moreElement = driver.findElement(By.xpath("//a[text()='More']"));
		wlib.mouseHoverOnElement(moreElement, driver);
		
		//click on vendors
		driver.findElement(By.xpath("//a[text()='Vendors']")).click();
		
		//verify vendors page is displayed or not
		String vendorTitle = "Administrator - Vendors - vtiger CRM 5 - Commercial Open Source CRM";
		if(driver.getTitle().equals(vendorTitle))
		{
			System.out.println("Vendor page is displayed, PASS");
		}else
		{
			System.out.println("Vendor page is not displayed, FAIL");
		}
		
		//clicking on create vendor img
		driver.findElement(By.xpath("//img[@title='Create Vendor...']")).click();
		
		//enter value in vendor name
		String vendorName = elib.getExcelData("vendors", 1, 0);
		driver.findElement(By.name("vendorname")).sendKeys(elib.getExcelData("vendors", 1, 0));
		
		//click on save button
		driver.findElement(By.xpath("(//input[@title='Save [Alt+S]'])[1]")).click();
		
		//verifying vendor is created or not
		String vendor=driver.findElement(By.xpath("//span[@class='lvtHeaderText']")).getText();
		if(vendor.contains(vendorName))
		{
			System.out.println("Vendor is created, PASS");
		}else
		{
			System.out.println("Vendor is not created, FAIL");
		}
		
		//clicking on products module
		driver.findElement(By.xpath("//a[@href='index.php?module=Products&action=index']")).click();
		
		//verifying products page is displayed or not
		String productsTitle = "Administrator - Products - vtiger CRM 5 - Commercial Open Source CRM";
		if(driver.getTitle().equals(productsTitle))
		{
			System.out.println("Products page is displayed, PASS");
		}else
		{
			System.out.println("Products page is not displayed, FAIL");
		}
		
		//clicking on create product img
		driver.findElement(By.xpath("//img[@title='Create Product...']")).click();
		
		//entering details in product name tab
		String productName = elib.getExcelData("Product", 1, 0);
		driver.findElement(By.name("productname")).sendKeys(productName);
		
		//selecting vendorname
		driver.findElement(By.xpath("//img[@title='Select']")).click();
		String parentId = driver.getWindowHandle();
		wlib.switchToWindow("Vendors&action", driver);
		
		//entering the vendor name
		driver.findElement(By.id("search_txt")).sendKeys(vendorName);
		
		//click on search button
		driver.findElement(By.name("search")).click();
		
		//select vendor name
		driver.findElement(By.id("1")).click();
		
		//switch back to main window
		driver.switchTo().window(parentId);
		
		//click on save button
		driver.findElement(By.xpath("(//input[@title='Save [Alt+S]'])[1]")).click();
		
		//verify whether product is created or not
		String product = driver.findElement(By.xpath("//span[@class='lvtHeaderText']")).getText();
		if(product.contains(productName))
		{
			System.out.println("Product is created, PASS");
		}else
		{
			System.out.println("Product is not created, FAIL");
		}
		
		// mouse hover to administration link
		WebElement adminElement = driver.findElement(By.xpath("//img[@src='themes/softed/images/user.PNG']"));
		wlib.mouseHoverOnElement(adminElement, driver);

		// click on signout link
		driver.findElement(By.xpath("//a[text()='Sign Out']")).click();

		// close the browser
		driver.close();
	}

}
