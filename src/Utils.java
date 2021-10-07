import java.io.BufferedReader;
import java.io.FileReader;

public class Utils {
    static String getStringGrid(String filePath){

        /*
        Takes a file path and returns the grid as a string with no line returns.
        Si it's all one big line.
        I assume the format is good - maybe I'll look into it one day.
         */

        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                // sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String stringGrid = sb.toString();
            System.out.println(stringGrid);
            return stringGrid;
        }
        catch (Exception e){
            System.out.println("Couldn't read file for some reason");
            return "";
        }
    }

}
