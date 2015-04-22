package excel;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;


public class ExcelServer {
		
	public static void main(String[] args) throws Exception
	{
			/* List of requests having lists of files to process */
			BasicConfigurator.configure();
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
	        
	        ContextHandler fetchContext = new ContextHandler();
	        fetchContext.setContextPath("/fetch");
	        fetchContext.setResourceBase(".");
	        fetchContext.setClassLoader(Thread.currentThread().getContextClassLoader());
	        fetchContext.setHandler(new FileFetchHandler());
	        
	        UploadHandler uploadHandler = new UploadHandler(fileListArray);
	        handlerCollection.setHandlers(new Handler[] {shareContext, fetchContext, uploadHandler});
	        
	        server.setHandler(handlerCollection);
	        server.start();
	        server.join();
	    }
}
