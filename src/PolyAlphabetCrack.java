import java.util.*;

public class PolyAlphabetCrack {


    private static final int MAX_DIVISOR = 26;
    private NGramsLoader loaderFrequency;
    private String ciphertext;
    private String cipherTextWithoutSpaces;
    private HashMap<Integer, Integer> divisors = new HashMap<>(26); // pocet znaku v abecede
    private int keyLength;
    private int[][] testShifts;
    private int[] shifts;


    /**
     * Konstruktor tridy - inicializuje vsechny parametry (bud primo zde, popripade v nasledujicich metodach)
     * @param cipherText text sifry
     */
    public PolyAlphabetCrack(String cipherText){
        this.ciphertext = cipherText;

        System.out.println(cipherText);

        findSequences();

        freqLettersAnalysis();

        decrypt();

    }

    private void freqLettersAnalysis(){
        shifts = new int[keyLength]; // pole pro ziskani posunu kazde abecedy, vzdy bude stejne dlouhe jako klic
        String test;
        testShifts = new int[keyLength][2];
        for (int i = 0; i < shifts.length; i++) {            // pro kazdou abecedu
            int[] frequency = new int[26]; // na kazde pozici je pismeno dle abecedy
            Arrays.fill(frequency, 0);
            char c;
            for (int j = 0; j < cipherTextWithoutSpaces.length(); j += keyLength) {
                c = cipherTextWithoutSpaces.charAt(j + i);
                if (c < NGramsLoader.ASCII_VALUE_A || c > NGramsLoader.ASCII_VALUE_Z ) { //mezera
                    continue;
                }
                else {
                    frequency[c - NGramsLoader.ASCII_VALUE_A]++;
                }

            }
//            System.out.println(Arrays.toString(frequency));

            int[] mostFrequentLetters = getMostFrequentLetters(frequency);
            testShifts[i][0] = mostFrequentLetters[0];
            testShifts[i][1] = mostFrequentLetters[1];
        }

    }


    /**
     * Najde dve nejpouzivaenjsi pismena
     * @param frequency - cetnosti znaku
     * @return pole dvou nejcetenjsich znaku
     */
    private int[] getMostFrequentLetters(int[] frequency) {
        int maxValue = 0;
        int maxValueIdx = 0; //index prvku na maximalni hodnote
        int currValue = 0;
        int secMaxValueIdx = 0;
        int secMaxValue = 0;

        //hledani nejcetnejsiho znaku
        for (int i = 0; i < frequency.length; i++) {
            if (frequency[i] > maxValue) {
                secMaxValueIdx = maxValueIdx;
                secMaxValue = maxValue;
                currValue = maxValue;
                maxValue = frequency[i];
                maxValueIdx = i;


            } else if (frequency[i] > currValue) {
                currValue = frequency[i];
            }
        }

        // posun vuci nejpouzivanejsimu pismenu
        int maxValueShift = maxValueIdx + NGramsLoader.ASCII_VALUE_A - NGramsLoader.SORTED_LETTERS_BY_FREQUENCY.charAt(0);
        int secMaxValueShift = secMaxValue + NGramsLoader.ASCII_VALUE_A - NGramsLoader.SORTED_LETTERS_BY_FREQUENCY.charAt(0);
        if (maxValueShift < 1) {
            maxValueShift = 26 + maxValueShift;
        }
        if (secMaxValueShift < 1) {
            secMaxValueShift = 26 + secMaxValueShift;
        }
        System.out.println(maxValueShift);

        return new int[]{maxValueShift, secMaxValueShift};
    }

    /**
     * Nalezeni vsech sekvenci, ktere se alespon jednou opakuji, ignoruje mezery
     */
    private void findSequences() {
        HashMap<String, Integer> map = new HashMap<>();
        cipherTextWithoutSpaces = ciphertext.replaceAll(" ", "");
        cipherTextWithoutSpaces = cipherTextWithoutSpaces.replaceAll("\n", "");

        ArrayList<Integer> sequenceDist = new ArrayList<Integer>();

        for (int j = 4; j <= 25; j++) {               // maximalni delky sekvenci
            for (int i = 0; i < cipherTextWithoutSpaces.length(); i++) {
                if (i + j >= cipherTextWithoutSpaces.length()) break;
                String substring = cipherTextWithoutSpaces.substring(i, i + j);
                if (map.containsKey(substring)) {
                    map.put(substring, map.get(substring) + 1);
                } else {
                    map.put(substring, 1);
                }
            }
        }
        Iterator it = map.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry item = (Map.Entry) it.next();
            String key = (String) item.getKey();
            int value = (int) item.getValue();

            if (value == 2) {
                int index1 = cipherTextWithoutSpaces.indexOf(key);
                String tmpSub = cipherTextWithoutSpaces.substring(index1 + key.length(), cipherTextWithoutSpaces.length());
                int index2 = tmpSub.indexOf(key) + key.length();
                sequenceDist.add(index2);
            }
        }
        findDivisors(sequenceDist);
        findMostFrequentDivisor();
    }

    /**
     * Metoda porovna vsechny delitele vzdalenosti a ulozi nejcetenjsiho spolecneho delitele.
     */
    private void findMostFrequentDivisor() {
        Iterator it = divisors.entrySet().iterator();
        int maxDivisor = 2;
        while (it.hasNext()) {
            Map.Entry item = (Map.Entry) it.next();
            int value = (int)item.getValue();
            if (value > divisors.get(maxDivisor)) {
                maxDivisor = (int)item.getKey();
            }
//            System.out.println( item.getKey()+ "\t==\t" + item.getValue());
        }
//        System.out.println(maxDivisor);
        keyLength = maxDivisor;
    }

    /**
     * Najde delitele vzdalenosti sekvence
     * @param sequenceDist vzdalenost mezi dvemi stejnymi sekvencemi
     */
    private void findDivisors(ArrayList<Integer> sequenceDist) {

        sequenceDist.forEach(dist -> {
            for (int i  = 2; i <= MAX_DIVISOR; i++) {
                if (dist % i == 0) {
                    if (divisors.containsKey(i))
                         divisors.put(i, divisors.get(i) + 1);
                    else divisors.put(i, 1);
                }

            }
        });
    }


    /**
     * Metoda rozsifruje kod
     * @return prolomena sifra
     */
    private String decrypt() {
        String encrypt = cipherTextWithoutSpaces;
        System.out.println((char) (encrypt.charAt(0) - testShifts[0][0]));

        for (int j = 0; j < keyLength; j++) {
            for (int i = j; i < encrypt.length(); i += 5) {
                char replace = (char) (encrypt.charAt(i) - testShifts[j][0]);
                if (replace > NGramsLoader.ASCII_VALUE_Z) {
                    replace = (char) (replace - 97 + 123);
                }
                encrypt = replaceChar(encrypt, replace, i);

            }
        }

        System.out.println(encrypt);

        return encrypt;
    }

    public String replaceChar(String str, char ch, int index) {
        return str.substring(0, index) + ch + str.substring(index+1);
    }
}
