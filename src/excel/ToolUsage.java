package excel;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class ToolUsage {
		
	private String toolName;
    private String pluginName;
	private Date timeStamp;
	private String applicationName;
	String file;
	
	ToolUsage(String toolName, String file)
	{
		this.toolName = toolName.trim();
		this.timeStamp = new Date(System.currentTimeMillis());
		this.file=file;
		//System.out.println("Tool Usage for "+toolName+" "+file);
	}

	public String getToolName()
	{
		return toolName;
	}

	public Date getTimeStamp()
	{
		return timeStamp;
	}

	public String getApplicationName()
	{
		return this.applicationName;
	}

	public final void setApplicationName(String pluginName)
	{
		this.applicationName = pluginName;
	}
	public  JSONObject makeJSON()
	{
		JSONObject jobj = new JSONObject();
		try {
			jobj.put("pluginName", getPluginName());
			jobj.put("Tool_Name", getToolName());
			jobj.put("Tool_Timestamp", getTimeStamp().getTime());
			if(file!=null)
				jobj.put("file", file);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jobj;

	}

	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
	

}