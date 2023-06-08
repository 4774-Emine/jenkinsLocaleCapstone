package utilities;


import io.github.bonigarcia.wdm.WebDriverManager;


import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static utilities.ConfigReader.*;

public class Driver {

    private static final ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();

    private Driver(){
    }


    private static WebDriver driver;



    public static WebDriver getDriver(){
        if (driverPool.get() == null) {

            // this line will tell which browser should open based on the value from properties file
            String browserParamFromEnv = System.getProperty("browser");
            String browser = browserParamFromEnv == null ? getProperty("browser") : browserParamFromEnv;

            switch (browser) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    driverPool.set(new ChromeDriver());
                    break;
                case "chrome_headless":
                    WebDriverManager.chromedriver().setup();
                    driverPool.set(new ChromeDriver(new ChromeOptions().setHeadless(true)));
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driverPool.set(new FirefoxDriver());
                    break;

                case "firefox_headless":
                    WebDriverManager.firefoxdriver().setup();
                    driverPool.set(new FirefoxDriver(new FirefoxOptions().setHeadless(true)));
                    break;
                case "ie":
                    if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
                        throw new WebDriverException("Your OS doesn't support Internet Explorer");
                    }
                    WebDriverManager.iedriver().setup();
                    driverPool.set(new InternetExplorerDriver());
                    break;
                case "edge":
                    if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
                        throw new WebDriverException("Your OS doesn't support Edge");
                    }
                    WebDriverManager.edgedriver().setup();
                    driverPool.set(new EdgeDriver());
                    break;
                case "safari":
                    if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
                        throw new WebDriverException("Your OS doesn't support Safari");
                    }
                    WebDriverManager.getInstance(SafariDriver.class).setup();
                    driverPool.set(new SafariDriver());
                    break;

//                case "remote_chrome":
//                    try {
//                        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
//                        desiredCapabilities.setBrowserName(BrowserType.CHROME);
//                        desiredCapabilities.setCapability("platform", Platform.ANDROID);
//                        driverPool.set(new RemoteWebDriver(new URL("http://localhost:4723/wd/hub"), desiredCapabilities));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                case "remote_firefox":
//                    try {
//                        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
//                        desiredCapabilities.setBrowserName(BrowserType.FIREFOX);
//                        desiredCapabilities.setCapability("platform", Platform.ANY);
//                        driverPool.set(new RemoteWebDriver(new URL("http://ec2-3-88-210-43.compute-1.amazonaws.com:4444/wd/hub"), desiredCapabilities));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
                default:
                    WebDriverManager.chromedriver().setup();
                    driverPool.set(new ChromeDriver());

            }
            driverPool.get().manage().window().maximize();
            driverPool.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(15));

        }
        return driverPool.get();
    }
    public static void closeDriver(){

        if (driver!=null){
            driverPool.get().quit();
            driverPool.remove();
        }

        driver=null;
    }
}