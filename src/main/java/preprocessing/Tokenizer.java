package preprocessing;

import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import pipeline.Pipe;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tokenizer implements Pipe<String, List<String>> {
    @Override
    public List<String> process(String string){
        //StringTokenizer stok = new StringTokenizer(text, " ");
        //return stok;

        List<String> result = new ArrayList<String>();

        WhitespaceTokenizer tokenizer = WhitespaceTokenizer.INSTANCE;
        String tokens[] = tokenizer.tokenize(string);
        result = Arrays.asList(tokens);

        return result;
    }
}
