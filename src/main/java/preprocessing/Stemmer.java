package preprocessing;
import org.tartarus.snowball.ext.PorterStemmer;

import java.util.List;

/**
 * Created by hk on 16.12.2017.
 */
public class Stemmer implements Pipe<List<String>, String>{

    @Override
    public String process(List<String> input) {
        String res = "";
        for(String s: input) {
            PorterStemmer stemmer = new PorterStemmer();
            stemmer.setCurrent(s);
            stemmer.stem();
            res += stemmer.getCurrent() + " ";
        }
        return res;
    }
}
