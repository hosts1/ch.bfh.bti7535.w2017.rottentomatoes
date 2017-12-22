package preprocessing;
import org.tartarus.snowball.ext.PorterStemmer;
import pipeline.Pipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hk on 16.12.2017.
 */
public class Stemmer implements Pipe<List<String>, List<String>> {

    @Override
    public List<String> process(List<String> input) {
        // String res = "";
        List<String> res = new ArrayList<String>();
        for(String s: input) {
            PorterStemmer stemmer = new PorterStemmer();
            stemmer.setCurrent(s);
            stemmer.stem();
            //res += stemmer.getCurrent() + " ";
            res.add(stemmer.getCurrent());
        }
        return res;
    }
}
