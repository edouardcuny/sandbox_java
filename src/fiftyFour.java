import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class fiftyFour {
    public static void main (String[] args){

        ArrayList<String[][]> matches = new ArrayList<String[][]>();

        // lecture du fichier
        String filePath = "/Users/edouardcuny/Downloads/poker.txt";
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while (line != null) {
                String[] cards = line.split(" ");
                String[] game1 = new String[5];
                String[] game2 = new String[5];
                System.arraycopy(cards, 0, game1, 0, 5);
                System.arraycopy(cards, 5, game2, 0, 5);
                String[][] match = new String[2][5];
                match[0] = game1;
                match[1] = game2;
                matches.add(match);
                line = br.readLine();
                }
            }
        catch (Exception e){
            System.out.println("Couldn't read file for some reason");
        }

        int winsTotal = 0;
        int defeats = 0;
        for (String[][] match:matches){
            String[] game1 = match[0];
            String[] game2 = match[1];
            if (player1Wins(game1,game2)){
                winsTotal+=1;
            }
            else{
                defeats+=1;
            }
        }

        System.out.println(winsTotal);
        System.out.println(defeats);
    }

    public static int getScore(String[] game){

        // isFlush
        int[] colors = new int[4];
        for (String card:game) {
            int idx = switch(card.charAt(1)){
                case 'S' -> 0;
                case 'H' -> 1;
                case 'D' -> 2;
                case 'C' -> 3;
                default -> throw new IllegalStateException("Unexpected value: " + card.charAt(1));
            };
            colors[idx] += 1;}

        boolean isFlush=false;
        for (int i: colors){
            if (i == 5) {
                isFlush = true;
                break;
            }
        }

        // isStraight
        int [] values = getValues(game);
        int consecutiveValues = 0;
        for (int count:values){
            if (consecutiveValues==5){
                break;
            }
            if (count==0){
                consecutiveValues = 0;
            }
            else{
                consecutiveValues +=1;
            }
        }
        boolean isStraight = (consecutiveValues==5);

        // break if flush or straight or both
        if (isStraight & isFlush){
            return 9;
        }
        else if(isStraight){
            return 5;
        }
        else if(isFlush){
            return 6;
        }

        // hand composition
        Arrays.sort(values);
        for(int i = 0; i < values.length / 2; i++)
        {
            int temp = values[i];
            values[i] = values[values.length - i - 1];
            values[values.length - i - 1] = temp;
        }
        List<Integer> handComposition = new ArrayList<>();
        for (int value:values){
            if (value!=0){
                handComposition.add(value);
            }
        }
        if (handComposition.get(0)==1){
            return 1; //high card
        }
        else if(handComposition.get(0)==4){
            return 8; //four of a kind
        }
        else if(handComposition.get(0)==2){
            if(handComposition.get(1)==2){
                return 3; //two pairs
            }
            else{
                return 2; //one pair
            }
        }
        else{
            if (handComposition.get(1)==1){
                return 4; //three of a kind
            }
            else{
                return 7; //full house
            }
        }
}
    public static boolean player1Wins(String[] game1, String[] game2){
        int score1 = getScore(game1);
        int score2 = getScore(game2);
        if (score1 != score2){
            return score1 > score2;
        }
        else{
            int[] values1 = getValues(game1);
            int[] values2 = getValues(game2);
            return player1WinsTie(values1, values2);
        }



    }
    public static int[] getValues(String[] game){
        int [] values = new int[13];
        for (String card:game) {
            if (Character.isDigit(card.charAt(0))){
                int cardValue = Character.getNumericValue(card.charAt(0));
                values[cardValue-2] += 1; // 2 est la carte la plus faible
            }
            else{
                int idx = switch(card.charAt(0)){
                    case 'T' -> 8;
                    case 'J' -> 9;
                    case 'Q' -> 10;
                    case 'K' -> 11;
                    case 'A' -> 12;
                    default -> throw new IllegalStateException("Unexpected value: " + card.charAt(0));};
                values[idx]+=1;
            }
        }
        return values;
    }
    public static boolean player1WinsTie(int[] values1, int[] values2){
        int[] values = Arrays.copyOf(values1, values1.length);
        Arrays.sort(values);
        for(int i = 0; i < values.length / 2; i++)
        {
            int temp = values[i];
            values[i] = values[values.length - i - 1];
            values[values.length - i - 1] = temp;
        }
        for (int value:values){
            for(int i=values1.length-1; i>=0; i--){
                if (values1[i]==value & values2[i]!=value){
                    return true;
                }
                else if (values1[i]!=value & values2[i]==value){
                    return false;
                }
            }
        }
        System.out.println("Logic error in player1Wins");
        return false;
    }

}
