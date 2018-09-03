
import java.io.FileWriter;
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
 
/**
 * This program demonstrates how to write characters to a text file.
 * @author www.codejava.net
 *
 */
public class writer{
 
    public static void write(String outpath) {
        try {
        		Files.deleteIfExists(Paths.get(outpath));
            FileWriter writer = new FileWriter(outpath, true);
            writer.write("NumNodes : "+Execution.total_nodes);
            writer.write("\r\n");   // write new line
            writer.write("NumTerminals : "+Execution.total_terminals);
            for(String s:Execution.res.keySet()) {
                		writer.write("\r\n");   // write new line
                		net n = Execution.res.get(s);
                		writer.write(n.id+"   "+n.x+"   "+n.y);
      		 }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
}