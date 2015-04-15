package excel;

import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ToolUsage {
	
	private static final Logger logger = Logger.getLogger(ToolUsage.class);
	
	private String toolName;
    private String pluginName;  /* Not needed? */
	private Date timeStamp;
	private String applicationName; /* Not needed? */
	String file=null;
	
	ToolUsage(String toolName, String file)
	{
		this.toolName = toolName.trim();
		this.timeStamp = new Date(System.currentTimeMillis());
		this.file=file;
	}

	public ToolUsage(String toolName) {
		this.toolName = toolName.trim();
		this.timeStamp = new Date(System.currentTimeMillis());
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
			logger.error("Error while forming JSON object for tool "+getToolName());
		}

		return jobj;

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
	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
	

}