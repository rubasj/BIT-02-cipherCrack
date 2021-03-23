import javax.sound.midi.SysexMessage;
import java.util.*;

public class FrequencyNGramCracker {

    private NGramsLoader loadFrequencies;
    private String cipherText;

    private HashMap<Character, Character> decryptedChars = new HashMap<>();



    public FrequencyNGramCracker(String cipherText) {
        this.cipherText = cipherText;

        loadFrequencies = new NGramsLoader();

        compareAndChangeTrigrams();
    }

    private void compareAndChangeTrigrams() {
        List <String> sorted = findFrequencyTrigrams();
        for (int i = 0; i < 4; i++) {
            String truthTrig = loadFrequencies.trigrams.get(i);
            String cipherTrig = sorted.get(i);

            for (int j = 0; j < truthTrig.length(); j++) {
                decryptedChars.put(cipherTrig.charAt(j), truthTrig.charAt(j));
                cipherText = cipherText.replaceAll(Character.toString(cipherTrig.charAt(j)),Character.toString(truthTrig.charAt(j))) ;
            }

        }

        System.out.println(cipherText);

        compareWithOtherTrigrams();

    }

    private void compareWithOtherTrigrams() {
        List<String> sorted = findFrequencyTrigrams();


        for (String trigram : loadFrequencies.trigrams) {

            if (!cipherText.contains(trigram)) {
              // System.out.println(trigram);


                for (String str : sorted) {
                    int upper = 0;
                    for (int i = 0; i < str.length(); i++) {

                        char ch = str.charAt(i);
                        if (ch >= 'A' && ch <= 'Z')
                            upper++;
                    }
                    if (upper == 2 && !str.contains("\n")) {
                        int conformity = 0;
                        for (int i = 0; i < trigram.length(); i++){

                            if (str.charAt(i) == trigram.charAt(i)) {
                                conformity++;
                            }
                        }
                        if (conformity == 2) {
                            for (int i  = 0; i < str.length(); i++) {
                                char c = str.charAt(i);
                                if (c >= 'a' && c <='z') {
                                    System.out.println(c + "==" + trigram.charAt(i) + "\n" + trigram + " " + str);
                                    decryptedChars.put(str.charAt(i), trigram.charAt(i));
                                    cipherText = cipherText.replaceAll(Character.toString(str.charAt(i)), Character.toString(trigram.charAt(i)));
                                    break;
                                }
                            }
                            break;
                        }

                    }

                }
            }
        }

        decryptedChars.forEach((crypt, encrypt) -> System.out.println(crypt + " ====== " + encrypt));
        System.out.println(cipherText);
    }


    private List<String> findFrequencyTrigrams() {
       return findFrequencyNgrams(3);
    }

    private void findFrequencyBigrams() {
        findFrequencyNgrams(2);

    }

    private ArrayList<String> findFrequencyNgrams(int n) {
        String[] devided = cipherText.split(" ");
        HashMap<String, Integer> seznam = new HashMap<>();

        String ngram = "";
        for (String word : devided) { // prochazi slovo po slovu
            if (word.length() >= n) { // pokud je slovo alespon n znaku
                for (int i = 0; i < word.length()-(n-1); i++){ // prochazeni slova a pismeno po pismenu a vytvareni bigramu
                    ngram = word.substring(i, i+n);
                    if (seznam.containsKey(ngram)) {  // zjisti zda uz slovo nebylo nalezeno, kdyz ano, inkrementuje value
                        seznam.put(ngram, seznam.get(ngram) + 1);

                    } else {        // kdyz slovo neexistuje, prida do seznamu
                        seznam.put(ngram, 1);
                    }
                }
            }
        }



        Object[] a = seznam.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Integer>) o2).getValue()
                        .compareTo(((Map.Entry<String, Integer>) o1).getValue());
            }
        });

        ArrayList<String> sorted = new ArrayList<>();
        for (Object e : a) {

            sorted.add(((Map.Entry<String, Integer>) e).getKey());
        }

        System.out.println(sorted.get(2));
        return sorted;
    }

    private void findFrequencyQuadrigrams() {
        String[] devided = cipherText.split(" ");
    }
}
