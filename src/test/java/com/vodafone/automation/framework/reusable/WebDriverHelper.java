package com.vodafone.automation.framework.reusable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import atu.testng.reports.ATUReports;
import atu.testng.reports.logging.LogAs;
import atu.testng.selenium.reports.CaptureScreen;
import atu.testng.selenium.reports.CaptureScreen.ScreenshotOf;

public class WebDriverHelper {
static WebDriver driver;
static Properties p;
static WebDriverWait wait;
public static void initialize(String browser) throws FileNotFoundException, IOException
{
	p = new Properties();
	p.load(new FileInputStream("Project.properties"));
	DesiredCapabilities cap=null;
	switch(browser)
	{
	case "gc":
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		driver = new ChromeDriver();
		/*cap = DesiredCapabilities.chrome();
		driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),cap);*/
		break;
	case "ff":
		System.setProperty("webdriver.gecko.driver", "drivers/geckodriver.exe");
		driver = new FirefoxDriver();
		/*cap = DesiredCapabilities.firefox();
		driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),cap);*/
		break;
	case "ie":
		System.setProperty("webdriver.ie.driver", "drivers/IEDriverServer.exe");
		driver = new InternetExplorerDriver();
		/*cap = DesiredCapabilities.internetExplorer();
		driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),cap);*/
		break;
		default:
			System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
			driver = new ChromeDriver();
			break;
	}
	ATUReports.setWebDriver(driver);
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	wait = new WebDriverWait(driver,120);
	ATUReports.add("Browser initialized", LogAs.INFO, new CaptureScreen(
            ScreenshotOf.DESKTOP));
	navigate(p.getProperty("url"));
}
public static void navigate(String url) {
	driver.get(url);
	ATUReports.add("Webpage initialized",url, LogAs.INFO, new CaptureScreen(
            ScreenshotOf.BROWSER_PAGE));
}
public static void click(By loc)
{
	int i=0;
	try
	{
		wait.until(ExpectedConditions.presenceOfElementLocated(loc));
		wait.until(ExpectedConditions.visibilityOfElementLocated(loc));
		wait.until(ExpectedConditions.elementToBeClickable(loc));
		driver.findElement(loc).click();
		ATUReports.add("Clicked on "+loc.toString(), LogAs.PASSED, new CaptureScreen(
	            ScreenshotOf.BROWSER_PAGE));
	}
	catch(NoSuchElementException e)
	{
		i++;
		while(i<3)
		{
		driver.navigate().refresh();
		click(loc);
		}
		ATUReports.add("Clicked on "+loc.toString(), LogAs.FAILED, new CaptureScreen(
	            ScreenshotOf.BROWSER_PAGE));
	}
	catch(WebDriverException e)
	{
		if(e.getMessage().contains("Some other element"))
		{
			Actions act = new Actions(driver);
			act.click(driver.findElement(loc)).build().perform();
			ATUReports.add("Mouse Clicked on "+loc.toString(), LogAs.PASSED, new CaptureScreen(
		            ScreenshotOf.BROWSER_PAGE));
		}
	}
}
public static void type(By loc, String data)
{
	wait.until(ExpectedConditions.presenceOfElementLocated(loc));
	wait.until(ExpectedConditions.visibilityOfElementLocated(loc));
	driver.findElement(loc).sendKeys(data);
	ATUReports.add("Input text on "+loc.toString(),data, LogAs.INFO, new CaptureScreen(
            ScreenshotOf.BROWSER_PAGE));
}
public static void quit()
{
	driver.quit();
	ATUReports.add("Close browser", false);
}
}
