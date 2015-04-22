package excel;

/*
 * Handler to aid the Localhub in sharing files. It provides the files to be uploaded onto the central
 * server to LocalHub.
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class ShareHandler extends AbstractHandler
{
    private static final Logger logger = Logger.getLogger(ShareHandler.class);

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	
		response.setContentType("text/html;charset=utf-8");
	    response.setStatus(HttpServletResponse.SC_OK);
	    
	    String toolName=request.getParameter("toolName");
	    
	    logger.info("Getting file to service share request for "+toolName);
	    String[] file_details = DatabaseUtil.fileForTool(toolName);
	    
	    if(file_details!=null)
	    {
	    	response.getWriter().println(file_details[0]);	
	    	response.getWriter().println(file_details[1]);
	    }
	    baseRequest.setHandled(true);
	}
	
}