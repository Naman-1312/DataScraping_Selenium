package Base;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Particular_DoctorData {

    private static final Logger logger = Logger.getLogger(Url_opening.class);

    public static void main(String[] args) {
        // To Configure log4j for the Logging purpose
        PropertyConfigurator.configure("src/main/resources/log4j.properties");

        WebDriverManager.edgedriver().setup();
        WebDriver driver = new EdgeDriver();
        driver.manage().window().maximize();
        
        List<String> urls = readUrlsFromExcel("DoctorURLs.xlsx");

        for (String url : urls) {
            driver.get(url);
            try {
                Thread.sleep(2000); // Sleep for 2 seconds to wait for the page to load
            } catch (InterruptedException e) {
                logger.error("Thread interrupted while waiting for page to load", e);
            }
            
            try {
                String doctorName = driver.findElement(By.xpath(".//h1")).getText();
                
              System.out.println("Doctor Name: " + doctorName);
/*            String doctorProfileImage = driver.findElement(By.cssSelector(".doctoravtar")).getAttribute("background-image");
              doctorProfileImage = doctorProfileImage.replace("url(", "").replace(")", "").replace("\"", "");
              System.out.println("Doctor Profile Image: " + doctorProfileImage);
              String doctorClinicDetails = driver.findElement(By.cssSelector("selector_for_doctor_clinic_details")).getText();
              String doctorExperience = driver.findElement(By.cssSelector("selector_for_doctor_experience")).getText();
                */
              
              // To Fetch the CSS Property Value for the Doctor Profile Image
              String doctorProfileImage = driver.findElement(By.cssSelector(".doctoravtar")).getCssValue("background-image");

              // To Extract the URL from the CSS property value
              if (doctorProfileImage.startsWith("url(\"") && doctorProfileImage.endsWith("\")")) {
                  doctorProfileImage = doctorProfileImage.substring(5, doctorProfileImage.length() - 2);
              } else if (doctorProfileImage.startsWith("url(") && doctorProfileImage.endsWith(")")) {
                  doctorProfileImage = doctorProfileImage.substring(4, doctorProfileImage.length() - 1);
              }

              System.out.println("Doctor Profile Image: " + doctorProfileImage);

              
              
                logger.info("Doctor Name: " + doctorName);
//                logger.info("Doctor Profile Image: " + doctorProfileImage);
//                logger.info("Doctor Clinic Details: " + doctorClinicDetails);
//                logger.info("Doctor Experience: " + doctorExperience);

                
     // Save the extracted data as needed, for example, write it to a new Excel file
                
            } catch (Exception e) {
                logger.error("Error scraping data from URL: " + url, e);
            }
        }
        
        driver.quit();
    }

    private static List<String> readUrlsFromExcel(String filePath) {
        List<String> urls = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip the header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell cell = row.getCell(0);
                if (cell != null) {
                    urls.add(cell.getStringCellValue());
                }
            }
        } catch (IOException e) {
            logger.error("Error reading URLs from Excel file", e);
        }
        return urls;
    }
}
