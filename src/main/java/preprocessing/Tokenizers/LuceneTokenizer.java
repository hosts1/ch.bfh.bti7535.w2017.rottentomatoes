package preprocessing.Tokenizers;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import pipeline.Pipe;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class LuceneTokenizer implements Pipe<String, List<String>> {
    @Override
    public List<String> process(String string){
        //StringTokenizer stok = new StringTokenizer(text, " ");
        //return stok;
        List<String> result = new ArrayList<String>();
        try {
            StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
            TokenStream stream  = analyzer.tokenStream(null, new StringReader(string));
            stream.reset();
            while (stream.incrementToken()) {
                result.add(stream.getAttribute(CharTermAttribute.class).toString());
            }
        } catch (IOException e) {
            // not thrown b/c we're using a string reader...
            throw new RuntimeException(e);
        }
        return result;
    }
}
