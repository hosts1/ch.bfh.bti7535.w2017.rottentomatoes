package preprocessing;

/**
 * Created by hk on 16.12.2017.
 */
public class Preprocessing {
    public static Lowercase lowercase = new Lowercase();
    public static StopwordFilter stopwordFilter = new StopwordFilter();
    public static Tokenizer tokenizer = new Tokenizer();
    public static LuceneTokenizer luceneTokenizer = new LuceneTokenizer();

    public static Stemmer stemmer = new Stemmer();
    public static MaxEntPosTagger maxEntPosTagger = new MaxEntPosTagger();
    public static VocabularyBuilder vocabularyBuilder = new VocabularyBuilder();
    public static ToWordVector toWordVector = new ToWordVector();
}
