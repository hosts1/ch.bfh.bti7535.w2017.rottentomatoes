package preprocessing;

import utils.FileReader;
import utils.Pipe;

import java.util.*;

public class StopwordFilter implements Pipe<String, String> {
    @Override
    public String process(String text){
        try {
            FileReader fr = new FileReader();
            String stopwordsStr = fr.readFile("stopwords.txt");
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
