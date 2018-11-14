package com.crm.qa.testcases;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.crm.qa.base.TestBase;
import com.crm.qa.pages.ContactsPage;
import com.crm.qa.pages.HomePage;
import com.crm.qa.pages.LoginPage;
import com.crm.qa.util.TestUtil;

public class ContactsPageTest extends TestBase {
	LoginPage loginPage;
	HomePage homePage;
	ContactsPage contactsPage;
	TestUtil testUtil;
	String sheetName = "contacts";


	
	public ContactsPageTest() {       //ContactsPageTest constructor
		//1. it must call TestBase constructor first via super()
		//2. this gets all properties from TestBase constructor
		//3. only then it can call TestBase.initialization() method
		super();

	}
	
	@BeforeMethod
	public void setUp() throws InterruptedException {
		initialization();
		testUtil = new TestUtil();
		contactsPage = new ContactsPage();
		loginPage = new LoginPage();  //to access LoginPage.login method
		homePage = loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
		testUtil.switchToFrame();
		contactsPage = homePage.clickOnContactLink();
	}
	

	@Test   //(priority=n) remove from @Test. Otherwise, in Suite, only 1st test from each class is run
	public void contactsLabelTest() {
//		testUtil.switchToFrame();  //no need now because it is done in "setUp();
		Assert.assertTrue(contactsPage.verifyContactsLabel(),"contacts label is missing on the page");
	}
	
	@Test
	public void selectSingleContactsTest() throws InterruptedException {
		contactsPage.selectContactsByName("Joe Din");
		Thread.sleep(3000); //just to see checked box by "name"
		
	}

	@Test
	public void selectMultipleContactsTest() throws InterruptedException {
		contactsPage.selectContactsByName("Joe Din");
		contactsPage.selectContactsByName("Wanda Knowles");
		Thread.sleep(3000); //just to see checked box by "name"
	}
	
	@DataProvider
	public Object[][] getCRMTestData() {
		Object data[][] = TestUtil.getTestData(sheetName);
		return data;
	}
	
	@Test(priority=1, dataProvider="getCRMTestData")
	public void validateCreateNewContactTest(String tit, String fName, String lName, String comp) throws InterruptedException {
		homePage.clickOnNewContactLink();
		contactsPage.createNewContact(tit, fName, lName, comp);
		Thread.sleep(2000);
		contactsPage.clickContactsTab();
		Thread.sleep(2000);
		
		String actName = driver.findElement(By.xpath("//a[contains(text(),'"+fName+" "+lName+"')]")).getText();
		
		Assert.assertEquals(actName, fName+" "+lName);

	} 
	
	
	
	@AfterMethod
	public void tearDown(){
		driver.quit();
	}

}
