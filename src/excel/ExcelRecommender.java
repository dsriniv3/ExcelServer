package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.formula.FormulaParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExcelRecommender {
	
	private static final Logger logger = Logger.getLogger(ExcelRecommender.class);
	
	public static void scan(FileList fileList)
	{
        HashMap<String, File> functions_to_file_map = new HashMap<String, File>();
		ArrayList<File> files  =  fileList.getFileList();
		Functions functions = new Functions();
		
		try
		{
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
									
									Pattern p = Pattern.compile("[A-Za-z]+\\(");
									Matcher m = p.matcher(cell.getCellFormula());
									
									while(m.find())
									{
										String extractedFormula = m.group().substring(0,m.group().length()-1);
										
										if(extractedFormula.length()!=0)
			        		    		//TO-DO: should be able to send multiple files for every function later
			        		    		if(functions.functionMap.get(extractedFormula)!=null)
			        		    		{
			        		    			if(functions_to_file_map.get(extractedFormula)==null)
			        		    			{
			        		    				functions_to_file_map.put(extractedFormula,excelFile);
			        		    				logger.info("Function:"+extractedFormula+" Filename:"+excelFile.getName());
			        		    			}
			        		    			      		    				
			        		    		}
									}
								}
							}
						}
					}
				} catch (InvalidFormatException e) {
					logger.error("Invalid Format Exception while scanning "+excelFile.getName());
				} catch (IOException e) {
					logger.error("IOException while analyzing "+excelFile.getName());
				} catch ( FormulaParseException e) {
				//This exception occurs when a user-defined function is encountered.
				//Since we deal with Excel Functions, best to ignore this exception
				}
			}
		}catch(NullPointerException e){
		}
		
		try
		{
			reportTools(functions_to_file_map, fileList.isPrivate());
		} catch (JSONException e) {
			logger.error("JSON exception occured while reporting tools");
		} 
		

	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public static void reportTools(HashMap<String,File> functionMap, boolean isPrivate) throws JSONException
	{
		
		ToolUsage[] usages = new ToolUsage[functionMap.size()];
		Iterator<Entry<String, File>> it = functionMap.entrySet().iterator();
	    
		int i=0;
		
		while (it.hasNext()) {
		    
			Map.Entry pair = (Map.Entry)it.next();
		    
			if(!isPrivate)
		    	usages[i++] = new ToolUsage(pair.getKey().toString(), Base64Converter.encodeBase64((File)pair.getValue()));
		    
			else
		    {
		    	usages[i++] = new ToolUsage(pair.getKey().toString());
		    	//store file into Database to be used later if user sets sharing settings
		    	DatabaseUtil.storeFile(pair.getKey().toString(),Base64Converter.encodeBase64((File)pair.getValue()));
		    }
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
				
				logger.info("Sending request to LocalHub with JSON object:"+jobj.toString());
				
				request.addHeader("content-type", "application/x-www-form-urlencoded");
				request.setEntity(new UrlEncodedFormEntity(params));
			    HttpResponse response = httpclient.execute(request);
		        
			    String responseString = HTTPUtil.getResponseBody(response);
		        JSONObject responseObj = new JSONObject(responseString);
				
		        if (!"OK".equals(responseObj.getString("_status")))
				{
					logger.error("Sending tool usages to LocalHub failed. "+responseObj);
				}
				
		        
		    	}catch (Exception ex) {
		    		logger.error("Error while sending tool usages to LocalHub");
		    	} finally {
		    		httpclient.getConnectionManager().shutdown();
		    	}
			}
		}
	}
