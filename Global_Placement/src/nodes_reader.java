import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class nodes_reader {
	  public void read(String fileName) {
		    String line = null; // This will reference one line at a time
		    int count = 0;
		    double max1 = 0.0;
		    double max2 = 0.0;
		    try {
		        // FileReader reads text files in the default encoding.
		        FileReader fileReader = new FileReader(fileName);
		        // Always wrap FileReader in BufferedReader.
		        BufferedReader bufferedReader = new BufferedReader(fileReader);

		        while((line = bufferedReader.readLine()) != null) {
		        		String[] strs = line.split("\\s+");
		        		//System.out.println(count+"      "+strs.length);
		            if(count==0) {
		            		Execution.total_nodes = Integer.parseInt(strs[2]);
		            		count++;
		            }
		            else if(count==1) {
		            	  	count++;
		            		continue;
		            }	
		            else {
		            		if(strs.length==5) continue;
		               	double width =  Double.parseDouble(strs[2]);
		               	double height =  Double.parseDouble(strs[3]);
		                Execution.avg_block_width +=width;
		                Execution.avg_block_height +=height;
		            		block cur = new block(strs[1].trim(),width,height);
		            		Execution.blocks.put(strs[1].trim(),cur);
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
