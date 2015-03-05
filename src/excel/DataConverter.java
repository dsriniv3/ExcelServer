package excel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.formula.FormulaParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Class to process excel files into csv file 
public class DataConverter implements Serializable
{
	HashMap<String,ArrayList<File>> functionToFile;
	private static final long serialVersionUID = 1L;
	void processFiles() throws IOException
	{
		//spreadsheets: folder where training set is present
		Functions functions = new Functions();
		File processFolder = new File("spreadsheets");
		File[] files = processFolder.listFiles();
		int fileID=8181;
		for(File file:files)
		{
			fileID++;
			Map<String,Integer> functionCount = new HashMap<String,Integer>();
			try
			{
				Workbook wb  =  WorkbookFactory.create(file);
				int numberSheets = wb.getNumberOfSheets();
				for(int i = 0;i<numberSheets;i++)
				{
					Sheet sheet = wb.getSheetAt(i);
					for (Row row : sheet)
					{
						for (Cell cell : row)
						{
								if (cell.getCellType() == Cell.CELL_TYPE_FORMULA)
								{
									//Extract the function name from the formula
									Pattern p = Pattern.compile("[A-Za-z]+\\(");
									Matcher m = p.matcher(cell.getCellFormula()); // get a matcher object
									while(m.find())
									{
										String extractedFormula = m.group().substring(0,m.group().length()-1);
										if(extractedFormula.length()!=0)
											if(functions.functionMap.get(extractedFormula)!=null)
											{
												int tmp = functionCount.get(extractedFormula) == null ? 0 : functionCount.get(extractedFormula);
												functionCount.put(extractedFormula, tmp+1);
											}
									}
								}
							}
					}
			    }
				
				System.gc();
			}catch(FormulaParseException e){ //To-do: find cause of exception
	        	System.out.println("FormulaParseException occurred at file:"+ file.getName());
	        } catch(IOException e) {
				e.printStackTrace();
			}catch (InvalidFormatException e) {
				e.printStackTrace();
			}
			try
			{
				PrintWriter printer = new PrintWriter(new BufferedWriter(new FileWriter("data\\data.csv",true)));
				List<Map.Entry<String,Integer>> list  =  new LinkedList<Map.Entry<String,Integer>>(functionCount.entrySet());
				for (Map.Entry<String, Integer> entry : list)
				{
					String s = entry.getKey(); 
					printer.println(fileID + "," + functions.functionMap.get(s) + "," + functionCount.get(s));
					//this.functionToFile.get(s).add(file);
					
				}
				printer.close();
			}catch(IOException e){
		    	e.printStackTrace();
		    }
			System.out.println("Completed "+file.getName() +" "+fileID);

		}
	}
	public static void main(String args[]) throws IOException
	{
		DataConverter dataConverter = null;
        try
	    {
	         FileInputStream fileIn = new FileInputStream("data/dataconverter.ser");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         dataConverter = (DataConverter) in.readObject();
	         in.close();
	         fileIn.close();
	    }catch(IOException i){
	         System.out.println("File not found");	
	         dataConverter = new DataConverter();
	    }catch(ClassNotFoundException c){
	         System.out.println("Data Converter class not found");
	         dataConverter = new DataConverter();
	    }
		dataConverter.processFiles();
		//Serialize the object
		try
	      {
	         FileOutputStream fileOut = new FileOutputStream("data/dataconverter.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(dataConverter);
	         out.close();
	         fileOut.close();
	       }catch(IOException i){
	            i.printStackTrace();
	      }
	}
}

