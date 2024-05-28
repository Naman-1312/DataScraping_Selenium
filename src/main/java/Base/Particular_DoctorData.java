package Base;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
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
            	System.out.println("**********************************************************");
            	System.out.println("Doctor Url : " + url);
            	
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

              // To scrap the Doctor Specialization 
              
              String doctorSpecialization = driver.findElement(By.xpath("//*[@id='doctorprofilecard']/div[1]/p")).getText();
              System.out.println("Doctor Specilization & Experience: " + doctorSpecialization);
              
              // To Scrap the Doctor Rating
              
              String doctorRating = driver.findElement(By.xpath("//*[@id = 'doctorprofilecard']/div[2]/span[1]")).getText();
              System.out.println("Doctor Ratings: " + doctorRating + " Star");
              
              // To scrap the Doctor Registration Number
              String doctorRegistration = driver.findElement(By.xpath("//*[@id = 'doctorprofilecard']/div[3]/div[1]/div/p[1]")).getText();
              System.out.println("Doctor Registration No. : " + doctorRegistration );
              
              
              // To Scrap the Doctor Education Data
              String doctorEducation = driver.findElement(By.xpath("//*[@class = 'educationbox']/p[2]")).getText();
              System.out.println("Doctor Education : " + doctorEducation );
              
              
              // To scrap Additional photos of the doctor   
              WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
              List<WebElement> imageThumbnails = driver.findElements(By.cssSelector("#doctorphotos .image-thumbnail"));

              // Iterate over the image thumbnails and extract the background-image URLs
              for (int i = 1; i <= 5; i++) {
                  String imageUrl = imageThumbnails.get(i).getAttribute("style");
                  imageUrl = extractImageUrl(imageUrl);
                  System.out.println("Doctor Additional Photos " + (i) + " URL: " + imageUrl);
              }
              
              // To scrap doctor clinic details and contact number
              
              List<WebElement> clinicElements = driver.findElements(By.cssSelector("#doctorclinics .clinics-title"));

              for (int i = 1; i <= 4; i++) {
//                  WebElement clinicNameElement = clinicElement.findElement(By.className("clinics-title"));
//                  String clinicName = clinicNameElement.getText();
            	  String clinicDetails = clinicElements.get(i).getText();
                  
//            	  List<WebElement> contactElements = clinicElements.findElements(By.className("text-link"));
//                  String contactNumber = "";
//                  for (WebElement contactElement : contactElements) {
//                      if (contactElement.getAttribute("href").startsWith("tel:")) {
//                          contactNumber = contactElement.getText().replace(" ", "");
//                          break;
//                      }
//                  }

                  System.out.println("Clinic Name: " + clinicDetails);
//                  System.out.println("Contact Number: " + contactNumber);
                  System.out.println();
              }


              
/*            String additionalPhoto1 = driver.findElement(By.cssSelector("//*[@id='doctorphotos']/div[1]/div[1]/div[1]")).getAttribute("url");
              System.out.println("Doctor Additional Photo 1 : " + additionalPhoto1 );
            
              String additionalPhoto2 = driver.findElement(By.xpath("//*[@id='doctorphotos']/div[1]/div[1]/div[2]")).getAttribute("url");
              System.out.println("Doctor Additional Photo 2 : " + additionalPhoto2 );
              
              String additionalPhoto3 = driver.findElement(By.xpath("//*[@id='doctorphotos']/div[1]/div[1]/div[3]")).getAttribute("url");
              System.out.println("Doctor Additional Photo 3 : " + additionalPhoto3 );
              
              String additionalPhoto4 = driver.findElement(By.xpath("//*[@id='doctorphotos']/div[1]/div[1]/div[4]")).getAttribute("url");
              System.out.println("Doctor Additional Photo 4 : " + additionalPhoto4 );

              String additionalPhoto5 = driver.findElement(By.xpath("//*[@id='doctorphotos']/div[1]/div[1]/div[5]")).getAttribute("url");
              System.out.println("Doctor Additional Photo 5 : " + additionalPhoto5 );
*/               
              	logger.info("Doctor Name: " + doctorName);
                logger.info("Doctor Profile Image: " + doctorProfileImage);
                logger.info("Doctor Specilization: " + doctorSpecialization);
                logger.info("Doctor Ratings: " + doctorRating );
                logger.info("Doctor Registration: " + doctorName);
                logger.info("Doctor Education: " + doctorEducation);
              
/*              logger.info("Doctor Additional Photo 1: " + additionalPhoto1);
                logger.info("Doctor Additional Photo 2: " + additionalPhoto2 );
                logger.info("Doctor Additional Photo 3: " + additionalPhoto3);
                logger.info("Doctor Additional Photo 4: " + additionalPhoto4 );
*/
                
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
    private static String extractImageUrl(String styleAttribute) {
        String imageUrl = "";
        if (styleAttribute != null && styleAttribute.contains("background-image: url(")) {
            int startIndex = styleAttribute.indexOf("background-image: url(") + "background-image: url(".length();
            int endIndex = styleAttribute.indexOf(")", startIndex);
            if (endIndex > startIndex) {
                imageUrl = styleAttribute.substring(startIndex, endIndex).replace("\"", "");
            }
        }
        return imageUrl;
    }
}
