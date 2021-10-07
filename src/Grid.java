import java.util.Arrays;

public class Grid {

    final private Cell[][] cells;
    final public Row[] rows = new Row[9];
    final public Column[] columns = new Column[9];
    final public Square[] squares = new Square[9];
    private boolean isSolved = false;
    private int cellsSolved = 0;
    private boolean gridBroken = false;

    Grid(String stringGrid){
        // constructor
        // CELLS
        cells = new Cell[9][9];
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

    public Grid cloneGrid(){
        String stringGrid = this.outputStringGrid();
        return new Grid(stringGrid);
    }

    public Grid treeSolveGrid(){
        solveGrid(10);
        if (isSolved){
            return(this);
        }
        if (gridBroken){
            return(this);
        }
        else{
            for (int i=0; i<9; i++){
                for (int j=0; j<9; j++){
                    Cell cell = cells[i][j];
                    if (!cell.isSolved){
                        for (int x=0; x<9; x++){
                            if (cell.possibilities[x]){
                                System.out.println("Testing value "+x+"for coordinates"+i+j);
                                Grid clone = this.cloneGrid();
                                clone.cells[i][j].setValue(x+1);
                                Grid solvedClone = clone.treeSolveGrid();
                                if (solvedClone.isSolved){
                                    return clone.treeSolveGrid();
                                }
                            }
                        }
                    }
                }
            }
        }
        return null; // if all else fails
    }

    public void udpateCellsSolvedGridBroken(){
        for (int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                if (cells[i][j].isSolved){
                    cellsSolved += 1;
                }
                if (cells[i][j].isBroken){
                    gridBroken = true;
                }
            }
        }
    }

    public void solveGrid(int iterations){

        for (int i=0; i<iterations; i++){

            udpateCellsSolvedGridBroken();

            if (cellsSolved == 81){
                System.out.println(">> GRID SOLVED!");
                printGrid();
                isSolved = true;
                break;
            }

            System.out.println("Itération " + i);
            System.out.println("Solved " + cellsSolved);
            System.out.println();

            for (Row row: rows){
                row.solveByCell();
                row.solveByGroup();
            }
            for (Column column: columns){
                column.solveByCell();
                column.solveByGroup();
            }
            for (Square square: squares){
                square.solveByCell();
                square.solveByGroup();
            }
        }

        if (!isSolved){
            System.out.println("Couldn't find a solution :");
            printGrid();
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

    public String outputStringGrid(){
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                sb.append(String.valueOf(cells[i][j].getValue()));
            }
        }
        return sb.toString();
    }

    abstract class Group {
        public String name;
        private int number;
        public int[][] coordinates = new int[9][2];
        final public boolean[] values = new boolean[9];
        public int[] possibilities = {0,0,0,0,0,0,0,0,0};

        public void solveByCell(){
            updateValues();
            updateCellPossibilities();
            updateCellValue();
        }

        private void updateValues(){
            for (int i=0; i<9; i++){
                if (cells[coordinates[i][0]][coordinates[i][1]].isSolved){
                    values[cells[coordinates[i][0]][coordinates[i][1]].getValue()-1] = true;
                }
            }
        }

        private void updateCellPossibilities(){
            /*
            ici je mets à jour les valeurs des possibilites dans les cells en fonction de ce que
            j'ai dans le groupe
             */
            for (int i=0; i<9; i++){
                cells[coordinates[i][0]][coordinates[i][1]].updatePossibilities(values);
            }
        }

        private void updateCellValue(){
            for (int i=0; i<9; i++){
                cells[coordinates[i][0]][coordinates[i][1]].updateCell();
            }
        }

        public void solveByGroup(){
            updateValues();
            updateCellPossibilities();
            updateGroupPossibilities();
            setLonePossibilityValue();
        }

        private void updateGroupPossibilities(){
            for (int x=0; x<9; x++){
                this.possibilities[x] = 0;
            }
            for (int i=0; i<9; i++){
                Cell cell = cells[coordinates[i][0]][coordinates[i][1]];
                if(!cell.isSolved){
                    for (int j=0; j<9; j++){
                        if (cell.possibilities[j]){
                            this.possibilities[j]+=1;
                        }
                    }
                }
            }
        }

        private void setLonePossibilityValue(){
            for (int i=0; i<9; i++){
                if (possibilities[i]==1){
                    for (int j=0; j<9; j++){
                        Cell cell = cells[coordinates[j][0]][coordinates[j][1]];
                        if (cell.possibilities[i]){
                            cell.setValue(i+1);
                        }
                    }
                }
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
        String stringGrid = Utils.getStringGrid(filePath);
        Grid grid = new Grid(stringGrid);
        grid.treeSolveGrid();
    }
}