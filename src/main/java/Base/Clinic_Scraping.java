package Base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utilities.Practice_DataScrap;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Clinic_Scraping {
    private static final Logger logger = Logger.getLogger(Practice_DataScrap.class);

    public static void main(String[] args) {
        // Configure log4j for logging
        PropertyConfigurator.configure("src/main/resources/log4j.properties");

        WebDriverManager.edgedriver().setup();
        WebDriver driver = new EdgeDriver();
        driver.manage().window().maximize();
        
        List<String> urls = readUrlsFromExcel("DoctorUrl.xlsx");
        
        List<String[]> clinicData = new ArrayList<>();

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

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            List<WebElement> clinicNames = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id='doctorclinics']/div/p[1]")));
            List<WebElement> phoneNumbers = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id='doctorclinics']/div/div[2]/a[1]")));

            int maxSize = Math.max(clinicNames.size(), phoneNumbers.size());
            for (int i = 0; i < maxSize; i++) {
                String clinicName = i < clinicNames.size() ? clinicNames.get(i).getText() : "NA";
                String phoneNumber = i < phoneNumbers.size() ? phoneNumbers.get(i).getAttribute("href") : "NA";
                clinicData.add(new String[]{clinicName, phoneNumber});
            }
        }

        for (String[] data : clinicData) {
            System.out.println("Clinic Name: " + data[0]);
            System.out.println("Phone Number: " + data[1]);
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
