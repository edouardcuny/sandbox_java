import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class fiftyFive {
    public static void main(String[] args) {
        // int[] text = {36, 22, 80, 0, 0, 4, 23, 25, 19, 17, 88};
        String filePath = "/Users/edouardcuny/Downloads/p059_cipher.txt";
        int[] text = readFile(filePath);
        int[] decodedText = new int[text.length];

        int count = 0;
        for (int a = 97; a <= 122; a++) {
            for (int b = 97; b <= 122; b++) {
                for (int c = 97; c <= 122; c++) {
                    StringBuilder sb = new StringBuilder();
                    List<Integer> convertedASCII = new ArrayList<>();
                    boolean validAnswer = true;
                    for (int i = 0; i < text.length; i++) {
                        if (i == 10) { // au bout de 5 lettres je fais un test pour voir si ça a du sens
                            for (int x : convertedASCII) {
                                if (x != 32 && (x < 65 || (x >= 91 && x <= 96) || x > 123)) {
                                    validAnswer = false;
                                    break;
                                }
                            }
                        }

                        int key = 0;
                        if (i % 3 == 0) {
                            key = a;
                        }
                        if (i % 3 == 1) {
                            key = b;
                        }
                        if (i % 3 == 2) {
                            key = c;
                        }
                        int decoded = key ^ text[i];

                        if (i == 0 && (decoded < 65 || decoded > 90)) {
                            validAnswer = false;
                            break;
                        }

                        if (decoded > 123) {
                            // System.out.println("No good");
                            validAnswer = false;
                            break;
                        }
                        sb.append(Character.toString((char) decoded));
                        decodedText[i] = decoded;
                        convertedASCII.add(decoded);

                    }
                    if (validAnswer) {
                        count += 1;
                        int sum = 0;
                        for (int i:decodedText){
                            sum+=i;
                        }
                        System.out.println(sb.toString());
                        System.out.println("Somme décodée : "+sum);
                        System.out.println();

                    }
                }
            }
        }
        System.out.println(count);
    }

    public static int[] readFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            String[] lettersAsStrings = line.split(",");
            int[] lettersAsInts = new int[lettersAsStrings.length];
            for (int i = 0; i < lettersAsStrings.length; i++) {
                lettersAsInts[i] = Integer.parseInt(lettersAsStrings[i]);
            }
            return lettersAsInts;
        } catch (Exception e) {
            System.out.println("Couldn't read file for some reason");
        }
        return null;
    }

}
