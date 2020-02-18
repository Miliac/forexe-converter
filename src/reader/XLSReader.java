package reader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class XLSReader {

    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream(new File("src/resources/Balanta cumulata_nov_2019.XLSX"));

        //creating Workbook instance that refers to .xlsx file
        XSSFWorkbook wb = new XSSFWorkbook(fis);

        //creating a Sheet object to retrieve object
        XSSFSheet sheet = wb.getSheet("Pagina 2");

        //iterating over excel file
        Iterator<Row> itr = sheet.iterator();

        while (itr.hasNext()) {
            Row row = itr.next();

            //iterating over each column
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                switch (cell.getCellType()) {
                    //field that represents string cell type
                    case STRING:
                        System.out.print(cell.getStringCellValue() + "\t\t\t");
                        break;
                    //field that represents number cell type
                    case NUMERIC:
                        System.out.print(cell.getNumericCellValue() + "\t\t\t");
                        break;
                    default:
                }
            }
            System.out.println();
        }
    }
}
