import java.util.*;

public class FrequencyNGramCracker {

    private NGramsLoader loadFrequencies;
    private String cipherText;

    private ArrayList<Character> cipherAlphabet = new ArrayList<>(26);
    private HashMap<Character, Character> decryptedChars = new HashMap<>();
    private HashMap<Character, Character> duplicitChars = new HashMap<>();


    /***
     * Konstruktor tridy
     * @param cipherText text sifry
     */
    public FrequencyNGramCracker(String cipherText) {
        this.cipherText = cipherText;
        fillCipherAlphabet();
        loadFrequencies = new NGramsLoader();

        compareAndChangeTrigrams();
        compareAndChangeBigrams();
        compareAndChangeQuadrigrams();
        compareWithOtherTrigrams();
        compareAndChangeBigrams();
        compareByFrequencyLetters();

        cipherAlphabet.forEach(System.out::println);


    }

    private void compareAndChangeMostFreqWords() {
        String[] ciphers = cipherText.split(" ");

        for (String cipher : ciphers) {
            if (cipher.length() >= 2) {

            }
        }



    }

    private void fillCipherAlphabet() {
        for (int i = NGramsLoader.ASCII_VALUE_A; i <= NGramsLoader.ASCII_VALUE_Z; i++) {
            cipherAlphabet.add((char)i);
        }
    }
    private void removeFromCipherAphabet(char c) {
        for (int i = 0; i < cipherAlphabet.size(); i++) {
            if (cipherAlphabet.get(i) == c && cipherAlphabet.get(i) != null) {
                cipherAlphabet.remove(i);
            }
        }
    }

    private void compareByFrequencyLetters() {
        HashMap<Character, Integer> freq = new HashMap<>(cipherAlphabet.size());

        cipherAlphabet.forEach(character -> freq.put(character, 0));
        for (int i = 0; i < cipherText.length(); i++) {
            char c = cipherText.charAt(i);
            if (freq.containsKey(c)) freq.put(c, freq.get(c) + 1);
        }


        freq.forEach((c, val) -> System.out.println(c + " = " + val));

        String unassignedValues = getUnassignedValues();

        // serazeni hash mapy do arraylistu
        Object[] a = freq.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<Character, Integer>) o2).getValue()
                        .compareTo(((Map.Entry<Character, Integer>) o1).getValue());
            }
        });

        ArrayList<Character> sorted = new ArrayList<>();
        for (Object e : a) {

            sorted.add(((Map.Entry<Character, Integer>) e).getKey());
        }

//        sorted.forEach(System.out::println);
//
        for (int i = 0; i < sorted.size(); i++) {
            char c = sorted.get(i);
            char tmp1 = 0;
            char tmp2 = 0;
//            cipherText = cipherText.replaceAll(Character.toString(c), Character.toString(tmp1));

            int cmp = 0;
            int newcmp = 0;
            for (int j = 0; j < unassignedValues.length(); j++ ) {

                if (tmp1 == 0) cipherText = cipherText.replaceAll(Character.toString(c), Character.toString(tmp1));

                tmp2 = unassignedValues.charAt(j);

                if (tmp2 == c) continue;

                cmp = loadFrequencies.compareResultNgram(cipherText);

                String cipherTest = cipherText.replaceAll(Character.toString(tmp1), Character.toString(tmp2));
                newcmp = loadFrequencies.compareResultNgram(cipherTest);

                if (newcmp > cmp) {
                    tmp1 = tmp2;
                    cipherText = cipherTest;
                    unassignedValues = unassignedValues.replaceAll(Character.toString(tmp1), "");
                }
            }
//

        }

        System.out.println(cipherText);
    }

    private String getUnassignedValues() {
        String sortedLetters = NGramsLoader.SORTED_LETTERS_BY_FREQUENCY;
        String unassigned = "";
        for (int i  = 0; i < sortedLetters.length(); i++) {
            String letter = Character.toString(sortedLetters.charAt(i)).toUpperCase();
            if (!cipherText.contains(letter)) {
                unassigned = unassigned + letter;
            }
        }
        System.out.println(unassigned);
        return unassigned;
    }

    private void compareAndChangeBigrams() {

        List <String> sorted = findFrequencyBigrams();
        for (String bigram : loadFrequencies.bigrams) {

            if (!cipherText.contains(bigram)) {
                // System.out.println(trigram);

                for (String str : sorted) {
                    int upper = 0;
                    for (int i = 0; i < str.length(); i++) {

                        char ch = str.charAt(i);
                        if (ch >= 'A' && ch <= 'Z')
                            upper++;
                    }
                    if (upper == 1 && !str.contains("\n")) {
                        int conformity = 0;
                        for (int i = 0; i < bigram.length(); i++){

                            if (str.charAt(i) == bigram.charAt(i)) {
                                conformity++;
                            }
                        }
                        if (conformity == 1) {
                            for (int i  = 0; i < str.length(); i++) {
                                char c = str.charAt(i);
                                if (c >= 'a' && c <='z') {
                                    if (decryptedChars.containsKey(str.charAt(i)) || decryptedChars.containsValue(bigram.charAt(i))) {
                                        duplicitChars.put(str.charAt(i), bigram.charAt(i));
                                        continue;
                                    }
                                    decryptedChars.put(str.charAt(i), bigram.charAt(i));
                                    removeFromCipherAphabet(str.charAt(i));

                                    cipherText = cipherText.replaceAll(Character.toString(str.charAt(i)), Character.toString(bigram.charAt(i)));
                                    break;
                                }
                            }
                            break;
                        }

                    }
                }
            }
        }

        duplicitChars.forEach((crypt, encrypt) -> System.out.println(crypt + " ==D== " + encrypt));
        compareDuplicates();
        decryptedChars.forEach((crypt, encrypt) -> System.out.println(crypt + " ====== " + encrypt));
        System.out.println(cipherText);

    }
    private void compareAndChangeQuadrigrams() {
        List <String> sorted = findFrequencyQuadrigrams();
        for (String quadrigram : loadFrequencies.quadrigrams) {

            if (!cipherText.contains(quadrigram)) {
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
                        for (int i = 0; i < quadrigram.length(); i++){

                            if (str.charAt(i) == quadrigram.charAt(i)) {
                                conformity++;
                            }
                        }
                        if (conformity == 3) {
                            for (int i  = 0; i < str.length(); i++) {
                                char c = str.charAt(i);
                                if (c >= 'a' && c <='z') {
                                    if (decryptedChars.containsKey(str.charAt(i)) || decryptedChars.containsValue(quadrigram.charAt(i))) {
                                        duplicitChars.put(str.charAt(i), quadrigram.charAt(i));
                                        continue;
                                    }
                                    decryptedChars.put(str.charAt(i), quadrigram.charAt(i));
                                    removeFromCipherAphabet(str.charAt(i));
                                    cipherText = cipherText.replaceAll(Character.toString(str.charAt(i)), Character.toString(quadrigram.charAt(i)));
                                    break;
                                }
                            }
                            break;
                        }

                    }
                }
            }
        }

        duplicitChars.forEach((crypt, encrypt) -> System.out.println(crypt + " ==D== " + encrypt));
//        compareDuplicates();
        decryptedChars.forEach((crypt, encrypt) -> System.out.println(crypt + " ====== " + encrypt));
        System.out.println(cipherText);
    }

    /**
     * Metoda vyhodnoti nejcastejsi Ngramy v sifre a porovna je s prvnimi ctyrmi nejpouzivanejsimi v AJ.
     */
    private void compareAndChangeTrigrams() {
        List <String> sorted = findFrequencyTrigrams();
        for (int i = 0; i < 4; i++) {
            String truthTrig = loadFrequencies.trigrams.get(i);
            String cipherTrig = sorted.get(i);

            for (int j = 0; j < truthTrig.length(); j++) {
                if (!decryptedChars.containsKey(cipherTrig.charAt(j)))
                    decryptedChars.put(cipherTrig.charAt(j), truthTrig.charAt(j));

                cipherText = cipherText.replaceAll
                        (Character.toString(cipherTrig.charAt(j)),Character.toString(truthTrig.charAt(j)));

                removeFromCipherAphabet(cipherTrig.charAt(j));

            }

        }

        System.out.println(cipherText);

        cipherAlphabet.forEach(System.out::println);
        compareWithOtherTrigrams();

    }

    /** Po ziskani prvnich trigramu se pokusi  dale doplnit sifrovaci abecedu */
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
                                    if (decryptedChars.containsKey(str.charAt(i))) {
                                        duplicitChars.put(str.charAt(i), trigram.charAt(i));
                                        continue;
                                    }
                                    decryptedChars.put(str.charAt(i), trigram.charAt(i));
                                    removeFromCipherAphabet(str.charAt(i));
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

        compareDuplicates();
        decryptedChars.forEach((crypt, encrypt) -> System.out.println(crypt + " ====== " + encrypt));
        System.out.println(cipherText);
//          duplicitChars.forEach((crypt, encrypt) -> System.out.println(crypt + " ====== " + encrypt));
    }

    /**
     * Metoda testuje duplicitni znaky. Zkouma, ktery znak je vhodnejsi
     */
    private void compareDuplicates() {
        Iterator it = duplicitChars.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry item = (Map.Entry) it.next();
                char key = (char) item.getKey();
                char value = (char) item.getValue();
                char origValue = decryptedChars.get(key);
                String cipherTest = cipherText.replaceAll(Character.toString(origValue), Character.toString(value));

                int original = loadFrequencies.compareResultNgram(cipherText);
                int compared = loadFrequencies.compareResultNgram(cipherTest);

                if (original < compared) { // pokud bude vhdonejsi zmeni se text
                    decryptedChars.put(key, value);
                    cipherText = cipherTest;
                }
                it.remove();
                if (duplicitChars.isEmpty()) break;

            }
    }
    private List<String> findFrequencyQuadrigrams() {

        return findFrequencyNgrams(4);
    }


    private List<String> findFrequencyTrigrams() {
       return findFrequencyNgrams(3);
    }


    private ArrayList<String> findFrequencyBigrams() {
       return findFrequencyNgrams(2);

    }

    /**
     * Metoda vytvori seznam vsech ngramu sifry.
     * @param n - typ n-gramu
     * @return serazena posloupnost nejcastejsich ngramu
     */
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
        // serazeni hash mapy do arraylistu
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

        return sorted;
    }


}
