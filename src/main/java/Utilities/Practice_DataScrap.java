package Utilities;



import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Practice_DataScrap {

    private static final Logger logger = Logger.getLogger(Practice_DataScrap.class);

    public static void main(String[] args) {
        // Configure log4j for logging
        PropertyConfigurator.configure("src/main/resources/log4j.properties");

        WebDriverManager.edgedriver().setup();
        WebDriver driver = new EdgeDriver();
        driver.manage().window().maximize();

        List<String> urls = readUrlsFromExcel("DoctorUrl.xlsx");
        List<DoctorData> doctorDataList = new ArrayList<>();

        for (String url : urls) {
            driver.get(url);
            try {
                System.out.println("**********************************************************");
                System.out.println("Doctor Url : " + url);
                System.out.println();
                Thread.sleep(2000); // Sleep for 2 seconds to wait for the page to load
            } catch (InterruptedException e) {
                logger.error("Thread interrupted while waiting for page to load", e);
            }

            try {
                String doctorName = driver.findElement(By.xpath(".//h1")).getText();
                System.out.println("Doctor Name: " + doctorName);

                String doctorProfileImage = driver.findElement(By.cssSelector(".doctoravtar")).getCssValue("background-image");
                if (doctorProfileImage.startsWith("url(\"") && doctorProfileImage.endsWith("\")")) {
                    doctorProfileImage = doctorProfileImage.substring(5, doctorProfileImage.length() - 2);
                } else if (doctorProfileImage.startsWith("url(") && doctorProfileImage.endsWith(")")) {
                    doctorProfileImage = doctorProfileImage.substring(4, doctorProfileImage.length() - 1);
                }
//                System.out.println("Doctor Profile Image: " + doctorProfileImage);

//                String doctorSpecialization = driver.findElement(By.xpath("//*[@id='doctorprofilecard']/div[1]/p")).getText();
//                System.out.println("Doctor Specialization & Experience: " + doctorSpecialization);

                String doctorSpecialization = "";
                try {
                    doctorSpecialization = driver.findElement(By.xpath("//*[@id='doctorprofilecard']/div[1]/p")).getText();
                } catch (Exception e) {
                    logger.error("Error fetching Doctor Specialization", e);
                }
                doctorSpecialization = doctorSpecialization.isEmpty() ? "NA" : doctorSpecialization;
//                System.out.println("Doctor Specialization & Experience: " + doctorSpecialization);
                
//                String doctorRating = driver.findElement(By.xpath("//*[@id = 'doctorprofilecard']/div[2]/span[1]")).getText();
//                System.out.println("Doctor Ratings: " + doctorRating + " Star");

                String doctorRating = "";
                try {
                    doctorRating = driver.findElement(By.xpath("//*[@id = 'doctorprofilecard']/div[2]/span[1]")).getText();
                } catch (Exception e) {
                    logger.error("Error fetching Doctor Ratings", e);
                }
                doctorRating = doctorRating.isEmpty() ? "NA" : doctorRating + " Star";
//                System.out.println("Doctor Ratings: " + doctorRating);
                
                
                
//                String doctorEducation = driver.findElement(By.xpath("//*[@class = 'educationbox']/p[2]")).getText();
//                System.out.println("Doctor Education : " + doctorEducation);

                String doctorEducation = "";
                try {
                	doctorEducation = driver.findElement(By.xpath("//*[@class = 'educationbox']/p[2]")).getText();
                } catch (Exception e) {
                    logger.error("Error fetching Doctor Education", e);
                }
                doctorEducation = doctorEducation.isEmpty() ? "NA" : doctorEducation;
//                System.out.println("Doctor Education: " + doctorEducation);
                
                
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
                List<WebElement> imageThumbnails = driver.findElements(By.cssSelector("#doctorphotos .image-thumbnail"));
                String[] additionalPhotosUrls = new String[5];
                for (int i = 0; i < 5; i++) {
                    if (i < imageThumbnails.size()) {
                        additionalPhotosUrls[i] = extractImageUrl(imageThumbnails.get(i).getAttribute("style"));
//                        System.out.println("Doctor Additional Photos " + (i + 1) + " URL: " + additionalPhotosUrls[i]);
                    } else {
                        additionalPhotosUrls[i] = "NA";
//                        System.out.println("Doctor Additional Photos " + (i + 1) + " URL: NA");
                    }
                }

                DoctorData doctorData = new DoctorData(doctorName, doctorProfileImage, doctorSpecialization, doctorRating, doctorEducation, additionalPhotosUrls, clinicNames);
                doctorDataList.add(doctorData);

                List<WebElement> clinicBox = driver.findElements(By.cssSelector("#doctorclinics .clinics-title"));
                String[] clinicNames = new String[5];	
                for (int i = 0; i < clinicNames.length; i++) {
                   // Get clinic name
            	  if (i < clinicBox.size()) {
            		  clinicNames[i] = extractClinicNames(clinicBox.get(i).getText());
            		  System.out.println("Doctor Clinic Names " + (i + 1) + " Name: " + clinicNames[i]);
            	  } else {
            		  clinicNames[i] = "NA";
            		  System.out.println("Doctor Clinic Name " + (i + 1) + "Clinic Name : NA");
            	  }

                    // Find the phone number element
                    WebElement phoneNumberElement = ((By) clinicBox).findElement((SearchContext) By.tagName("a"));
                    // Extract the phone number
                    String phoneNumber = phoneNumberElement.getText();

                    // Print the clinic name and phone number
                    System.out.println("Clinic Name: " + clinicNames);
                    System.out.println("Phone Number: " + phoneNumber);
                }
                
                // Logging the information
                logger.info("Doctor Name: " + doctorName);
                logger.info("Doctor Profile Image: " + doctorProfileImage);
                logger.info("Doctor Specialization: " + doctorSpecialization);
                logger.info("Doctor Ratings: " + doctorRating);
                logger.info("Doctor Education: " + doctorEducation);
                
            } catch (Exception e) {
                logger.error("Error scraping data from URL: " + url, e);
            }
        }

        driver.quit();
        writeDataToExcel(doctorDataList, "DoctorInfo.xlsx");
    }
    private static String extractClinicNames(String text) {
        try {
            // Split the text to extract only the clinic name
            String[] parts = text.split("\\r?\\n");
            String clinicName = parts[0];
            
            // Return the clinic name
            return clinicName;
        } catch (Exception e) {
           // If any exception occurs, return "NA"
            return "NA";
        }

      
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

    private static void writeDataToExcel(List<DoctorData> doctorDataList, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Doctor Info");

            // Creating header row
            String[] columns = {
                "Doctor Name", "Doctor Profile Image", "Doctor Specialization and Experience", "Doctor Rating",
                "Doctor Education", "Doctor Additional Photo 1", "Doctor Additional Photo 2",
                "Doctor Additional Photo 3", "Doctor Additional Photo 4", "Doctor Additional Photo 5","Doctor Clinic Name 1", "Doctor Clinic Name 2","Doctor Clinic Name 3",
                "Doctor Clinic Name 4","Doctor Clinic Name 5","Clinic Contact No. 1","Clinic Contact No. 2","Clinic Contact No. 3","Clinic Contact No. 4","Clinic Contact No. 5"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Creating data rows
            int rowNum = 1;
            for (DoctorData doctorData : doctorDataList) {
                Row dataRow = sheet.createRow(rowNum++);

                
  // To store data in the Excel sheet!              
        dataRow.createCell(0).setCellValue(doctorData.getDoctorName() != null && !doctorData.getDoctorName().isEmpty() ? doctorData.getDoctorName() : "NA");
        dataRow.createCell(1).setCellValue(doctorData.getProfileImage() != null && !doctorData.getProfileImage().isEmpty() ? doctorData.getProfileImage() : "NA");
        dataRow.createCell(2).setCellValue(doctorData.getSpecialization() != null && !doctorData.getSpecialization().isEmpty() ? doctorData.getSpecialization() : "NA");
        dataRow.createCell(3).setCellValue(doctorData.getRating() != null && !doctorData.getRating().isEmpty() ? doctorData.getRating() : "NA");
        dataRow.createCell(4).setCellValue(doctorData.getEducation() != null && !doctorData.getEducation().isEmpty() ? doctorData.getEducation() : "NA");

                String[] imageUrl = doctorData.getAdditionalPhotos();
                for (int i = 0; i < imageUrl.length; i++) {
                    dataRow.createCell(5 + i).setCellValue(imageUrl[i]);
                }
                String[] clinicName = doctorData.getAdditionalPhotos();
                for (int i = 0; i < imageUrl.length; i++) {
                    dataRow.createCell(5 + i).setCellValue(imageUrl[i]);
                }
            }

            // Writing to Excel file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            System.out.println("Doctor information successfully written to Excel file at: " + filePath);

        } catch (IOException e) {
            logger.error("Error writing data to Excel file", e);
        }
    }
}

class DoctorData {
    private String doctorName;
    private String profileImage;
    private String specialization;
    private String rating;
    private String education;
    private String[] additionalPhotos;
    private String[] clinicNames;
    private String[] clinicContactNumber;

    public DoctorData(String doctorName, String profileImage, String specialization, String rating, String education, String[] additionalPhotos, String[] clinicName, String[] clinicContactNumber) {
        this.doctorName = doctorName;
        this.profileImage = profileImage;
        this.specialization = specialization;
        this.rating = rating;
        this.education = education;
        this.additionalPhotos = additionalPhotos;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getRating() {
        return rating;
    }

    public String getEducation() {
        return education;
    }

    public String[] getAdditionalPhotos() {
        return additionalPhotos;
    }
    
    public String[] getDoctorClinicName() {
        return clinicName;
    }
    public String[] getDoctorClinicContactNumber() {
        return clinicContactNumber;
    }
   
}
