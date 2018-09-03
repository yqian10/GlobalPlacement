import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class terminal_reader {
    public void read(String fileName) {
    String line = null; // This will reference one line at a time
    int count = 0;
    try {
        // FileReader reads text files in the default encoding.
        FileReader fileReader = new FileReader(fileName);
        // Always wrap FileReader in BufferedReader.
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        while((line = bufferedReader.readLine()) != null) {
        		String[] strs = line.split("\\s+");
            if(count==0) {
            		Execution.total_terminals = Integer.parseInt(strs[2]);
            }
            else {
               	double numx =  Double.parseDouble(strs[1]);
               	double numy =  Double.parseDouble(strs[2]);
            		terminal cur = new terminal(strs[0].trim(),numx,numy);
            		Execution.terminal_left = Math.min(Execution.terminal_left,numx);
            		Execution.terminal_right = Math.max(Execution.terminal_right,numx);
            		Execution.terminal_up = Math.max(Execution.terminal_up,numy);
            		Execution.terminal_down = Math.min(Execution.terminal_down,numy);
            		Execution.terminals.put(strs[0].trim(),cur);
            }
            count++;
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
