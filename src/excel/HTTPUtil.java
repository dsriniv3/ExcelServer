package excel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class HTTPUtil {

	public static String getRequestBody(HttpServletRequest request) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String data;
		
		while((data= br.readLine())!=null){
			sb.append(data).append('\n');
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
}
