import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class nets_reader {
	public void read(String fileName) {
	    String line = null; // This will reference one line at a time
	    int count = 0;
	    try {
	        // FileReader reads text files in the default encoding.
	        FileReader fileReader = new FileReader(fileName);
	        // Always wrap FileReader in BufferedReader.
	        BufferedReader bufferedReader = new BufferedReader(fileReader);
	        List<net> list = new ArrayList();
	        boolean flag = false;		//flag for detecting terminals 
	        while((line = bufferedReader.readLine()) != null) {
	        		String[] strs = line.split("\\s+");
	        		//System.out.println(count+"      "+strs.length);
	            if(count==0) {
	            		Execution.total_nets = Integer.parseInt(strs[2]);
	            		count++;
	            }
	            else if(count==1) {
	            	  	count++;
	            	  	Execution.total_pins = Integer.parseInt(strs[2]);
	            }	
	            else {
//	             	System.out.println(count);
	            		if(strs[0].compareTo("NetDegree")==0) {
	            			if(list.size()!=0) {
	            				Execution.nets.add(new ArrayList(list));
	            				if(flag) Execution.nets_terminal.add(new ArrayList(list));
	            				else Execution.nets_noterminal.add(new ArrayList(list));
	            				flag = false;  //reset to nonterminal
	            				list.clear();
	            			}
	            			count++;
	            		}
	            		else {
	            			String id = strs[1];
	            			if(id.charAt(0)=='p') {  // means it is terminal 
	            				terminal t = Execution.terminals.get(id);
	            				net cur = new net(id,t.x,t.y,false);
	            				list.add(cur);
	            				flag = true;
	            			}
	            			else {
	            				block b = Execution.blocks.get(id);
	            				net cur = new net(id,b.width,b.height,true);
	            				list.add(cur);
	            			}
	            			count++;
	            		}
	            }
	        }   
	        Execution.nets.add(new ArrayList(list));
	        if(flag) Execution.nets_terminal.add(new ArrayList(list));
			else Execution.nets_noterminal.add(new ArrayList(list));
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


