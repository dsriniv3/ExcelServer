package excel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import excel.HTTPUtil;

public class hey {
public static void main(String args[]) throws URISyntaxException, UnsupportedEncodingException
{
	HttpClient httpclient=null;
    
    try {
        httpclient = HttpClients.createDefault();
		HttpPost request = new HttpPost("http://localhost:4443/api/shareFile");
		List<NameValuePair> params = new ArrayList<NameValuePair>(5);
		
		/*params.add(new BasicNameValuePair("toolName", "ABS"));
		params.add(new BasicNameValuePair("ownerName", "kjlubick+test@ncsu.edu"));
		params.add(new BasicNameValuePair("userName", "Kevin Test"));
		params.add(new BasicNameValuePair("userEmail", "kjlubick+test@ncsu.edu"));
		params.add(new BasicNameValuePair("token", "221ed3d8-6a09-4967-91b6-482783ec5313"));
		*/
		params.add(new BasicNameValuePair("ownerName", "kjlubick+test@ncsu.edu"));
		params.add(new BasicNameValuePair("userName", "Kevin Test"));
		
		request.addHeader("content-type", "application/x-www-form-urlencoded");
		request.setEntity(new UrlEncodedFormEntity(params));
	    HttpResponse response = httpclient.execute(request);
	    
	    String responseString = HTTPUtil.getResponseBody(response);
        //JSONObject responseObj = new JSONObject(responseString);
		
        System.out.println("Response:"+responseString);       
    	}catch (Exception ex) {
    		System.out.println("Error while sending tool usages to LocalHub");
    		ex.printStackTrace();
    	} finally {
    		httpclient.getConnectionManager().shutdown();
    	}
	}
}

