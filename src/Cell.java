import java.util.Arrays;
import java.util.List;

public class Cell {
    final private int X;
    final private int Y;
    private int value;
    public boolean isSolved;
    public boolean[] possibilities;
    public boolean isBroken = false;

    public int getValue() {
        return value;
    }

    Cell(int x, int y, int value){
        /*
        constructeur d'une cell
        NB : si c'est un vide, sa valeur est "null"
         */
        this.X = x;
        this.Y = y;
        this.possibilities = new boolean[9];

        if (value!=0){
            this.value = value;
            isSolved = true;
            Arrays.fill(possibilities, Boolean.FALSE);
            this.possibilities[value-1] = true; // NB : on décale tout de 1 !
        }
        else{
            this.isSolved = false;
            this.possibilities = new boolean[9];
            Arrays.fill(possibilities, Boolean.TRUE);
        }

    }

    public void printDetails(){
        /*
        print verbose des infos d'une cell
         */
        System.out.println("Cell (" + X + "," + Y + ")");
        System.out.println("Solved : " + isSolved);
        System.out.println("Possibilities : " + Arrays.toString(possibilities));
        System.out.println("Value " + value);
        System.out.println();
    }

    public void updatePossibilities(boolean[] values){
        /*
        à partir d'un vecteur de valeurs qu'on trouve dans un groupe je mets à jour les possibilités des cellules
        (une cell ne peut pas prendre une valeur qu'on trouve déjà dans le groupe)
         */
        for (int i=0; i<9; i++){
            possibilities[i] = (possibilities[i] && !values[i]);
        }
    }

    public void updateCell(){
        /*
        j'itère dans les possibilité d'une cell
        - si elle n'a qu'une possiblité je mets à jour la valeur
        - si elle n'en a aucune je dis que la cell est broken
         */
        if (!isSolved){
            int numberOfTrue = 0;
            int indexOfValue = 0;
            for (int i=0; i < possibilities.length; i++){
                if (possibilities[i]){
                    numberOfTrue++;
                    indexOfValue = i;
                }
            }
            if (numberOfTrue == 1){
                isSolved = true;
                value = indexOfValue+1;
            }
            if (numberOfTrue ==0){
                isBroken = true;
            }
        }
    }

    public void setValue(int value){
        /*
        je donne à une cell une valeur imposée
         */
        this.value = value;
        isSolved = true;
        Arrays.fill(possibilities, Boolean.FALSE);
        this.possibilities[value-1] = true; // NB : on décale tout de 1 !
    }
}


class GridTest{
    public static void main(String[] args){
        String filePath = "/Users/edouardcuny/IdeaProjects/sandbox/grids/gridsSmall.txt";
        String stringGrid = Utils.getStringGrid(filePath);
        Grid grid = new Grid(stringGrid);
        System.out.println(grid.getTopRowNumber());
    }
}
