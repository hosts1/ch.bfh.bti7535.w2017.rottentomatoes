package preprocessing;

import utils.FileReader;
import pipeline.Pipe;

import java.util.*;

public class StopwordFilter implements Pipe<List<String>, List<String>> {
    @Override
    public List<String> process(List<String> text){
        try {
            FileReader fr = new FileReader();
            String stopwordsStr = fr.readFile("stopwords.txt");
            HashMap<String, Boolean> stopwords = new HashMap<String, Boolean>();
            StringTokenizer stok = new StringTokenizer(stopwordsStr, "\n");
            while (stok.hasMoreTokens()) {
                String token = stok.nextToken(); // get and save in variable so it can be used more than once
                stopwords.put(token, true); // use already extracted value
            }

            List<String> newList = new ArrayList<String>();
            for(String word: text)
            {
                if(!stopwords.getOrDefault(word, false))
                    newList.add(word);
            }


            return text;
        }catch(Exception ex){
            return new ArrayList<String>();
        }
    }

}
