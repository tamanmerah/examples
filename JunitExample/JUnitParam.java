
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;


import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.perfectomobile.httpclient.MediaType;
import com.perfectomobile.httpclient.utils.FileUtils;
import com.perfectomobile.selenium.MobileDriver;
import com.perfectomobile.selenium.api.IMobileDevice;
import com.perfectomobile.selenium.api.IMobileDriver;

/**
 * JUnit Parameterized Test
 *
 */
@RunWith(value = Parameterized.class)
public class JUnitParam {

        private String _dev_id;
        static MobileDriver driver;

        public JUnitParam(String DeviceID) {
                this._dev_id = DeviceID;

                String host = Constants.PM_CLOUD;
                String user = Constants.PM_USER;
                String password = Constants.PM_PASSWORD;
                driver = new MobileDriver(host, user, password);

        }

        @Parameters
        public static Collection<Object[]> data() {
                return Arrays.asList(new Object[][] {{"4DF1F4C107319F79"},{"4A8203C8DBAB382EE6BB8021B825A736CA734484"}});

        }



        @Test 
        public void test() {
                //  
                

        }
        // The test work on the specific device ID
                // This testcase go to united.com , look for specific flight and validate we go   
                @Test
                public void CheckFlight() {
                         System.out.println("Parameterized Number is : " + _dev_id);

                        IMobileDevice device = ((IMobileDriver) driver).getDevice(_dev_id);

                        // Getting the device form Perfecto mobile system, open it and take it to the ready mode (HOME)
                        device.open();
                        device.home();
                        
                        // Define to types of web driver 
                                        // 1. DOM - standard web webdriver works with the DOM objects
                                        // 2. Visual Driver - allows to validate that text appear on the screen using visual analysis (OCR).
                                        //    This validation is very important and simulate the real user experience.
                                        
                        WebDriver webDriver = device.getDOMDriver ("www.united.com");
                        WebDriver visualDriver = device.getVisualDriver();

                         webDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
                        webDriver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
                         
                        // press on the button "flight status"
                        WebElement webElement = webDriver.findElement(By.xpath("(//#text)[53]"));
                        webElement.click();

                        // press on the button "flight number"
                        webElement = webDriver.findElement(By.xpath("(//@id=\"FlightNumber\")[1]"));
                        webElement.sendKeys("84");
                        
                        // press on the button "Search"
                        webDriver.findElement(By.xpath("(//INPUT)[5]")).click();
                        
                        // visual based validation - validate the text appear on the screen and can be seen by users.
                        visualDriver.manage().timeouts().implicitlyWait(25,TimeUnit.SECONDS);
                        visualDriver.findElement(By.linkText("New York/Newark"));

                        // object based validation - validate the text appear in the as part of the DOM structure 
                        String text =  webDriver.findElement(By.xpath("(//#text)[177]")).getAttribute("text");
                           assertEquals("New York/Newark, NJ (EWR)",text);
          
                  }

        @AfterClass
        public static void testCleanup() {
                driver.quit();
         
        }

}
