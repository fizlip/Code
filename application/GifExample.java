package application;
import javax.swing.*;

import java.net.*;
import java.nio.file.*;
import java.io.File.*;
import java.io.IOException;
import java.nio.charset.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Random;

public class GifExample {
	private static boolean alwaysOnTop = true;
	private static String filename;
	private static List<String> gifNames = new ArrayList<String>();
	public static String specificGif;
	public static Integer amountSold = Integer.parseInt(Intermediate.getProperty(AmountSoldText.bestProperty()));
	
	private static boolean isNewRecord = false;
	
	public static String filePath = "C:\\ISGG\\gifs\\";
	
	public static void runGif() throws MalformedURLException {
		checkIfRecord();
		if (!isNewRecord) {
			Random rand = new Random();
			try {
				readRandomGifFile();
				int randomFile = rand.nextInt(gifNames.size());
				filename = filePath + gifNames.get(randomFile);
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
			filename = filePath + "kingOftheWorld.gif";
		}
		Icon icon = new ImageIcon(filename);
		JLabel label = new JLabel(icon);
		
		JFrame f = new JFrame("Nytt Sälj!");
		f.getContentPane().add(label);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.pack();
		f.setLocation(355,320);
		f.setAlwaysOnTop(alwaysOnTop);
		f.setVisible(true);
	}
	
	public static void checkIfRecord() {
		System.out.println(amountSold);
		if (UsecController.amountSold == amountSold) {isNewRecord = true;}
		else {isNewRecord =  false;}
	}
	
	public static void changeGif(String fname) {
		filename = fname;
	}
	
	public static void readRandomGifFile() throws IOException {
		
		try (BufferedReader br = new BufferedReader(new FileReader(filePath + "gifFiles.txt"))){
			
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				gifNames.add(currentLine);
			}
		}catch (IOException e) {e.printStackTrace();}
		
	}
	
	public static void setOnTop(boolean b) {
		alwaysOnTop = b;
	}
}
