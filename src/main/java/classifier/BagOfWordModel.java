package classifier;

import classifier.BagOfWords.FileBasedVocabularyBuilder;
import classifier.BagOfWords.NGramVocabularyBuilder;
import preprocessing.Tokenizers.NGramTokenizer;

/**
 * Created by hk on 27.12.2017.
 */
public class BagOfWordModel {
    // GenerateNewBagOfWords step uses this vocabulary builder. It first generates a bag of words model and uses this to generate features/attributes
    public static NGramVocabularyBuilder nGramVocabularyBuilder = new NGramVocabularyBuilder(new NGramTokenizer(10, true));

    // UseExistingBagOfWordsModel step uses this vocabulary builder. It doesn't generate a bow model, but loads a previously generated vocabulary from the resource file vocabulary.txt so it's much faster
    public static FileBasedVocabularyBuilder fileBasedVocabularyBuilder = new FileBasedVocabularyBuilder(new NGramTokenizer(3, true));


    // pipeline steps
    public static UseExistingBagOfWordsModel useExistingBagOfWordsModel = new UseExistingBagOfWordsModel();
    public static GenerateNewBagOfWords generateNewBagOfWordsModel = new GenerateNewBagOfWords();
    public static ProcessDocuments processDocuments = new ProcessDocuments();
    public static OptimizeAttributes optimizeAttributes = new OptimizeAttributes();
    public static DumpToArffFile dumpToArffFile = new DumpToArffFile();
    public static ClassifyNaiveBayes classifyNaiveBayes = new ClassifyNaiveBayes();
    public static FoldCrossEvaluation foldCrossEvaluation = new FoldCrossEvaluation();
}
