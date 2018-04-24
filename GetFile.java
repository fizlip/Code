package DriveConfig;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import DriveConfig.Quickstart;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream; 
import java.util.List;

import com.google.api.services.drive.model.*;


public class GetFile {

  // ...

  /**
   * Print a file's metadata.
   *
   * @param service Drive API service instance.
   * @param fileId ID of the file to print metadata for.
   */
  private static void printFile(Drive service, String fileId) {

    try {
      File file = service.files().get(fileId).execute();
      System.out.println("Title: " + file.getName());
      System.out.println("Id: " + file.getId());
      System.out.println("Description: " + file.getDescription());
      System.out.println("MIME type: " + file.getMimeType());
    } catch (IOException e) {
      System.out.println("An error occurred: " + e);
    }
  }

  /**
   * Download a file's content.
   *
   * @param service Drive API service instance.
   * @param file Drive File instance.
   * @return InputStream containing the file's content if successful,
   *         {@code null} otherwise.
   */
  private static InputStream downloadFile(Drive service, File file) {
    if (file.getWebContentLink() != null && file.getWebContentLink().length() > 0) {
      try {
        HttpResponse resp =
            service.getRequestFactory().buildGetRequest(new GenericUrl(file.getWebContentLink()))
                .execute();
        return resp.getContent();
      } catch (IOException e) {
        // An error occurred.
        e.printStackTrace();
        return null;
      }
    } else {
      // The file doesn't have any content stored on Drive.
      return null;
    }
  }
  
  public static String searchDrive(String searchedFile, Drive service) {
	  //Initalisera som inga resultat, om inga resultat finns kommer strängen inte att ändras
	  String searchResult = "Inga resultat";
	  
	  try {
		  FileList result = service.files().list()
				  			.setPageSize(1000)
				  			.execute();
		  		  
		  List<File> files = result.getFiles();
		  
		  for(File file : files) {
			  if(file.getName().equals(searchedFile)){
				  String fileId = file.getId();
				  printFile(service, fileId);
				  OutputStream outputStream = new ByteArrayOutputStream();
				  service.files().export(fileId, "text/plain")
				      .executeMediaAndDownloadTo(outputStream);
				  searchResult = "Meta data: " + file.toPrettyString() + "\n\n" + service.files().list() + "\n";
				  searchResult += outputStream.toString();

				  
				  searchResult = searchResult.replaceAll("Ã¥", "å");searchResult = searchResult.replaceAll("Ã…", "Å");
				  searchResult = searchResult.replaceAll("Ã¤", "ä");searchResult = searchResult.replaceAll("Ã„", "Ä");
				  searchResult = searchResult.replaceAll("Ã¶", "ö");searchResult = searchResult.replaceAll("Ã–", "Ö");
				  searchResult = searchResult.replaceAll("Ã©", "é");
				  outputStream.close();
				  return searchResult;
			  }
		  }
	  }
	  catch(IOException e) {System.out.println("Google Drive connection failure");e.printStackTrace();}
	  return searchResult;
  }
  public static void main(String[] args) {
	  //This is a test.
	  //searchDrive("Makulering brev");
  }
  // ...
}