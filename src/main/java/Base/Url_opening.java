package Base;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Url_opening {


	    public static void main(String[] args) {
	        WebDriverManager.edgedriver().setup();
	        WebDriver driver = new EdgeDriver();
	        driver.manage().window().maximize();
	        
	        List<String> urls = readUrlsFromExcel("DoctorURLs.xlsx");

	        for (String url : urls) {
	            driver.get(url);
	            // You can add a delay here if you want to wait for the page to load completely
	            try {
	                Thread.sleep(2000); // Sleep for 2 seconds
	            } catch (InterruptedException e) {
	                e.printStackTrace();
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
	            e.printStackTrace();
	        }
	        return urls;
	    }
	}


