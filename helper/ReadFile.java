package helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.nio.charset.Charset;
import java.nio.file.*;

public class ReadFile {
	public static Map<String, String> getFile(String file) {
		Map<String, String> data = new HashMap<>();
		try(BufferedReader br = new BufferedReader(new FileReader(file))){
			String currentLine;
			
			while ((currentLine = br.readLine()) != null) {
				String[] line = currentLine.split(" ");
				data.put(line[0], line[1]);
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		return data;
	}
}
