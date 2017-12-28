package preprocessing;

import preprocessing.Tokenizers.LuceneTokenizer;
import preprocessing.Tokenizers.NGramTokenizer;
import preprocessing.Tokenizers.Tokenizer;
import classifier.BagOfWords.*;

/**
 * Created by hk on 16.12.2017.
 */
public class Preprocessing {
    public static StopwordFilter stopwordFilter = new StopwordFilter();
    public static WordFilter wordFilter = new WordFilter();
    public static Tokenizer tokenizer = new Tokenizer();
    public static LuceneTokenizer luceneTokenizer = new LuceneTokenizer();
    public static Stemmer stemmer = new Stemmer();
    public static MaxEntPosTagger maxEntPosTagger = new MaxEntPosTagger();


}
