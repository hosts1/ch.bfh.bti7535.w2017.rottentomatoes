package preprocessing;

import pipeline.Pipe;
import utils.FileReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class WordFilter implements Pipe<List<String>, List<String>> {
    @Override
    public List<String> process(List<String> text){
        try {
            FileReader fr = new FileReader();
            String ignorewordstr = fr.readFile("ignorewords.txt");
            HashMap<String, Boolean> ignorewords = new HashMap<String, Boolean>();
            StringTokenizer stok = new StringTokenizer(ignorewordstr, "\n");
            while (stok.hasMoreTokens()) {
                String token = stok.nextToken(); // get and save in variable so it can be used more than once
                ignorewords.put(token, true); // use already extracted value
            }

            List<String> newList = new ArrayList<String>();
            for(String word: text)
            {
                if(!ignorewords.getOrDefault(word, false))
                    newList.add(word);
            }

            return newList;
        }catch(Exception ex){
            return new ArrayList<String>();
        }
    }

}
