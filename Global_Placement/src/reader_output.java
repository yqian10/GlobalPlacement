import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class reader_output {
	public static void read(String fileName) {
		 String line = null; // This will reference one line at a time
		    int count = 0;
		    try {
		        // FileReader reads text files in the default encoding.
		        FileReader fileReader = new FileReader(fileName);
		        // Always wrap FileReader in BufferedReader.
		        BufferedReader bufferedReader = new BufferedReader(fileReader);
		        while((line = bufferedReader.readLine()) != null) {
		        		String[] strs = line.split("\\s+");
		        		//System.out.println(count+"      "+strs.length);
		            if(count==0) {
		            		count++;
		            		continue;
		            }
		            else if(count==1) {
		            		count++;
		            	  	continue;
		            }	
		            else {
	             		String id = strs[0];
	             		double x = Double.parseDouble(strs[1]);
	             		double y = Double.parseDouble(strs[2]);
//	             		if(id.charAt(0)!='p') Execution.best_res.put(id, x+" "+y);
	             		if(id.charAt(0)!='p') {
	             			net n = Execution.res.get(id);
	             			n.x = x;
	             			n.y = y;
	             		}
		            	     count++;
		            	}
		       
		            }
	   
		        // Always close files.
		        bufferedReader.close();         
		    }
		    catch(FileNotFoundException ex) {
		        System.out.println(
		            "Unable to open file '" + 
		            fileName + "'");                
		    }
		    catch(IOException ex) {
		        System.out.println(
		            "Error reading file '" 
		            + fileName + "'");                  
		        // Or we could just do this: 
		        // ex.printStackTrace();
		    }
	}
}
