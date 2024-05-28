package Base;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Image_Saver_Log {
    private static final Logger logger = Logger.getLogger(Image_Saver_Excel.class);

    public static void main(String[] args) {
        // Configure log4j
        PropertyConfigurator.configure("src/main/resources/log4j.properties");

        String excelFilePath = "D:\\Data_Scraping-Selenium\\target\\Doctor Details.xlsx";

        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Assuming the first sheet

            for (Row row : sheet) {
                Cell nameCell = row.getCell(0); // Assuming the doctor name is in the first column
                Cell urlCell = row.getCell(1); // Assuming the image URL is in the second column

                if (nameCell != null && urlCell != null) {
                    String doctorName = nameCell.getStringCellValue();
                    String imageUrl = urlCell.getStringCellValue();

                    // Replace spaces with underscores in the doctor name for the file name
                    String fileName = doctorName.replaceAll("\\s+", "_") + "_image.jpg";
                    String downloadPath = "C:\\Users\\naman\\Desktop\\Doctor Images\\" + fileName;

                    downloadImage(imageUrl, downloadPath);
                }
            }

        } catch (IOException e) {
            logger.error("Error reading the Excel file", e);
        }
    }

    private static void downloadImage(String imageUrl, String downloadPath) throws IOException {
        try {
            URL url = new URL(imageUrl);
            try (InputStream in = url.openStream();
                 FileOutputStream out = new FileOutputStream(downloadPath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                logger.info("Image downloaded successfully: " + downloadPath);
            } catch (IOException e) {
                logger.error("Error downloading the image from URL: " + imageUrl, e);
            }
        } catch (MalformedURLException e) {
            logger.error("Malformed URL: " + imageUrl, e);
        }
    }
}
