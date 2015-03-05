package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.formula.FormulaParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExcelRecommender {
	public static void scan(String directory)
	{
		HashMap<String, File> functions_map = new HashMap<String, File>();
		File folder  =  new File(directory);
		File[] files  =  folder.listFiles();
		Functions functions = new Functions();
		for(File excelFile:files)
		{
			Workbook workbook;
			try 
			{
				workbook  =  WorkbookFactory.create(excelFile);
				int numberSheets = workbook.getNumberOfSheets();
				for(int i = 0;i<numberSheets;i++)
				{
					Sheet sheet = workbook.getSheetAt(i);
					for (Row row : sheet)
					{
						for (Cell cell : row)
				        {
				        	if (cell.getCellType() == Cell.CELL_TYPE_FORMULA)
				            {
				            	//to-do put Regex matcher in a separate function
			            	    Pattern p = Pattern.compile("[A-Za-z]+\\(");
			            	    Matcher m = p.matcher(cell.getCellFormula()); // get a matcher object
								while(m.find())
			        		    {
									String extractedFormula = m.group().substring(0,m.group().length()-1);
			        		    	if(extractedFormula.length()!=0)
			        		    		//TO-DO: should be able to send multiple files for every function later
			        		    		if(functions.functionMap.get(extractedFormula)!=null)
			        		    		{
			        		    			if(functions_map.get(extractedFormula)==null)
			        		    				functions_map.put(extractedFormula,excelFile);
			        		    			      		    				
			        		    		}
			        		    }
				            }
				        }
				    }
					
			    }
				//Send a Json object for every entry in the HashMap
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch ( FormulaParseException e) {
				//This exception occurs when a user-defined function is encountered.
				//Since we deal with Excel Functions, best to ignore this exception
			}
		}
		try {
			reportTools(functions_map);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@SuppressWarnings("rawtypes")
	public static void reportTools(HashMap<String,File> functionMap) throws JSONException
	{
		//making the ToolUsage objects
		//System.out.println("Preparing to report tools for functionMap of size "+functionMap.size());
		ToolUsage[] usages = new ToolUsage[functionMap.size()];
		Iterator<Entry<String, File>> it = functionMap.entrySet().iterator();
	    int i=0;
		while (it.hasNext()) {
		    Map.Entry pair = (Map.Entry)it.next();
	        usages[i++] = new ToolUsage(pair.getKey().toString(), encodeBase64((File)pair.getValue()));
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		for(int j=0;j<i;j++)
		{
			JSONObject[] jobj1 = new JSONObject[1];
			jobj1[0]=usages[j].makeJSON();
			JSONArray jobj = new JSONArray(jobj1);
            HttpClient httpclient=null;
            try {
		        httpclient = HttpClients.createDefault();
				HttpPost request = new HttpPost("http://localhost:4443/reportTool");
				List<NameValuePair> params = new ArrayList<NameValuePair>(2);
				params.add(new BasicNameValuePair("pluginName", "Excel"));
				params.add(new BasicNameValuePair("toolUsages", jobj.toString()));
				System.out.println("Seding request with JSON object:"+jobj.toString());
				request.addHeader("content-type", "application/x-www-form-urlencoded");
				request.setEntity(new UrlEncodedFormEntity(params));
			    HttpResponse response = httpclient.execute(request);
		        String responseString = HTTPUtil.getResponseBody(response);
		        JSONObject responseObj = new JSONObject(responseString);
				if (!"OK".equals(responseObj.getString("_status")))
				{
					System.out.println("Request failed");
				}
				
		        // handle response here...
		    	}catch (Exception ex) {
		        // handle exception here
		    	} finally {
		        httpclient.getConnectionManager().shutdown();
		    	}
			}
	}
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
	        ex.printStackTrace();               
	    }catch(IOException ex) {
	        ex.printStackTrace();
	    }
	  	return encodedString.toString();
	}
}
