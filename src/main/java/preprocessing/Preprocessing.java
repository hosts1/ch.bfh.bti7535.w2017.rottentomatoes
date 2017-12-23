package preprocessing;

/**
 * Created by hk on 16.12.2017.
 */
public class Preprocessing {
    public static StopwordFilter stopwordFilter = new StopwordFilter();
    public static WordFilter wordFilter = new WordFilter();
    public static Tokenizer tokenizer = new Tokenizer();
    public static NGramTokenizer biGramTokenizer = new NGramTokenizer(2);
    public static NGramTokenizer triGramTokenizer = new NGramTokenizer(3);
    public static LuceneTokenizer luceneTokenizer = new LuceneTokenizer();
    public static Stemmer stemmer = new Stemmer();
    public static MaxEntPosTagger maxEntPosTagger = new MaxEntPosTagger();
    public static VocabularyBuilder vocabularyBuilder = new VocabularyBuilder();
    public static NGramVocabularyBuilder bigramVocabularyBuilder = new NGramVocabularyBuilder();
    public static NGramVocabularyBuilder trigramVocabularyBuilder = new NGramVocabularyBuilder();

    public static ToWordVector toWordVector = new ToWordVector();
    public static ToBigramVector toBigramVector = new ToBigramVector();
    public static ToTrigramVector toTrigramVector = new ToTrigramVector();

}
