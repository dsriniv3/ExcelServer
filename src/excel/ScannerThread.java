package excel;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/* 
 *	Thread to service scanner requests  
*/
public class ScannerThread implements Runnable
{
	FileListArray fileListArray;
	ScannerThread(FileListArray fileListArray)
	{
		this.fileListArray=fileListArray;
	}
	public void run()
	{
		int index=0;
		while(true)
		{
				FileList fl=fileListArray.get(index);
				if(fl!=null)
				{
					ExcelRecommender.scan(fl);
					index++;
				}
		}
	}
}

class FileList
{
	private ArrayList<File> fileList;
	private boolean isPrivate;
	FileList(ArrayList<File> file, boolean isPrivate)
	{
		this.fileList=file;
		this.isPrivate=isPrivate;
	}
	public ArrayList<File> getFileList() {
		return fileList;
	}
	public void setFileList(ArrayList<File> file) {
		this.fileList = file;
	}
	public boolean isPrivate() {
		return isPrivate;
	}
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
}

class FileListArray
{
	private ArrayList<FileList> fileLists;
	private boolean available = false;
	
	private static final Logger logger = Logger.getLogger(FileListArray.class);
	
	FileListArray()
	{
		fileLists=new ArrayList<FileList>();
	}
	public synchronized FileList get(int index)
	{
		while(available==false)
		{
			try
			{
				wait();
			}catch(InterruptedException e){
				logger.error("Interrupted");
			}
		}
		available=false;
		notifyAll();
		return fileLists.get(index);
	}
	
	public synchronized void put(FileList fl)
	{
		fileLists.add(fl);
		available=true;
		notifyAll();
	}
}

