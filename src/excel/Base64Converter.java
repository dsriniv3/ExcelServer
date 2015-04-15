package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class Base64Converter {
	
	private static final Logger logger = Logger.getLogger(Base64Converter.class);
	
	public static String encodeBase64(File file)
	{
		StringBuffer encodedString = new StringBuffer();
		try
		{
	          byte[] buffer = new byte[1000];
	          FileInputStream inputStream = new FileInputStream(file);
	          int nRead = 0;
	          while((nRead = inputStream.read(buffer)) != -1)
	          {
	        	  String name=(new String(buffer));
	              byte[] encodedBytes = Base64.encodeBase64(name.getBytes());
	              encodedString.append((new String(encodedBytes)));
	          }   

	          inputStream.close(); 
	          
	    }catch(FileNotFoundException ex) {
	        logger.error("File Not Found Exception while encoding "+file.getName()+" to binary");               
	    }catch(IOException ex) {
	    	logger.error("IOException while encoding "+file.getName()+" to binary");
	    }
		
	  	return encodedString.toString();
	}


}
