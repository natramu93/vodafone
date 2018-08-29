package com.vodafone.automation.framework.test;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.vodafone.automation.framework.po.HomePage;
import com.vodafone.automation.framework.po.LoginPage;
import com.vodafone.automation.framework.po.RegisterPage;
import com.vodafone.automation.framework.reusable.WebDriverHelper;

import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;

@Listeners({ ATUReportsListener.class, ConfigurationListener.class,
	  MethodListener.class })
public class TestSuite1 {

	{
        System.setProperty("atu.reporter.config", "atu.properties");
    }
	@Parameters({"browser"})
	@BeforeClass
	public void bs(String browser) throws FileNotFoundException, IOException {
		WebDriverHelper.initialize(browser);
	}
	
	@Test
	public void register()
	{
		WebDriverHelper.click(HomePage.my_acc);
		WebDriverHelper.click(LoginPage.reg);
		WebDriverHelper.type(RegisterPage.fname, "Natarajan");
		WebDriverHelper.type(RegisterPage.lname, "Ramanathan");
	}
	
	@AfterSuite
	public void as() {
		WebDriverHelper.quit();
	}
}
