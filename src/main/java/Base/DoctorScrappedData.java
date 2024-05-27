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

public class DoctorScrappedData {

    public static void main(String[] args) throws IOException {
        WebDriver driver = null;
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.get("https://kivihealth.com/jaipur/doctors");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // Add column names in the first row
        addColumnNames(sheet);

        int rowNum = 1; // Start from row 1 for data
        while (true) {
            try {
                List<WebElement> list = driver.findElements(By.xpath("//div[@class='searchContainer']//div[contains(@class,'docBox')]"));
                for (int i = 0; i < list.size(); i++) {
                    Row row = sheet.createRow(rowNum++);
                    WebElement element = list.get(i);
                    String imageUrl = element.findElement(By.xpath(".//img")).getAttribute("src");
                    String doctorName = element.findElement(By.xpath(".//h4")).getAttribute("innerText");
                    String doctorEducation = element.findElement(By.xpath(".//h5[1]")).getAttribute("innerText");
                    String doctorSpecialization = element.findElement(By.xpath(".//h5[2]")).getAttribute("innerText");
                    String doctorExperience = element.findElement(By.xpath(".//h5[3]")).getAttribute("innerText");

                    row.createCell(0).setCellValue(doctorName);
                    row.createCell(1).setCellValue(doctorEducation);
                    row.createCell(2).setCellValue(doctorSpecialization);
                    row.createCell(3).setCellValue(doctorExperience);
                    row.createCell(4).setCellValue(imageUrl);
                }
                driver.findElement(By.xpath("//i[contains(text(),'chevron_right')]")).click();
            } catch (NoSuchElementException e) {
                break; // Exit the loop when 'chevron_right' element is not found
            }
        }

        try (FileOutputStream fileOut = new FileOutputStream("KiviDoctorsData.xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        workbook.close();
        driver.quit();
    }

/*    private static void addColumnNames(Sheet sheet) {
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Doctor Name");
        row.createCell(1).setCellValue("Doctor Education");
        row.createCell(2).setCellValue("Doctor Specialization");
        row.createCell(3).setCellValue("Doctor Experience");
        row.createCell(4).setCellValue("Doctor Image");
    } */
    
    private static void addColumnNames(Sheet sheet) {
        Row row = sheet.createRow(0);
        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Cell cell;

        cell = row.createCell(0);
        cell.setCellValue("Doctor Name");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("Doctor Education");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("Doctor Specialization");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("Doctor Experience");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("Doctor Image");
        cell.setCellStyle(style);
    }
}
