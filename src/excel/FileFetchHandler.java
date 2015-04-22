package excel;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.JSONException;
import org.json.JSONObject;


public class FileFetchHandler extends AbstractHandler
{
	private static final Logger logger = Logger.getLogger(FileFetchHandler.class);
	
	private static final String SHARE_PATH = "sharedFiles/";
	@Override
	
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		String filePath = null;
		String file_data = null;
		String file_ext = null;
		
		CloseableHttpClient client = HttpClients.createDefault();
		
        response.setContentType("text/html;charset=utf-8");
	    response.setStatus(HttpServletResponse.SC_OK);
	    baseRequest.setHandled(true);
	    
	    String toolName = request.getParameter("toolName");
	    String ownerName = request.getParameter("ownerName");
	    String userName = request.getParameter("userName");
	    String userEmail = request.getParameter("userEmail");
	    String userToken = request.getParameter("userToken");
	    	      
	    /* Check if already present in the database. */
	    
	    filePath = DatabaseUtil.fetchSharedFiles(toolName, ownerName);
	    File sharedFile = null;
	    
	    /* Else, Send a post request to socaster-api. Decode the file, create a temp file and path to database. */
	    
	    if(filePath==null)
	    {
	    	HttpPost postRequest=null;
			
			try{
			
				JSONObject jobj = new JSONObject();
				jobj.put("ownerName", ownerName);
				jobj.put("toolName", toolName);
				
				URI putUri = HTTPUtil.buildExternalHttpURI("/fetch-files");
				postRequest = new HttpPost(putUri);
				HTTPUtil.addAuth(postRequest, userName, userEmail, userToken);
				
				StringEntity input = new StringEntity(jobj.toString());
				logger.info("Object being sent "+ jobj.toString());
				input.setContentType("application/json");
				postRequest.setEntity(input);
				 
				HttpResponse httpresponse = client.execute(postRequest);
				 
				String responseString = HTTPUtil.getResponseBody(httpresponse);
				logger.trace(responseString);
				JSONObject responseObj = new JSONObject(responseString);
				
				file_data = responseObj.getString("file_data");
				file_ext = responseObj.getString("file_ext");
				
				if (!"OK".equals(responseObj.getString("_status"))) {
					logger.info("There was a problem reporting toolusage event "+jobj+" response: "+responseObj.toString(2));
				}
			
			}catch (URISyntaxException e) {
				logger.error("Problem reporting tool info",e);
				e.printStackTrace();
			} catch (JSONException e) {
				logger.error("Problem reporting tool info",e);
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				logger.error("Problem reporting tool info",e);
				e.printStackTrace();
			}catch (IOException e)
			{
				logger.error("Problem reporting tool info",e);
				e.printStackTrace();
			}
			finally {
				postRequest.releaseConnection();
			}
			ownerName = ownerName.substring(0,ownerName.indexOf('@'));
			filePath = SHARE_PATH+toolName+"_"+ownerName+"."+file_ext;
			sharedFile = new File(filePath);
			DatabaseUtil.addSharedFile(toolName, ownerName, filePath);
			sharedFile.createNewFile();
			Base64Converter.base64DecodeFile(file_data, filePath);
		}
	    
	    response.getWriter().println(sharedFile.getAbsolutePath());
	}

}
