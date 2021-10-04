import java.util.Arrays;

public class Cell {
    final private int X;
    final private int Y;
    private int value;
    public boolean isSolved;
    private boolean[] possibilities;
    public static int solvedCounter;

    public int getValue() {
        return value;
    }

    Cell(int x, int y, int value){
        this.X = x;
        this.Y = y;
        this.possibilities = new boolean[9];

        if (value!=0){
            this.value = value;
            isSolved = true;
            solvedCounter += 1;
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
        System.out.println("Cell (" + X + "," + Y + ")");
        System.out.println("Solved : " + isSolved);
        System.out.println("Possibilities : " + Arrays.toString(possibilities));
        System.out.println("Value " + value);
        System.out.println();
    }

    public void updatePossibilities(boolean[] values){
        for (int i=0; i<9; i++){
            possibilities[i] = (possibilities[i] && !values[i]);
        }
    }

    public void updateCell(){
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
                solvedCounter += 1;
                value = indexOfValue+1;
            }
        }
    }
}
