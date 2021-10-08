import java.util.Arrays;
import java.util.Objects;

public class Grid {

    final private Cell[][] cells;
    final public Row[] rows = new Row[9];
    final public Column[] columns = new Column[9];
    final public Square[] squares = new Square[9];
    private boolean isSolved = false;
    private int cellsSolved = 0;
    private boolean gridBroken = false;

    Grid(String stringGrid){
        /*
        Constructeur d'une Grid.
        Prend en input un String : tous les nombres de la grid en sens de lecture "normal".
         */
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
        /*
        Clones "this" into a new Grid.
        NB : doesn't clone all the attributes - so we might need to update Possibilites, this kind of thing afterwards.
         */
        String stringGrid = this.outputStringGrid();
        return new Grid(stringGrid);
    }

    public Grid treeSolveGrid(){
        /*
        - fais une résolution naïve de ma grille en 10 itérations
        - met à jour les possibilities dans mes cells
        - si c'est résolu je m'arrête
        - si c'est broken : je m'arrête
        - sinon je prends ma première cell non résolue et j'appelle de façon récursive cette méthode sur la grille
        clonée à partir de cette grid en essayant la première possibilité
         */
        // udpateGridIntegrity();

        solveGrid(10);

        udpateCellsSolvedGridBroken();
        updateGridPossibilites();

        if (isSolved){
            return(this);
        }
        else if (gridBroken){
            return(this);
        }

        else{
            for (int i=0; i<9; i++){
                for (int j=0; j<9; j++){
                    Cell cell = cells[i][j];
                    if (!cell.isSolved){
                        for (int x=0; x<9; x++){
                            if (cell.possibilities[x]){
                                // System.out.println("Testing value "+x+" for coordinates "+i+j);
                                Grid clone = this.cloneGrid();
                                clone.cells[i][j].setValue(x+1);
                                clone.updateGridPossibilites();
                                Grid solvedClone = clone.treeSolveGrid();
                                if( Objects.isNull(solvedClone)){
                                    continue;
                                }
                                solvedClone.updateGridIntegrity();
                                if (solvedClone.isSolved && !solvedClone.gridBroken){
                                    return solvedClone;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (cellsSolved==81){
            return this;
        }
        return null; // if all else fails
    }

    public void udpateCellsSolvedGridBroken(){
        /*
        - je mets à jour le paramètre cellsSolved de ma grille
        - je regarde si une cell est broken - si oui, ma grille est broken
         */
        cellsSolved = 0;
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

    private void updateGridPossibilites(){
        /*
        j'itère dans mes groupes puis dans mes cells pour màj les possibilities
         */
        for (Row row: rows){
            row.updateValues();
            row.updateCellPossibilities();
        }
        for (Column column: columns){
            column.updateValues();
            column.updateCellPossibilities();
        }
        for (Square square: squares){
            square.updateValues();
            square.updateCellPossibilities();
        }
    }

    public void solveGrid(int iterations){
        /*
        - je compte mes cells résolues : si tout bon je ne fais rien
        - sinon, pour au max iterations fois : je fais ma résolution naïve, je compte ce que j'ai résolu, si c'est bon
        je sors, sinon je continue
         */

        udpateCellsSolvedGridBroken();
        if (cellsSolved == 81 && !gridBroken){
            isSolved = true;
        }
        else if (!gridBroken){
            int counter = 0;
            while(!isSolved && counter<iterations){
                counter++;
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
                udpateCellsSolvedGridBroken();
                if (cellsSolved == 81 && !gridBroken){
                    isSolved = true;
                }
            }
        }
    }

    public void printGrid(){
        /*
        une façon lisible d'outputer ma grid dans la console
         */
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
        /*
        output ma grid en String au format lisible par mon constructeur de Grid
         */
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                sb.append(String.valueOf(cells[i][j].getValue()));
            }
        }
        return sb.toString();
    }

    public int getTopRowNumber(){
        /*
        renvoie le nombre constitué des 3 premiers chiffres de ma grid
         */
        return cells[0][0].getValue()*100 +
                cells[0][1].getValue()*10 +
                cells[0][2].getValue();
    }

    public boolean isMistakeInGrid(){
        /*
        >> returns true si ma grid est incorrectement résolue
        - si elle n'est pas résolue : returns true
        - si elle l'est : on vérifie qu'il ne manque aucun numéro dans un groupe
         */
        if (!isSolved){
            return true;
        }
        else{
            for (Row row: rows){
                if (row.isMultipleNumberGroup()){
                    return true;
                }
            }
            for (Column column: columns){
                if(column.isMultipleNumberGroup()){
                    return true;
                }
            }
            for (Square square: squares){
                if(square.isMultipleNumberGroup()){
                    return true;
                }
            }

        }
        return false;}

    public void updateGridIntegrity(){
        /*
        nouvelle façon d'updater isBroken : si j'ai deux fois la même valeur
         */
        for (Row row: rows){
            if (row.isMultipleNumberGroup()){
                gridBroken = true;
            }
        }
        for (Column column: columns){
            if(column.isMultipleNumberGroup()){
                gridBroken = true;
            }
        }
        for (Square square: squares){
            if(square.isMultipleNumberGroup()){
                gridBroken = true;
            }
        }
    }

    abstract class Group {
        public String name;
        private int number;
        public int[][] coordinates = new int[9][2];
        final public boolean[] values = new boolean[9];
        public int[] possibilities = {0,0,0,0,0,0,0,0,0};

        public boolean isMultipleNumberGroup(){
            /*
            itère dans les cells d'un groupe pour vérifier qu'aucun nombre n'apparaît deux fois
             */
            boolean[] check = new boolean[9];
            Arrays.fill(check, Boolean.FALSE);
            for (int i=0; i<9; i++){
                if (cells[coordinates[i][0]][coordinates[i][1]].isSolved){
                    int value = cells[coordinates[i][0]][coordinates[i][1]].getValue();
                    check[value-1] = true;
                }
            }
            for (boolean b: check){
                if (!b){
                    return true;
                }
            }
            return false;
        }

        public void solveByCell(){
            updateValues();
            updateCellPossibilities();
            updateCellValue();
        }

        public void updateValues(){
            /*
            met à jour le vecteur values du groupe en mettant à true les cases correspondant aux nombres déjà présents
            dans le groupe
             */
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

        private void updateCellValue(){
            /*
            je fais un updateCell dans les cellules d'un groupe
             */
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
            /*
            je mets à jour le vecteur possibilites du groupe qui compte combien d'emplacements possibles il existe pour
            un nombre
             */
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
            /*
            j'itère dans le vecteur de possibilites et s'il n'y a qu'un emplacement possible pour un nombre j'inscris
            ce nombre dans la cell correspondante
             */
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
            /*
            façon très verbose d'imprimer des infos sur un groupe - imprime tous les détails des cells, utile
            pour débugger (maybe)
             */
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