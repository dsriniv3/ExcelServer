package excel;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.json.JSONException;
import org.json.JSONObject;
/*
 * This file contains the main server class and all the required handlers.
 */

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
		directory.put(directory_path);
		
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
				String str=directory.get(index);
				if(str!=null)
				{
					System.out.println("About to scan "+str);
					ExcelRecommender.scan(str);
					index++;
				}
			
		}
	}
}
class DirectoryList
{
	private ArrayList<String> directories;
	private boolean available = false;
	DirectoryList()
	{
		directories=new ArrayList<String>();
	}
	public synchronized String get(int index)
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
	public synchronized void put(String dir)
	{
		directories.add(dir);
		available=true;
		notifyAll();
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
