package Base;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import PageObject.DoctorPage;
import Utilities.ExcelUtils;

import java.io.IOException;

public class MainClass {
	
	public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        
        // Url opening
        String url = "https://kivihealth.com/jaipur/doctors";
        driver.get(url);

        DoctorPage doctorPage = new DoctorPage(driver);
        ExcelUtils excelUtils = new ExcelUtils();

        // Loop through each doctor's profile page and extract data
        // This is a placeholder logic, adjust the iteration as needed
        for (int i = 0; i < 10; i++) {
            // Navigate to the doctor's profile page
            // (Implement the navigation logic according to your site structure)

            // Extract data
            String profilePhotoUrl = doctorPage.getProfilePhotoUrl();
            String name = doctorPage.getDoctorName();
            String education = doctorPage.getDoctorEducation();
            String fees = doctorPage.getDoctorFees();

            // Store data in Excel
            excelUtils.addDoctorData(profilePhotoUrl, name, education, fees, i + 1);
        }

        // To Save the Excel file
        try {
            excelUtils.saveToFile("doctors_data.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }

        driver.quit();
    }

}
