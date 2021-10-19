import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class fiftySix {

    public static ArrayList<Integer> getPrimes(int upperbound) {
        boolean[] flagPrimeArray = new boolean[upperbound];
        Arrays.fill(flagPrimeArray, Boolean.TRUE);
        flagPrimeArray[0] = false;
        flagPrimeArray[1] = false;
        flagPrimeArray[2] = false;

        for (int i = 2; i <= Math.sqrt(upperbound); i++) {
            int k = i;
            while (k * i < upperbound) {
                flagPrimeArray[k * i] = false;
                k++;
            }
        }
        ArrayList<Integer> primes = new ArrayList<Integer>();
        for (int i = 0; i < flagPrimeArray.length; i++) {
            if (flagPrimeArray[i]) {
                primes.add(i);
            }
        }
        return primes;
    }

    public static boolean[] getSieve(int upperbound) {
        boolean[] sieve = new boolean[upperbound];
        Arrays.fill(sieve, Boolean.TRUE);
        sieve[0] = false;
        sieve[1] = false;
        sieve[2] = false;

        for (int i = 2; i <= Math.sqrt(upperbound); i++) {
            int k = i;
            while (k * i < upperbound) {
                sieve[k * i] = false;
                k++;
            }
        }

        return sieve;
    }

    public static ArrayList<Integer> addFriend(ArrayList<Integer> friends,
                                               boolean[] sieve,
                                               ArrayList<Integer> primes,
                                               int maxSum,
                                               int primeBound) {
        /*
        INPUT
        friends : une liste de primes intervertibles
        sieve : un crible avec true sur les cases de primes
        primes : une liste de primes

        OUTPUT
        friends avec un nouvel ami si on en a trouvé un d'intervertible
        null sinon
         */

        int maxFriends = Collections.max(friends);
        int sum = 0;
        for (int friend : friends) {
            sum += friend;
        }
        if (sum > maxSum) {
            return new ArrayList<>();
        }
        for (int candidate : primes) { //TODO nettoyer cette boucle "FOR" pour réduire le nombre de candidats
            if (candidate > maxFriends && candidate < primeBound) {
                boolean isValidCandidate = true;
                for (int friend : friends) {
                    String candidateFriendString = String.valueOf(candidate) + String.valueOf(friend);
                    try{
                        int candiFriend = Integer.parseInt(candidateFriendString);
                        if (candiFriend > maxSum - sum) {
                            isValidCandidate = false;
                            break;
                        }
                        if (!sieve[candiFriend]) {
                            isValidCandidate = false;
                            break;
                        }
                        String friendCandidateString = String.valueOf(friend) + String.valueOf(candidate);
                        int friendCandi = Integer.parseInt(friendCandidateString);
                        if (friendCandi > maxSum - sum) {
                            isValidCandidate = false;
                            break;
                        }
                        if (!sieve[friendCandi]) {
                            isValidCandidate = false;
                            break;
                        }
                    }
                    catch (Exception NumberFormatException){
                        isValidCandidate = false;
                        break;
                    }

                }
                if (isValidCandidate) {
                    friends.add(candidate);
                    return friends;
                }
            }
        }
        return new ArrayList<>();
    }

    public static void solveProblem(int depth, int primesUpTo, int sieveSize, int primeBound) {
        ArrayList<Integer> primes = getPrimes(primesUpTo);
        boolean[] sieve = getSieve(sieveSize);
        int minSolution = sieveSize;

        for (int prime : primes) {
            ArrayList<Integer> friends = new ArrayList<Integer>();
            friends.add(prime);
            boolean noSolution = false;
            boolean foundSolution = false;
            while (!noSolution && !foundSolution) {
                friends = addFriend(friends, sieve, primes, minSolution, primeBound);
                if (friends.size() == 0) {
                    noSolution = true;
                    // System.out.println("No solution with :"+prime);
                }
                if (friends.size() == depth) {
                    foundSolution = true;
                }
            }
            if (foundSolution) {
                int sum = 0;
                for (int friend : friends) {
                    sum += friend;
                }
                if (sum < minSolution) {
                    minSolution = sum;
                    System.out.println(friends.toString());
                    System.out.println(minSolution);
                    System.out.println();
                }

            }

        }
        System.out.println("Final solution "+minSolution);
    }


    public static void main(String[] args) {
        solveProblem(2, 10000000, 100000000, 1000000);
    }}


// PISTES : le temps de calcul du sieve doit me permettre d'éliminer certaines pistes
// en particulier un sieve de 1 yard ça me paraît gros
// une arme qu'on a c'est qu'il faut que tous les friends aient la même congruance modulo 3
// un truc qu'on va devoir faire c'est rationaliser la loop pour éviter de passer trop de temps
// il faudra aussi probablement passer en long ? ou pas ? je ne pense pas parce qu'on le tape à 2 yards
// on peut réfléchir à d'autres armes