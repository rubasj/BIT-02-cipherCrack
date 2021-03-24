import java.util.Arrays;

public class CaesarCracker {
    private NGramsLoader loadFrequencies;

    private String cipher;

    public String decryptedText;

    public CaesarCracker(String cipher) {
        loadFrequencies = new NGramsLoader();
        this.cipher = cipher;
        startCrack();
    }

    private void startCrack() {
        // Frequency analysis
        String freq = this.frequencyAnalysis();
        int freqOK = loadFrequencies.compareResultsCaesar(freq);
        System.out.println("Frekvencni analyza pomoci caesara - vysledek:\n" + freq.toUpperCase() + "\nShoda s anglickym slovnikem: " +  freqOK);

        String brute = this.bruteForce();
        int bruteOK = loadFrequencies.compareResultsCaesar(brute);
        System.out.println("Brute force pomoci caesara - vysledek:\n" + brute.toUpperCase() + "\nShoda s anglickym slovnikem: " +  bruteOK);
    }


    /**
     * Pomoci caesarova algoritmu rozsifruje sifru.
     * @param step posun
     * @return rozsifrovany text
     */
    private String decryptCaesar(int step) {
        String encrypt = "";
        for (int i = 0; i < cipher.length(); i++) {

            int c = cipher.charAt(i) - step;
            char tmp;
            if (cipher.charAt(i) == 32) tmp = 32;
            else if (c < 97) {// a = 97
                tmp = (char) (c - 97 + 123);
            }
            else tmp = (char) c;
            encrypt = encrypt + tmp;
        }


        return encrypt;
    }

    /**
     * Pokus prolomit sifru pomoci brute-force algoritmu
     * @return prolomena sifra
     */
    public String bruteForce() {

        String res = "";
        int bestOK = 0;
        int countLetters = 26; // pocet pismen v abecede
        for (int i = 0; i < countLetters; i++) {
            String tmp = decryptCaesar(i);
            int ok = loadFrequencies.compareResultsCaesar(tmp);
            if (bestOK < ok) {
                res = tmp;
                bestOK = ok;
            }
        }
        return res;
    }

    private String frequencyAnalysis() {
        System.out.println(cipher);
        int[] frequency = new int[26]; // na kazde pozici je pismeno dle abecedy
        Arrays.fill(frequency, 0);
        char c;
        for (int i = 0; i < cipher.length(); i++) {
            c = cipher.charAt(i);
            if (c < NGramsLoader.ASCII_VALUE_A || c > NGramsLoader.ASCII_VALUE_Z ) { //mezera
                continue;
            }
            else {
                frequency[c - NGramsLoader.ASCII_VALUE_A]++;
            }

        }

        int maxValue = 0; //hodnota prvku na maximalni hodnote
        int maxValueIdx = 0; //index prvku na maximalni hodnote
        int secondMaxValue = 0; // budeme zaznamenavat i druhou nejvetsi hodnotu (nejcetnejsi byva pocet mezer)
        int secondMaxValueIdx = 0; // index pozice druhe nejvetsi cetnosti

        //hledani nejcetnejsiho znaku
        for (int i = 0; i < frequency.length; i++) {
            if (frequency[i] > maxValue) {
                secondMaxValue = maxValue;
                secondMaxValueIdx = maxValueIdx;
                maxValue = frequency[i];
                maxValueIdx = i;
            } else if (frequency[i] > secondMaxValue) {
                secondMaxValue = frequency[i];
                secondMaxValueIdx = i;
            }
        }

        System.out.println(Arrays.toString(frequency)+ " max value = " + maxValue + " second max value = " + secondMaxValue + "  " + secondMaxValueIdx);


        int stepSec = secondMaxValueIdx + NGramsLoader.ASCII_VALUE_A - NGramsLoader.SORTED_LETTERS_BY_FREQUENCY.charAt(1);
        System.out.println(stepSec);

        String decrypt = decryptCaesar(stepSec);


        return decrypt;
    }

}
