package preprocessing;

import preprocessing.Tokenizers.LuceneTokenizer;
import preprocessing.Tokenizers.NGramTokenizer;
import preprocessing.Tokenizers.Tokenizer;
import preprocessing.BagOfWords.*;

/**
 * Created by hk on 16.12.2017.
 */
public class Preprocessing {
    public static StopwordFilter stopwordFilter = new StopwordFilter();
    public static WordFilter wordFilter = new WordFilter();
    public static Tokenizer tokenizer = new Tokenizer();
    public static NGramTokenizer biGramTokenizer = new NGramTokenizer(2, false);
    public static NGramTokenizer triGramTokenizer = new NGramTokenizer(3, false);
    public static NGramTokenizer mixedBiGramTokenizer = new NGramTokenizer(2, true);
    public static NGramTokenizer mixedTriGramTokenizer = new NGramTokenizer(3, true);
    public static LuceneTokenizer luceneTokenizer = new LuceneTokenizer();
    public static Stemmer stemmer = new Stemmer();
    public static MaxEntPosTagger maxEntPosTagger = new MaxEntPosTagger();
    public static VocabularyBuilder vocabularyBuilder = new VocabularyBuilder();
    public static NGramVocabularyBuilder bigramVocabularyBuilder = new NGramVocabularyBuilder();
    public static NGramVocabularyBuilder trigramVocabularyBuilder = new NGramVocabularyBuilder();
    public static FileBasedVocabularyBuilder fileBasedVocabularyBuilder = new FileBasedVocabularyBuilder();

}
