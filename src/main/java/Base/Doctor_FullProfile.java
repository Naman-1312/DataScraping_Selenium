package Base;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Doctor_FullProfile {
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
	        		String doctorEducation = element.findElement(By.xpath(".//h5[1]")).getAttribute("innerText");
	        		String doctorSpecialization = element.findElement(By.xpath(".//h5[2]")).getAttribute("innerText");
	        		String doctorExperience = element.findElement(By.xpath(".//h5[3]")).getAttribute("innerText");
//	        		String doctorClinic = element.findElement(By.className("//*[@class='overflow_set text-left' ]")).getAttribute("innerText");
//	        		String xkj = element.findElement(By.className(doctorClinic))
	        		
	        		// Printing the extracted values 
	        		System.out.println("Image Url: " + imageUrl); 
	        		System.out.println("Doctor Name: " + doctorName); 
	        		System.out.println("Doctor Education: " + doctorEducation); 
	        		System.out.println("Doctor Specialization: " + doctorSpecialization); 
	        		System.out.println("Doctor Specialization: " + doctorExperience);
//	        		System.out.println("Doctor Clinic: " + doctorClinic);
	        	}
	        	driver.findElement(By.xpath("//i[contains(text(),'chevron_right')]")).click(); 

	        }
//	driver.quit();
	}
}
