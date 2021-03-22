import java.util.HashMap;

public class FrequencyNGramCracker {

    private NGramsComparer loadFrequencies;
    private String cipherText;


    public FrequencyNGramCracker(String cipherText) {
        this.cipherText = cipherText.replaceAll("\n", "");

        loadFrequencies = new NGramsComparer();
         findFrequencyBigrams();
    }

    private void findFrequencyBigrams() {
        String[] devided = cipherText.split(" ");

        HashMap<String, Integer> seznam = new HashMap<>();

        String bigram = "";
        for (String word : devided) { // prochazi slovo po slovu
            if (word.length() >= 2) { // pokud je slovo alespon 2 znaky
                for (int i = 0; i < word.length()-1; i++){ // prochazeni slova a pismeno po pismenu a vytvareni bigramu
                    bigram = word.substring(i, i+2);
                    if (seznam.containsKey(bigram)) {  // zjisti zda uz slovo nebylo nalezeno, kdyz ano, inkrementuje value
                        seznam.put(bigram, seznam.get(bigram) + 1);

                    } else {        // kdyz slovo neexistuje, prida do seznamu
                        seznam.put(bigram, 1);
                    }
                }
            }
        }
        seznam.forEach((s, integer) -> System.out.println(s + " " + integer));

    }

    private void findFrequencyTrigrams() {
        String[] devided = cipherText.split(" ");
    }

    private void findFrequencyQuadrigrams() {
        String[] devided = cipherText.split(" ");
    }
}
