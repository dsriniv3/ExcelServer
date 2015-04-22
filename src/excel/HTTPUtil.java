package excel;

/*
 * @author Kevin Lubick
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class HTTPUtil {
	
	public static final String BASE_URL = "127.0.0.1";
	@Deprecated
	public static final int PORT = 5005;

	private static final Logger logger = Logger.getLogger(HTTPUtil.class);
	
	public static String getRequestBody(HttpServletRequest request) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String data;
		
		while((data= br.readLine())!=null){
			sb.append(data);
		}	
		
		return sb.toString();
	}
	
	/**
	 * Gets the body of a POST or PULL request and parses it as a JSON object
	 * @param request
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	public static String getResponseBody(HttpResponse response) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		InputStream ips = response.getEntity().getContent();
	
		try (BufferedReader buf = new BufferedReader(new InputStreamReader(ips, StandardCharsets.UTF_8));)
		{
			String s;
			while (true)
			{
				s = buf.readLine();
				if (s == null || s.length() == 0)
					break;
				sb.append(s);
				}
			}
			return sb.toString();
		}
		
	public static JSONObject getRequestJSON(HttpServletRequest request) throws JSONException
	{
		String jsonString;
		try
		{
			jsonString = getRequestBody(request);
		}
		catch (IOException e)
		{
			throw new JSONException(e);
		}
		return new JSONObject(jsonString);
	}
	
	public static void addAuth(HttpMessage httpMessage, String username, String userEmail, String token)
	{
		String authString = String.format("%s|%s:%s", userEmail,username, token);
		httpMessage.addHeader("Authorization", "Basic "+Base64.encodeBase64String(authString.getBytes(StandardCharsets.UTF_8)));
	}	
	
	public static URI buildExternalHttpURI(String path) throws URISyntaxException
	{
		//return new URI("http", BASE_URL, path, null);
		return new URI("http", null, BASE_URL, PORT, path, null,null);
	}
}
