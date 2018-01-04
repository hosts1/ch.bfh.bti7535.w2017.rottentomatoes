package classifier;

import classifier.BagOfWords.NGramVocabularyBuilder;
import preprocessing.Tokenizers.NGramTokenizer;
import utils.Pair;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by hk on 27.12.2017.
 */
public class BagOfWordModel {
    public static ConcurrentMap<String, List<String>> _tokenizedStringCache = new ConcurrentHashMap();

    // GenerateNewBagOfWords step uses this vocabulary builder. It first generates a bag of words model and uses this to generate features/attributes
    public static NGramVocabularyBuilder nGramVocabularyBuilder = new NGramVocabularyBuilder(new NGramTokenizer(4, true));

    // pipeline steps
    public static GenerateNewBagOfWords generateNewBagOfWordsModel = new GenerateNewBagOfWords();
    public static TrainModel trainModel = new TrainModel();
    public static ProcessDocuments processDocuments = new ProcessDocuments();
    public static OptimizeAttributes optimizeAttributes = new OptimizeAttributes();
    public static DumpToArffFile dumpToArffFile = new DumpToArffFile();
    public static ClassifyNaiveBayes classifyNaiveBayes = new ClassifyNaiveBayes();
    public static EvaluateClassifier evaluateClassifier = new EvaluateClassifier();
    // public static WekaToWordVector wekaToWordVector = new WekaToWordVector(); // wekas StringToWordVector, not used


    public static List<String> queryTokenizedStringCache(String str){
        return _tokenizedStringCache.getOrDefault(str, null);
    }

    public static void addToCache(String str, List<String> tokens){
        _tokenizedStringCache.put(str, tokens);
    }
}
