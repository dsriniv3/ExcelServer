package excel;
import java.sql.*;

import org.apache.log4j.Logger;

/* Class to perform all database operations to the excelfiles sqlite DB */

public class DatabaseUtil {
	
	private static final Logger logger = Logger.getLogger(DatabaseUtil.class);
	
	public static void storeFile(String toolName, String encodedFile, String extension)
	{
		Connection c = null;
	    Statement stmt = null;
	
	    try
	    {
	        Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:excelFiles.db");
	    	c.setAutoCommit(false);
	    	
	    	stmt = c.createStatement();
	    	
	    	String sql = "CREATE TABLE IF NOT EXISTS ExcelFiles " +
	    				 "(ToolName TEXT    NOT NULL, " + 
	    				 " File     TEXT    NOT NULL, " +
	    				 " Extension TEXT   NOT NULL, " +
	    				 " Uploaded INT NOT NULL)"; 
	    	stmt.executeUpdate(sql);
	      
	    	PreparedStatement prep = c.prepareStatement(
	              "insert into ExcelFiles values (?, ?, ?, ?);");
	    	prep.setString(1, toolName);
	    	prep.setString(2, encodedFile);
	    	prep.setString(3, extension);
	    	prep.setInt(4, 0);
	    	prep.addBatch();
	    	
	    	stmt.close();
	    	prep.executeBatch();
            
	    	c.commit();
	        c.close();
	    } catch ( Exception e ) {
	    	logger.error( e.getClass().getName() + ": " + e.getMessage() );
	    }
	}
	
	public static String[] fileForTool(String toolName)
	{
		
		Connection c = null;
	    String file[]=new String[2];
	    int uploaded = 0;
	    
	    try
	    {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:excelFiles.db");
	    	c.setAutoCommit(false);
	    	
	    	PreparedStatement prep = c.prepareStatement(
	    			"SELECT  File, Uploaded, Extension FROM ExcelFiles where ToolName = ?;");
	    	prep.setString(1, toolName);
	    	
	    	ResultSet rs = prep.executeQuery();
	    	while ( rs.next() )
	    	{
	    		file[0] = rs.getString("File");
	    		file[1] = rs.getString("Extension");
	    		uploaded = rs.getInt("Uploaded");
	    	}
			if(uploaded==1)
				file=null;
			else
			{
				prep = c.prepareStatement("UPDATE ExcelFiles set Uploaded = 1 where ToolName=?;");
				prep.setString(1, toolName);
				prep.executeUpdate();
			    c.commit();
				
			}
	    	c.close();
	    } catch ( Exception e ) {
	    	logger.error( e.getClass().getName() + ": " + e.getMessage() );
	    }

	    return file;
	}
	public static String fetchSharedFiles(String toolName, String ownerName)
	{
		Connection c = null;
	    String filePath=null;
	    
	    try
	    {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:excelFiles.db");
	    	c.setAutoCommit(false);
	    	
	    	PreparedStatement prep = c.prepareStatement(
	    			"SELECT  FilePath FROM SharedFiles WHERE ToolName = ? AND Owner = ?;");
	    	prep.setString(1, toolName);
	    	prep.setString(2, ownerName);
	    	
	    	ResultSet rs = prep.executeQuery();
	    	while ( rs.next() )
	    	{
	    		filePath = rs.getString("FilePath");
	    	}
	    	c.close();
	    } catch ( Exception e ) {
	    	//logger.error( e.getClass().getName() + ": " + e.getMessage() );
	    }
	    return filePath;
	}
	public static void addSharedFile(String toolName, String ownerName, String filePath)
	{
		Connection c = null;
	    Statement stmt = null;
	
	    try
	    {
	        Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:excelFiles.db");
	    	c.setAutoCommit(false);
	    	
	    	stmt = c.createStatement();
	    	
	    	String sql = "CREATE TABLE IF NOT EXISTS SharedFiles " +
	    				 "(ToolName TEXT    NOT NULL, " + 
	    				 " Owner     TEXT    NOT NULL, " +
	    				 " FilePath TEXT NOT NULL)"; 
	    	stmt.executeUpdate(sql);
	      
	    	PreparedStatement prep = c.prepareStatement(
	              "insert into SharedFiles values (?, ?, ?);");
	    	prep.setString(1, toolName);
	    	prep.setString(2, ownerName);
	    	prep.setString(3, filePath);
	    	prep.addBatch();
	    	
	    	stmt.close();
	    	prep.executeBatch();
            
	    	c.commit();
	        c.close();
	    } catch ( Exception e ) {
	    	logger.error( e.getClass().getName() + ": " + e.getMessage() );
	    }
	}
}
