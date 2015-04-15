package excel;
import java.sql.*;

import org.apache.log4j.Logger;

/* Class to perform all database operations to the excelfiles sqlite DB */

public class DatabaseUtil {
	
	private static final Logger logger = Logger.getLogger(DatabaseUtil.class);
	
	public static void storeFile(String toolName, String encodedFile)
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
	    				 " File     TEXT    NOT NULL)"; 
	    	stmt.executeUpdate(sql);
	      
	    	PreparedStatement prep = c.prepareStatement(
	              "insert into ExcelFiles values (?, ?);");
	    	prep.setString(1, toolName);
	    	prep.setString(2, encodedFile);
	    	prep.addBatch();
	    	
	    	stmt.close();
	    	prep.executeBatch();
            
	    	c.commit();
	        c.close();
	    } catch ( Exception e ) {
	    	logger.error( e.getClass().getName() + ": " + e.getMessage() );
	    }
	}
	
	public static String fileForTool(String toolName)
	{
		
		Connection c = null;
	    String file=null;
	    
	    try
	    {
	    	Class.forName("org.sqlite.JDBC");
	    	c = DriverManager.getConnection("jdbc:sqlite:excelFiles.db");
	    	c.setAutoCommit(false);
	    	
	    	PreparedStatement prep = c.prepareStatement(
	    			"SELECT  File FROM ExcelFiles where ToolName = ?;");
	    	prep.setString(1, toolName);
	    	
	    	ResultSet rs = prep.executeQuery();
	    	while ( rs.next() )
	    	{
	    		file = rs.getString("File");
	    	}
	    	c.close();
	    } catch ( Exception e ) {
	    	logger.error( e.getClass().getName() + ": " + e.getMessage() );
	    }
		
	    return file;
	}
}
