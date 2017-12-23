package preprocessing;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import pipeline.Pipe;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hk on 23.12.2017.
 */
public class NGramTokenizer implements Pipe<String,List<String>> {
    int n = 0;

    public NGramTokenizer(int n){
        this.n = n;
    }

    @Override
    public List<String> process(String input)  {
        Reader reader = new StringReader(input);
        TokenStream tokenizer = new StandardTokenizer(Version.LUCENE_36, reader);
        tokenizer = new ShingleFilter(tokenizer, n);
        CharTermAttribute charTermAttribute = tokenizer.addAttribute(CharTermAttribute.class);

        List<String> res = new ArrayList<String>();
        try {
            while (tokenizer.incrementToken()) {
                String token = charTermAttribute.toString();
                if(token.split(" ").length == this.n)
                    res.add(token);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
