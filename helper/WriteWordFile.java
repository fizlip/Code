package helper;

import java.io.File;
import java.io.FileOutputStream;

//import org.apache.poi.xwpf.usermodel.*;



/*Denna klass skapar en Word fil med logga i �vre v�nster 
 * h�rn, kund-id i h�ger h�rn och ett kundmeddelande under allt detta
 */

public class WriteWordFile {
	
	public static int logoFontSize = 20;	//Font storlek f�r loggan
	public static int customerIDFontSize = 15;	//Font storlek f�r kund-id
	/*
	//Skriv ett standardiserat Word dokument
	public static void writeDocument(String filename, String id, String customerName) throws Exception{
		// Skapa ett nytt word-dokument
		XWPFDocument doc = new XWPFDocument();
		FileOutputStream file = new FileOutputStream(new File(filename));
		
		// Skriv dokumentet
		writeLogo(doc);		
		writeCustomerID(doc, id);	
		writeMessage(doc, customerName);
				
		// Skriv data till angiven fil
		doc.write(file);
		file.close();
		System.out.println(filename + " has succesfully been created");
	}
	
	//Skriv loggan
	private static void writeLogo(XWPFDocument doc) {
		//Skapa en ny paragraf p� v�nster sida av papperet
		XWPFParagraph logo = doc.createParagraph();
		logo.setAlignment(ParagraphAlignment.LEFT);
		
		// Skriv det bl�a U:et
		XWPFRun run = logo.createRun();
		run.setFontSize(logoFontSize); run.setText("U|"); run.setBold(true); run.setColor("5ce1ff");
		
		// Skriv security i svart
		run = logo.createRun();
		run.setFontSize(logoFontSize); run.setBold(true); run.setText("SECURITY");run.addBreak();
		
		//Skriv den lilla texten under loggan
		run = logo.createRun();
		run.setItalic(true); run.setFontSize(10); run.setText("        S�kerhet f�r dig");
	}
	
	// Metod som skriver kund-id p� h�ger sida av papperet
	private static void writeCustomerID(XWPFDocument doc, String id) {
		//Skapa en ny paragraf p� h�ger sida av papperet
		XWPFParagraph customerID = doc.createParagraph();
		customerID.setAlignment(ParagraphAlignment.RIGHT);

		//Skriv kund-id med given storlek
		XWPFRun customerRun = customerID.createRun();
		customerRun.setFontSize(customerIDFontSize);

		customerRun.setText(id);
	}
	
	public static void writeMessage(XWPFDocument doc, String name) {
		// Skapa en ny paragraf d�r meddelandet ska skrivas
		XWPFParagraph message = doc.createParagraph();
		XWPFRun messageRun = message.createRun();
		
		//Skriv meddelande
		messageRun.setText("Hej " + name + "!");
		messageRun.addBreak();
		messageRun.setText("Bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla");
		messageRun.addBreak();messageRun.addBreak();
		messageRun.setText("Bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla");

	}
	*/
}


