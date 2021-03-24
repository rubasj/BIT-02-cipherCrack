import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PolyAlphabetCrack {


    private String ciphertext;
    /** Integer uklada vzdalenost */
    private ArrayList<Integer> sequencesDist;

    public PolyAlphabetCrack(String cipherText){
        this.ciphertext = cipherText;

        System.out.println(cipherText);

        findSequences();

    }

    private void findSequences() {
        HashMap<String, Integer> map = new HashMap<>();
        String cipherTmp = ciphertext.replaceAll(" ", "");
        cipherTmp = cipherTmp.replaceAll("\n", "");

        for (int j = 4; j <= 25; j++) {               // maximalni delky sekvenci
            for (int i = 0; i < cipherTmp.length(); i++) {
                if (i+j >= cipherTmp.length()) break;
                String substring = cipherTmp.substring(i, i + j);
                if (map.containsKey(substring)) {
                    map.put(substring, map.get(substring) + 1);
                } else {
                    map.put(substring, 1);
                }
            }
        }
        HashMap <String, Integer> seq = new HashMap<>();
        Iterator it = map.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry item = (Map.Entry) it.next();
            String key = (String) item.getKey();
            int value = (int) item.getValue();

            if (value == 2){
                int index1 = cipherTmp.indexOf(key);
                String tmpSub = cipherTmp.substring(index1 + key.length(), cipherTmp.length());
                int index2 = tmpSub.indexOf(key) + key.length();
                System.out.println("first: " + index1 + " second: " + index2);

            }
        }
    }
}
