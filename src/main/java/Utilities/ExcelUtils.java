package Utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtils {
	private static final String[] COLUMN_HEADERS = {"Profile Photo", "Doctor Name", "Doctor Education", "Doctor Fees"};
    private Workbook workbook;
    private Sheet sheet;

    public ExcelUtils() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Doctors Data");
        createHeaderRow();
    }

    private void createHeaderRow() {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < COLUMN_HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(COLUMN_HEADERS[i]);
        }
    }

    public void addDoctorData(String profilePhotoUrl, String name, String education, String fees, int rowIndex) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(profilePhotoUrl);
        row.createCell(1).setCellValue(name);
        row.createCell(2).setCellValue(education);
        row.createCell(3).setCellValue(fees);
    }

    public void saveToFile(String filePath) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
	
}
