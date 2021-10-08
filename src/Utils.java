import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

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
            // System.out.println(stringGrid);
            return sb.toString();
        }
        catch (Exception e){
            System.out.println("Couldn't read file for some reason");
            return "";
        }
    }
    static List<String> getStringGridArray(String filePath){

        List<String> grids = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                if (line.contains("Grid")) {
                    line = br.readLine();
                    // on passe ici seulement à la première lecture
                    if (!sb.toString().isEmpty()) {
                        grids.add(sb.toString());
                        sb.setLength(0);
                    }
                }
                sb.append(line);
                line = br.readLine();
            }
            grids.add(sb.toString());
            return grids;
        }
        catch (Exception e){
            System.out.println("Couldn't read file for some reason");
            return grids;
        }
    }
}
