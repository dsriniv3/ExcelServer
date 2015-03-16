package excel;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;

class ScannerHandler extends AbstractHandler
 {
	DirectoryList directory;
	ScannerHandler(DirectoryList dir)
	{
		directory=dir;
	}
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException
	{
		response.setContentType("text/html;charset=utf-8");
	    response.setStatus(HttpServletResponse.SC_OK);
	    baseRequest.setHandled(true);
	    
		String directory_path = request.getParameter("dir");
		String is_private = request.getParameter("isPrivate");
		boolean isPrivate=true;
		if(is_private=="false")
		{
			isPrivate=false;
		}
		directory.put(new Directory(directory_path,isPrivate));
		
	}
 }

class Runner implements Runnable
{
	DirectoryList directory;
	Runner(DirectoryList dir)
	{
		directory=dir;
	}
	public void run()
	{
		int index=0;
		while(true)
		{
				Directory dir=directory.get(index);
				if(dir!=null)
				{
					System.out.println("About to scan "+dir.getDirectory());
					ExcelRecommender.scan(dir);
					index++;
				}
			
		}
	}
}
class DirectoryList
{
	private ArrayList<Directory> directories;
	private boolean available = false;
	DirectoryList()
	{
		directories=new ArrayList<Directory>();
	}
	public synchronized Directory get(int index)
	{
		while(available==false)
		{
			try
			{
				wait();
			}catch(InterruptedException e){
				//to-do
			}
		}
		available=false;
		notifyAll();
		return directories.get(index);
	}
	public synchronized void put(Directory dir)
	{
		directories.add(dir);
		available=true;
		notifyAll();
	}
}
class Directory
{
	private String directory;
	private boolean isPrivate;
	Directory(String directory, boolean isPrivate)
	{
		this.directory=directory;
		this.isPrivate=isPrivate;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	public boolean isPrivate() {
		return isPrivate;
	}
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
}
public class ExcelServer {
	public static void main(String[] args) throws Exception
	{
			DirectoryList directory = new DirectoryList();
			Thread scannerThread = new Thread(new Runner(directory));
			scannerThread.start();
	        Server server = new Server(5001);
	        //set Scan context
	        ContextHandler scanContext = new ContextHandler();
	        scanContext.setContextPath("/scan");
	        scanContext.setResourceBase(".");
	        scanContext.setClassLoader(Thread.currentThread().getContextClassLoader());
	        scanContext.setHandler(new ScannerHandler(directory));
	        server.setHandler(scanContext);
	        server.start();
	        server.join();
	        
	    }
}
