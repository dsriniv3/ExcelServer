package excel;

import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ToolUsage {
	
	private static final Logger logger = Logger.getLogger(ToolUsage.class);
	
	private String toolName;
    private String pluginName;  
	private Date timeStamp;
	private String applicationName; 
	private String file_data=null;
	private String file_ext=null;
	
	ToolUsage(String toolName, String file, String fileExt)
	{
		this.toolName = toolName.trim();
		this.timeStamp = new Date(System.currentTimeMillis());
		this.file_data=file;
		this.file_ext=fileExt;
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
			
			if(file_data!=null)
			{
				jobj.put("file_data", file_data);
				jobj.put("file_ext", file_ext);
			}
			else
			{
				jobj.put("file_data", " ");
				jobj.put("file_ext", " ");
			}
		
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