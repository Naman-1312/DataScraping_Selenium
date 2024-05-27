package Base;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Scrapping_SecondMethod {

	
	public static void main(String[] args) {
		WebDriver driver = null;
		 WebDriverManager.edgedriver().setup();
	        driver = new EdgeDriver();
	        driver.manage().window().maximize();
	        // Url opening
	        driver.get("https://kivihealth.com/jaipur/doctors");
//	        driver.get("https://kivihealth.com/jaipur/doctors");
	        while(driver.findElement(By.xpath("//i[contains(text(),'chevron_right')]")).isDisplayed())
	        {
	        	List<WebElement> list = driver .findElements(By.xpath("//div[@class='searchContainer']//div[contains(@class,'docBox')]")); 
	        	for (int i = 0; i < list.size(); i++)	
	        	{
	        		
	        		System.out.println("*******************************" + i + "***********************");
	        		WebElement element = list.get(i); 
	        		String imageUrl = element.findElement(By.xpath(".//img")).getAttribute("src"); 
	        		String doctorName = element.findElement(By.xpath(".//h4")).getAttribute("innerText"); 
	        		
	        		// Printing the extracted values 
	        		System.out.println("Image Url: " + imageUrl); 
	        		System.out.println("Doctor Name: " + doctorName); 
	        		// System.out.println("Specialization: " + specialization); 
	        		// System.out.println("Currency Value: " + currencyValue); 

	        	}
	        	driver.findElement(By.xpath("//i[contains(text(),'chevron_right')]")).click(); 

	        }
//	driver.quit();
	}

}
