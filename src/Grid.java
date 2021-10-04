import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

public class Grid {

    final private Cell[][] cells;
    final public Row[] rows = new Row[9];
    final public Column[] columns = new Column[9];
    final public Square[] squares = new Square[9];

    Grid(String filePath){
        // constructor
        // CELLS
        cells = new Cell[9][9];
        String stringGrid = getStringGrid(filePath);
        int idx = 0;
        for (int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                int value = Character.digit(stringGrid.charAt(idx), 10);
                cells[i][j] = new Cell(i, j, value);
                idx++;
            }
        }

        // GROUPS
        for (int i=0; i<9; i++){
            rows[i] = new Row(i);
            columns[i] = new Column(i);
            squares[i] = new Square(i);
        }


    }

    public void solveGrid(int iterations){
        for (int i=0; i<iterations; i++){

            if (Cell.solvedCounter==81){
                System.out.println(">> GRID SOLVED!");
                printGrid();
                break;
            }

            System.out.println("Itération " + i);
            System.out.println("Solved " + Cell.solvedCounter);
            System.out.println();

            for (Row row: rows){
                row.updateValues();
                row.updateCellPossibilities();
                row.updateCellValue();
            }
            for (Column column: columns){
                column.updateValues();
                column.updateCellPossibilities();
                column.updateCellValue();
            }
            for (Square square: squares){
                square.updateValues();
                square.updateCellPossibilities();
                square.updateCellValue();
            }
        }

    }

    private String getStringGrid(String filePath){

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

    public void printGrid(){
        for (int i=0; i<9; i++){
            if (i%3==0 && i!=0){
                System.out.println("- - - - - - - - - - - - - - - - - - -");
            }
            for (int j=0; j<9; j++) {
                if (j%3==2){
                    System.out.print(cells[i][j].getValue() + "  | ");
                }
                else{
                    System.out.print(cells[i][j].getValue() + "   ");
                }
            }
            System.out.println();
        }
    }

    abstract class Group {
        public String name;
        private int number;
        public int[][] coordinates = new int[9][2];
        private boolean solved = false;
        final public boolean[] values = new boolean[9];

        public void updateValues(){
            for (int i=0; i<9; i++){
                if (cells[coordinates[i][0]][coordinates[i][1]].isSolved){
                    values[cells[coordinates[i][0]][coordinates[i][1]].getValue()-1] = true;
                }
            }
        }

        public void updateCellPossibilities(){
            /*
            ici je mets à jour les valeurs des possibilites dans les cells en fonction de ce que
            j'ai dans le groupe
             */
            for (int i=0; i<9; i++){
                cells[coordinates[i][0]][coordinates[i][1]].updatePossibilities(values);
            }
        }

        public void updateCellValue(){
            for (int i=0; i<9; i++){
                cells[coordinates[i][0]][coordinates[i][1]].updateCell();
            }
        }

        public void printGroupDetails(){
            System.out.println(name);
            for (int i=0; i<9; i++){
                cells[coordinates[i][0]][coordinates[i][1]].printDetails();
            }
        }
    }

    class Row extends Group{
        Row(int number){
            name = "Row " + number;
            Arrays.fill(values, Boolean.FALSE);
            for (int i=0; i<9; i++){
                this.coordinates[i] = new int[]{number, i};
            }
        }

    }

    class Column extends Group{
        Column(int number){
            name = "Column " + number;
            Arrays.fill(values, Boolean.FALSE);
            for (int i=0; i<9; i++){
                this.coordinates[i] = new int[]{i, number};
            }
        }

    }

    class Square extends Group{
        Square(int number){
            name = "Square " + number;
            Arrays.fill(values, Boolean.FALSE);
            int r = number%3;
            int q = number/3;
            int idx = 0;
            for (int i=0; i<3; i++){
                for (int j=0; j<3; j++){
                    int[] coor = {i+q*3, j+r*3};
                    coordinates[idx] = coor;
                    idx++;
                }
            }
        }

    }
}

class GridTest{
    public static void main(String[] args){
        String filePath = "/Users/edouardcuny/IdeaProjects/sandbox/grids/grid1.txt";
        Grid grid = new Grid(filePath);
        grid.solveGrid(10);
    }
}