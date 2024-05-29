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
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticularDoctorData_Excel {

    private static final Logger logger = Logger.getLogger(ParticularDoctorData_Excel.class);

    public static void main(String[] args) {
        // To Configure log4j for the Logging purpose
        PropertyConfigurator.configure("src/main/resources/log4j.properties");

        WebDriverManager.edgedriver().setup();
        WebDriver driver = new EdgeDriver();
        driver.manage().window().maximize();
        
        List<String> urls = readUrlsFromExcel("DoctorUrl.xlsx");

        List<DoctorData> doctorDataList = new ArrayList<>();

        for (String url : urls) {
            driver.get(url);
            try {
                System.out.println("Doctor Url : " + url);
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

                String doctorSpecialization = driver.findElement(By.xpath("//*[@id='doctorprofilecard']/div[1]/p")).getText();
                String doctorRating = driver.findElement(By.xpath("//*[@id = 'doctorprofilecard']/div[2]/span[1]")).getText();
                String doctorRegistration = driver.findElement(By.xpath("//*[@id = 'doctorprofilecard']/div[3]/div[1]/div/p[1]")).getText();
                String doctorEducation = driver.findElement(By.xpath("//*[@class = 'educationbox']/p[2]")).getText();
                
                // To Fetch Additional Photos of the Doctor!
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); 
                List<WebElement> imageThumbnails = driver.findElements(By.cssSelector("#doctorphotos .image-thumbnail"));
                String[] additionalPhotosUrls = new String[5];

                for (int i = 0; i < 5; i++) {
                    if (i < imageThumbnails.size()) {
                        additionalPhotosUrls[i] = extractImageUrl(imageThumbnails.get(i).getAttribute("style"));
                        System.out.println("Doctor Additional Photos " + (i + 1) + " URL: " + additionalPhotosUrls[i]);
                    } else {
                        additionalPhotosUrls[i] = "NA";
                        System.out.println("Doctor Additional Photos " + (i + 1) + " URL: NA");
                    }
                }
                
                DoctorData1 doctorData = new DoctorData1(doctorName, doctorProfileImage, doctorSpecialization, doctorRating, doctorRegistration, doctorEducation, additionalPhotosUrls);
                doctorDataList.add(doctorData);

            } catch (Exception e) {
                logger.error("Error scraping data from URL: " + url, e);
            }
        }

        driver.quit();

        writeDataToExcel(doctorDataList, "DoctorInfo.xlsx");
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
                "Doctor Name", "Doctor Profile Image", "Doctor Specialization and Experience", "Doctor Rating", "Doctor Registration",
                "Doctor Education", "Doctor Additional Photo 1", "Doctor Additional Photo 2",
                "Doctor Additional Photo 3", "Doctor Additional Photo 4", "Doctor Additional Photo 5"
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
                
                dataRow.createCell(0).setCellValue(doctorData.getDoctorName());
                dataRow.createCell(1).setCellValue(doctorData.getProfileImage());
                dataRow.createCell(2).setCellValue(doctorData.getSpecialization());
                dataRow.createCell(3).setCellValue(doctorData.getRating());
//                dataRow.createCell(4).setCellValue(doctorData.getRegistration());
                dataRow.createCell(4).setCellValue(doctorData.getEducation());

                String[] imageUrl = doctorData.getAdditionalPhotos();
                for (int i = 0; i < imageUrl.length; i++) {
                    dataRow.createCell(6 + i).setCellValue(imageUrl[i]);
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

class DoctorData1 {
    private String doctorName;
    private String profileImage;
    private String specialization;
    private String rating;
//    private String registration;
    private String education;
    private String[] additionalPhotos;

    public DoctorData1(String doctorName, String profileImage, String specialization, String rating, String registration, String education, String[] additionalPhotos) {
        this.doctorName = doctorName;
        this.profileImage = profileImage;
        this.specialization = specialization;
        this.rating = rating;
//        this.registration = registration;
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

//    public String getRegistration() {
//        return registration;
//    }

    public String getEducation() {
        return education;
    }

    public String[] getAdditionalPhotos() {
        return additionalPhotos;
    }
}
