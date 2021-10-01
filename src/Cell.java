import java.util.Arrays;

public class Cell {
    private int x;
    private int y;
    private int value;
    private boolean isSolved;
    private boolean[] possibilities;

    Cell(int x, int y, int value){
        this.x = x;
        this.y = y;
        this.value = value;
        this.isSolved = true;
        this.possibilities = new boolean[9];
        Arrays.fill(possibilities, Boolean.FALSE);
        this.possibilities[value] = true;
    }

    Cell(int x, int y){
        this.x = x;
        this.y = y;
        this.isSolved = false;
        this.possibilities = new boolean[9];
        Arrays.fill(possibilities, Boolean.TRUE);
    }

    public void printDetails(){
        System.out.println("Cell (" + x + "," + y + ")");
        System.out.println("Solved : " + isSolved);
        System.out.println("Possibilities : " + Arrays.toString(possibilities));
    }

    private void updateCell(){
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
                value = indexOfValue;
            }
        }
    }
}
