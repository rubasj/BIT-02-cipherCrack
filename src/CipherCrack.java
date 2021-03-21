import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Vychozi bod programu
 * @author Jan Rubas
 */
public class CipherCrack {

    // https://www.nku.edu/~christensen/1402%20Friedman%20test%202.pdf?fbclid=IwAR0pE6GqQ2XcGmvJJ8HJow1pm3HcLyTg8_PeC60q-UfM9Z1mxjLLvNLwgpY
    public boolean indexOfCoicidence(String cipherText) {
        int[] frequency = new int[26];
        char c;
        int charCount = 0;
        for (int i = 0; i < cipherText.length(); i++) {
            c = cipherText.charAt(i);
            if (c >= NGramsLoader.ASCII_VALUE_A && c <= NGramsLoader.ASCII_VALUE_Z ) {
                frequency[c - NGramsLoader.ASCII_VALUE_A]++;
                charCount++;
            }


        }

        double index = 0;

        for (int n: frequency) {
            index += (double) n / (double) charCount * ((double) n-1) / ((double)charCount-1);
        }

        int cmp = Double.compare(index, 0.0515); // stred mezi 0.065 a 0.038, hodnoty viz ppst. shody, zda se jedna o poly nebo mono
        if (cmp > 0) {
            return true; // jedna se o monoalfabetickou sifru
        }

        return false;
    }

    public static void main(String[] args) {
        LoadText loadText = new LoadText(args[0]);

        FrequencyAnalysisCaesar freqAnalysis = new FrequencyAnalysisCaesar();
//        NGramsLoader dataLoader = new NGramsLoader();

        CipherCrack crack = new CipherCrack();
        boolean isMono = crack.indexOfCoicidence(loadText.text.get(2));
        System.out.println("Is mono:" + " " + isMono);
//        freqAnalysis.caesarFrequencyAnalysis(loadText.text.get(0));



    }
}

/**
 * Trida nacte texty ze souboru
 */
class LoadText {
    private BufferedReader in;
    public List <String> text;
    private static final char TEXT_SEPARATOR = '#';


    public LoadText(String fileName) {
        text = new ArrayList<>();
        System.out.println(fileName);
        try {
            FileReader fr = new FileReader(fileName);
            in = new BufferedReader(fr);
            int c = 0;
            StringBuilder tmp = new StringBuilder();
            while ((c = in.read()) != -1) {
                if (c != '#')
                    tmp.append((char) c);

                if (c == '#') {
                    text.add(tmp.toString());
                    tmp = new StringBuilder();

                }
            }
            if (tmp.toString().length() > 0)
                text.add(tmp.toString());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(text.size());
    }

}


/**
 * Trida pro pokus o prolomeni "natvrdo" a pokus zda se jedna o Ceasarovo sifru o n-krocich
 */
class FrequencyAnalysisCaesar {

    NGramsLoader loadFrequencies;

    public FrequencyAnalysisCaesar() {
        loadFrequencies = new NGramsLoader();
    }

    /**
     * Pomoci caesarova algoritmu rozsifruje sifru.
     * @param cipher sifra
     * @param step posun
     * @return rozsifrovany text
     */
    String decryptCeasar(String cipher, int step) {
        String encrypt = "";
        for (int i = 0; i < cipher.length(); i++) {
            int c = cipher.charAt(i) - step;
            char tmp;
            if (c < 97) {// a = 97
                tmp = (char) (c - 97 + 123);
            }
            else tmp = (char) c;
            encrypt = encrypt + tmp;
        }


        return encrypt;
    }

    public String bruteForce(String cipher) {
        String tmp = "";
        return tmp;
    }

    public String caesarFrequencyAnalysis(String cipher) {
        System.out.println(cipher);
        int[] frequency = new int[27]; // pozice 26 je pro mezeru
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

        int step = secondMaxValueIdx + NGramsLoader.ASCII_VALUE_A - loadFrequencies.SORTED_LETTERS_BY_FREQUENCY.charAt(1);
        System.out.println(step);


        return null;
    }
}

