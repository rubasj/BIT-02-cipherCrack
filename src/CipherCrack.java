import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Trida rozhodne jak bude postupovat v prolomeni sifer.
 * @author Jan Rubas
 */
public class CipherCrack {

    /** objekt predstavujici text sifry */
    private LoadText textCipher;

    /**
     * Konstruktor tridy
     * @param cipher sifra, kterou se pokusi program prolomit
     */
    public CipherCrack(String cipher) {
        this.textCipher = new LoadText(cipher);
    }

    /**
     * Pomoci indexu shody se predpovi zda se jedna o monoalfabetickou substituci nebo polyalfabetickou
     * @param cipherText text sifry.
     * @return True - monoalfabeticka, False - polyalfabeticka
     */
    // https://www.nku.edu/~christensen/1402%20Friedman%20test%202.pdf?fbclid=IwAR0pE6GqQ2XcGmvJJ8HJow1pm3HcLyTg8_PeC60q-UfM9Z1mxjLLvNLwgpY
    private boolean indexOfCoicidence(String cipherText) {
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

    /**
     * Metoda rozhodne, o jaky druh sifry se jedna.
     */
    public void crack(){

        CaesarCracker caasarCrack;
//        NGramsLoader dataLoader = new NGramsLoader();

//        boolean isMono = this.indexOfCoicidence(textCipher.text.get(0));
//        System.out.println("Is mono:" + " " + isMono);
//
//        if (this.indexOfCoicidence(textCipher.text.get(0))) // is probably monoalphabetic cipher
            caasarCrack = new CaesarCracker(textCipher.text.get(0));

        FrequencyNGramCracker x = new FrequencyNGramCracker(textCipher.text.get(1));
//        PolyAlphabetCrack c = new PolyAlphabetCrack(textCipher.text.get(2));
    }



    /**
     * Vstupni bod programu
     * @param args parametry prikazove radky.
     */
    public static void main(String[] args) {


        CipherCrack crack = new CipherCrack(args[0]);

        crack.crack();



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



