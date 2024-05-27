package Base;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Doctor_URL {
    public static void main(String[] args) {
        WebDriver driver = null;
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.get("https://kivihealth.com/jaipur/doctors");
        
        // Find all the anchor elements on the page
        List<WebElement> links = driver.findElements(By.tagName("a"));

        // Create a new workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Doctor URLs");
        
        addColumnNames(sheet); // To add the column name in the excel sheet!
        
        // Create a header row
        Row headerRow = sheet.createRow(0);
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("Doctor Profile URL");
        
        // Variable to keep track of the row number
        int rowNum = 1;

        
        while (true) {
            try {
                List<WebElement> list = driver.findElements(By.xpath("//div[@class='searchContainer']//div[contains(@class,'docBox')]"));
        // Loop through each link and print the href attribute if it matches a specific pattern
        for (WebElement link : links) {
            String url = link.getAttribute("href");
            if (url != null && url.contains("iam")) {
                // Print the URL (optional)
//                System.out.println(url);                
                // Create a new row and write the URL to the Excel sheet
                Row row = sheet.createRow(rowNum++);
                Cell cell = row.createCell(0);
                cell.setCellValue(url);
            }
            driver.findElement(By.xpath("//i[contains(text(),'chevron_right')]")).click();
        }
            }catch (NoSuchElementException e) {
            break; // Exit the loop when 'chevron_right' element is not found
        }
        }

        // Write the output to an Excel file
        try (FileOutputStream fileOut = new FileOutputStream("DoctorURLs.xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the browser
        driver.quit();
    }
    private static void addColumnNames(Sheet sheet) {
        Row row = sheet.createRow(0);
        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Cell cell;

        cell = row.createCell(0);
        cell.setCellValue("Doctor Url");
        cell.setCellStyle(style);


    }
}


