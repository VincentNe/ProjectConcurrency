package domain;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class WriteFile {
    private XSSFWorkbook workbook;

    private Sheet customerSheet;

    public WriteFile(){
        workbook = new XSSFWorkbook();
        customerSheet = workbook.createSheet("Customers");
        Row row = customerSheet.createRow(0);
        row.createCell(0).setCellValue("Name");
        row.createCell(1).setCellValue("entryTime");
        row.createCell(2).setCellValue("itemCount");
        row.createCell(3).setCellValue("leftEarly");
        row.createCell(4).setCellValue("customerArrivedAddCounter");
        row.createCell(5).setCellValue("customerLeftTime");
        row.createCell(6).setCellValue("checkoutName");
        row.createCell(7).setCellValue("startScanningTime");
        row.createCell(8).setCellValue("changedCheckout");

    }

    public synchronized void writeCustomerSpawn(){

    }


    private void save(){
        try {
            FileOutputStream fileOut = new FileOutputStream(new File("data.xlsx"));
            workbook.write(fileOut);
            workbook.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void createRow(int index , String name, long entryTime, int itemCount, boolean leftEarly, long customerArrivedAddCounter, long customerLeftTime, String checkoutName, long startScanningTime, int changedCheckout ){
        Row row = customerSheet.createRow(index);
        row.createCell(0).setCellValue(name);
        row.createCell(1).setCellValue(entryTime);
        row.createCell(2).setCellValue(itemCount);
        row.createCell(3).setCellValue(leftEarly);
        row.createCell(4).setCellValue(customerArrivedAddCounter);
        row.createCell(5).setCellValue(customerLeftTime);
        row.createCell(6).setCellValue(checkoutName);
        row.createCell(7).setCellValue(startScanningTime);
        row.createCell(8).setCellValue(changedCheckout);

    }
    public void saveSimulation(SimulationStatistics simulationStatistics) {
       CustomerHelperService customerHelperService= simulationStatistics.getCustomerHelperService();
       HashMap<String,CustomerHelper> customers = customerHelperService.getCustomers();

        Iterator<CustomerHelper> itr = customers.values().iterator();
        int i = 1;
        while (itr.hasNext()){
            CustomerHelper customer = itr.next();
            createRow(i,customer.getName(),customer.getEntryTime(),customer.getItemCount(),customer.isLeftEarly(),customer.getCustomerArrivedAddCounter(),customer.getCustomerLeftTime(),customer.getCheckoutName(),customer.getStartScanningTime(),customer.getChangedCheckout());
            i++;
        }
        save();
        JOptionPane.showMessageDialog(null,"Your file has been saved");
    }
}


