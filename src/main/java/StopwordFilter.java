import java.nio.charset.StandardCharsets;
import java.util.*;

public class StopwordFilter {
    public static String filter(String text){
        try {
            String stopwordsStr = Utils.readFile("stopwords.txt", StandardCharsets.UTF_8);
            ArrayList<String> stopwords = new ArrayList<String>();
            StringTokenizer stok = new StringTokenizer(stopwordsStr, "\n");
            while (stok.hasMoreTokens()) {
                String token = stok.nextToken(); // get and save in variable so it can be used more than once
                stopwords.add(token); // use already extracted value
            }

            for(String str: stopwords){
                text = text.replace(" " + str + " ", " ");
                text = text.replace("." + str + " ", " ");
                text = text.replace(" " + str + ".", ".");
            }

            return text;
        }catch(Exception ex){
            return "";
        }
    }

}
