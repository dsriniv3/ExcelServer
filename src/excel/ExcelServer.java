package excel;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;


public class ExcelServer {
	
	
	public static void main(String[] args) throws Exception
	{
			/* List of requests having lists of files to process */
			FileListArray fileListArray = new FileListArray();
			HandlerCollection handlerCollection = new HandlerCollection();
			
			/* Thread that services the scan requests */
			Thread scannerThread = new Thread(new ScannerThread(fileListArray));
			scannerThread.start();
	        Server server = new Server(5001);
	        
	        ContextHandler shareContext = new ContextHandler();
	        shareContext.setContextPath("/share");
	        shareContext.setResourceBase(".");
	        shareContext.setClassLoader(Thread.currentThread().getContextClassLoader());
	        shareContext.setHandler(new ShareHandler());
	        
	        UploadHandler uploadHandler = new UploadHandler(fileListArray);
	        handlerCollection.setHandlers(new Handler[] {shareContext});
	        uploadHandler.setHandler(handlerCollection);
	        
	        server.setHandler(uploadHandler);
	        server.start();
	        server.join();
	        
	    }
}
