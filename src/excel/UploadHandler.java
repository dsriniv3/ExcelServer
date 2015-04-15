package excel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.apache.log4j.Logger;

public class UploadHandler extends HandlerWrapper
{
	private static final Logger logger = Logger.getLogger(UploadHandler.class);
	
	public FileListArray fileListArray;
	private static int fileCount=0;
	
	private static final String CACHE_PATH = "temp";
	private static final int CACHE_SIZE = 100*(int)Math.pow(10,6);
    private static final int MAX_REQUEST_SIZE = 10*(int)Math.pow(10,6);
    private static final int MAX_FILE_SIZE = 1*(int)Math.pow(10,6);
    
    private static final MultipartConfigElement MULTI_PART_CONFIG = new MultipartConfigElement(
        System.getProperty("java.io.tmpdir"));

    UploadHandler(FileListArray fileListArray)
    {
    	this.fileListArray = fileListArray;
    }

    public static boolean isMultipartRequest(ServletRequest request) {
    	
    	return request.getContentType() != null
          && request.getContentType().startsWith("multipart/form-data");
      
    }
    
    public static void enableMultipartSupport(HttpServletRequest request) {
        request.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, MULTI_PART_CONFIG);
      }
	
    
	public void handle(String target, Request baseRequest, HttpServletRequest req, HttpServletResponse response) 
	throws IOException, ServletException
	{
		ArrayList<File> files=null;
		boolean isPrivate = true;
		
		boolean multipartRequest = isMultipartRequest(req);
		if (multipartRequest) {
		      enableMultipartSupport(req);
		}
		
		response.setContentType("text/html;charset=utf-8");
	    response.setStatus(HttpServletResponse.SC_OK);
	    baseRequest.setHandled(true);
		
	    if (multipartRequest)
        {
			files = new ArrayList<File>();
            
			DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setRepository(new File(CACHE_PATH));
            factory.setSizeThreshold(CACHE_SIZE);
 
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(MAX_REQUEST_SIZE);
            upload.setFileSizeMax(MAX_FILE_SIZE);
            
            List<FileItem> items = null;
            try
            {
            	items = upload.parseRequest(req);
            }catch(FileUploadException e)
            {
            	logger.error("The request could not be parsed");
            	
            }
 
            // Process the uploaded items
            Iterator<FileItem> iter = items.iterator();
            int count=0;
            while (iter.hasNext())
            {
                FileItem item = (FileItem) iter.next();
                if (item.isFormField())
                {
                	/* To-Do: Find why code never enters this loop */
                	/* Need to set files to private here */
                }
                else
                {
                	File newfile = new File(CACHE_PATH+"/File"+fileCount+".xlsx");
                	newfile.createNewFile();
                	try 
                	{
						item.write(newfile);
					} catch (Exception e) {
						logger.error("Writing uploaded file to temporary file File"+ fileCount  + " failed");
					}
                    files.add(count, newfile);
                    count++;
                    fileCount++;
                }
            }
        }
        fileListArray.put(new FileList(files,isPrivate));
    }
}
