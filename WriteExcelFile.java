package helper;

import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;

import org.json.simple.JSONObject;
import java.util.Map;
import java.util.*;

public class WriteExcelFile {
	
	private static final String filename = "C:\\Usec/register.xlsx";
	
	public static void main(String[] args) throws Exception{
		
		FileInputStream registerXls = new FileInputStream(new File(filename));
		
		//XSSFWorkbook workbook = new XSSFWorkbook(registerXls);
		//XSSFSheet sheet = workbook.getSheetAt(0);
		
		Workbook wb = WorkbookFactory.create(registerXls);
		
		Sheet sheet = wb.getSheetAt(0);
		
		int rowNum = 0;
		
		//Fint empty row
		while (true) {
			if (!sheet.getRow(rowNum).equals(null)) {
				break;
			}
			rowNum++;
		}
		System.out.println(rowNum);
		//JSONObject data = Registration.readJsonFile("C:\\Usec/register.json");
		
		//Konvertera JSON till Map
		//String[] dataArray = d.split(",");
		Map<String, String> dataMapping = new HashMap<String, String>();
		String[] keys = {"säljare", "namn", "pris", "adress"};
		//System.out.println(data.keySet().toArray().length);
		
		//String[] keyArray = (String[]) data.keySet().toArray();
		/*
		for (int j = 0 ; j < keyArray.length; j++) {
			String dataString = (String) data.get(keyArray[j]);
			String[] dataArray = (String[]) dataString.split(",");
			for(int i = 0; i < dataArray.length; i++) {
				String[] tempArray = dataArray[i].split(":");
				tempArray[1] = tempArray[1].replaceAll("\\}", "");
				dataMapping.put(keys[i], (String)tempArray[1]);
			}
		}
		System.out.println(dataMapping.get("adress"));
		
		registerXls.close();
		
		try {
			FileOutputStream outputStream = new FileOutputStream(filename);
			wb.write(outputStream);
			wb.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Done");
		*/
	}
	
}
