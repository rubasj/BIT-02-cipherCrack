import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PolyAlphabetCrack {


    private static final int MAX_DIVISOR = 26;
    private String ciphertext;
    private HashMap<Integer, Integer> divisors = new HashMap<>(26);
    private int mostFreqDivisor;


    public PolyAlphabetCrack(String cipherText){
        this.ciphertext = cipherText;

        System.out.println(cipherText);

        findSequences();

    }

    private void findSequences() {
        HashMap<String, Integer> map = new HashMap<>();
        String cipherTmp = ciphertext.replaceAll(" ", "");
        cipherTmp = cipherTmp.replaceAll("\n", "");

        ArrayList<Integer> sequenceDist = new ArrayList<Integer>();

        for (int j = 4; j <= 25; j++) {               // maximalni delky sekvenci
            for (int i = 0; i < cipherTmp.length(); i++) {
                if (i + j >= cipherTmp.length()) break;
                String substring = cipherTmp.substring(i, i + j);
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
                int index1 = cipherTmp.indexOf(key);
                String tmpSub = cipherTmp.substring(index1 + key.length(), cipherTmp.length());
                int index2 = tmpSub.indexOf(key) + key.length();
                System.out.println("first: " + index1 + " second: " + index2);
                sequenceDist.add(index2);
            }
        }
        findDivisors(sequenceDist);
        findMostFrequentDivisor();
    }

    private void findMostFrequentDivisor() {
        Iterator it = divisors.entrySet().iterator();
        int maxDivisor = 2;
        while (it.hasNext()) {
            Map.Entry item = (Map.Entry) it.next();
            int value = (int)item.getValue();
            if (value > divisors.get(maxDivisor)) {
                maxDivisor = (int)item.getKey();
            }
            System.out.println( item.getKey()+ "\t==\t" + item.getValue());
        }
        System.out.println(maxDivisor);
        mostFreqDivisor = maxDivisor;
    }

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
}
