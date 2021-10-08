import java.util.List;

class Program {
    public static void main(String[] args) {
        // String filePath = "/Users/edouardcuny/IdeaProjects/sandbox/grids/grid1.txt";
        // String stringGrid = Utils.getStringGrid(filePath);
        // Grid grid = new Grid(stringGrid);
        // System.out.println(grid.getTopRowNumber());

        String filePath = "/Users/edouardcuny/IdeaProjects/sandbox/grids/grids.txt";
        // String filePath = "/Users/edouardcuny/IdeaProjects/sandbox/grids/grids.txt";

        List<String> grids = Utils.getStringGridArray(filePath);
        int toprowsum = 0;
        int counter = 0;
        for (String stringGrid : grids) {
            Grid grid = new Grid(stringGrid);
            Grid solvedGrid = grid.treeSolveGrid();
            if (solvedGrid.isMistakeInGrid()) {
                break;
            }
            solvedGrid.printGrid();
            System.out.println();
            toprowsum += solvedGrid.getTopRowNumber();
            System.out.println(solvedGrid.getTopRowNumber());
            counter += 1;
        }
        System.out.println("Answer is " + toprowsum);
        System.out.println("# of solved grids : " + counter);

    }
}

