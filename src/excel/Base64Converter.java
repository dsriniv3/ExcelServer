package excel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//import org.apache.commons.codec.binary.Base64;
import org.apache.xerces.impl.dv.util.Base64;
import org.apache.log4j.Logger;

public class Base64Converter {
	
	private static final Logger logger = Logger.getLogger(Base64Converter.class);
	
	public static String encodeBase64(File file)
	{
		 InputStream is = null;  
	     StringBuilder sb = new StringBuilder();  
	  
	        try {  
	            is = new FileInputStream(file);  
	            int bytesRead = 0;  
	            int chunkSize = 10000000;  
	            byte[] chunk = new byte[chunkSize];  

	            while ((bytesRead = is.read(chunk)) > 0) {  
	                byte[] ba = new byte[bytesRead];  
	  
	                for(int i=0; i<bytesRead; i++)  
	                    ba[i] = chunk[i];  
	                  
	                String encStr = Base64.encode(ba);  
	                sb.append(encStr);  
	            
	            }  
	  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } finally {  
	            try {  
	                is.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	            
	        }  
	    return sb.toString();
	}

	public static void base64DecodeFile(String fileToDecode, String resultingFile){ 
		
		InputStream is = null;  
        OutputStream os = null;  
  
        try 
        {  
            is = new ByteArrayInputStream(fileToDecode.getBytes());  
            os = new FileOutputStream(resultingFile);  
            int bytesRead = 0;  
            int chunkSize = 10000000;  
            byte[] chunk = new byte[chunkSize];  
            byte[] ba=null;
            
            while ((bytesRead = is.read(chunk)) > 0) {  
                ba = new byte[bytesRead];  
  
                for(int i=0; i< bytesRead; i++)  
                    ba[i] = chunk[i];  
                                 
  
                ba = Base64.decode(new String(ba));  
                os.write(ba, 0, ba.length);
            }
        }  
  
         catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                is.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            try {  
                os.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
}
