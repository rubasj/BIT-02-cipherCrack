import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Trida nacte pole nejpouzivanejsich n gramu, pismen.
 * Vzdy bude platit, ze ve vstupnich souborech jsou n-gramy serazeny dle pouzivatelnosti.
 * Tudiz vzdy bude platit, ze prvni n-gram v poli bude nejvice cetny
 */
public class NGramsComparer {

    /** Pismena serazena dle cetnosti pouzivatelnosti v AJ */
    public static final String SORTED_LETTERS_BY_FREQUENCY = " etaoinshrdlucmfwgypbvkxjqz";

    public static final int ASCII_VALUE_A = 'a';
    public static final int ASCII_VALUE_Z = 'z';

    private static final String BIGRAMS_FILE = "bigrams.txt";
    private static final String TRIGRAMS_FILE = "trigrams.txt";
    private static final String QUADRIGRAMS_FILE = "quadrigrams.txt";
    private static final String ENGLISH_DICTIONARY_FILE = "EnglishDictionary.txt";
    public List<String> words;
    public List<String> quadrigrams;
    public List<String> bigrams;
    public List<String> trigrams;

    public NGramsComparer() {

        bigrams = loadNGrams(BIGRAMS_FILE);
        trigrams = loadNGrams(TRIGRAMS_FILE);
        quadrigrams = loadNGrams(QUADRIGRAMS_FILE);
        words = loadNGrams(ENGLISH_DICTIONARY_FILE);
    }

    /**
     * Metoda nacte ze souboru N-gramy.
     * @param nGramsFile soubor s N-grmamy.
     * @return
     */
    private List<String> loadNGrams(String nGramsFile) {
        List<String> tmp = new ArrayList<>();

        try {
            FileReader fr  = new FileReader(nGramsFile);
            BufferedReader br = new BufferedReader(fr);

            String line;
            while((line = br.readLine())!= null ) {

                    tmp.add(line);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
    }
        return tmp;
    }


    // Metoda bude porovnavat slova s desifrovym textem

    public int compareResults(String decrypt) {
        String tmp = decrypt.replaceAll("\\s+", "");
        int count = 0;
        for (String word: words
             ) {
            if (tmp.contains(word))
                count++;

        }

        return count;
    }




}
