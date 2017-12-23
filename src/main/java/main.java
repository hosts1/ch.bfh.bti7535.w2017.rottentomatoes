import features.*;
import pipeline.Pipeline;
import preprocessing.Preprocessing;
import preprocessing.ToTrigramVector;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

public class main {

    public static void main(String[] args) throws Exception {
        ReviewData rvw = new ReviewData("txt_sentoken");
        Features features = new Features();

        // Unigrams

        // build a bag of words / vocabulary
        Pipeline<String, Void> bagOfWordsChain = Pipeline
                .start(Preprocessing.luceneTokenizer)
                .append(Preprocessing.stopwordFilter)
                .append(Preprocessing.wordFilter)
                .append(Preprocessing.vocabularyBuilder);

        rvw.getReviews().parallelStream().forEach((review) -> {
            bagOfWordsChain.run(review.getSecond());
        });

        // sort the vocabulary in descending order and reduce it to n words
        Preprocessing.vocabularyBuilder.sortAndLimit(1000);
        System.out.println(Preprocessing.vocabularyBuilder._vocab);

        // add a feature for every word
        Iterator it = Preprocessing.vocabularyBuilder._vocab.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String word = (String) pair.getKey();
            features.addFeature(new UnigramFeature(word));
        }

        // the following pipeline creates vectors (actually maps) for every document, containing the number of occurrences of the words within the vocabulary
        Pipeline<String, HashMap<String,Integer>> stringToWordVectorChain = Pipeline
                .start(Preprocessing.luceneTokenizer)
                .append(Preprocessing.stopwordFilter)
                .append(Preprocessing.wordFilter)
                .append(Preprocessing.toWordVector);


        // BiGrams

        // build a bag of words / vocabulary
        Pipeline<String, Void> bigramBagOfWordsChain = Pipeline
                .start(Preprocessing.biGramTokenizer)
                .append(Preprocessing.wordFilter)
                .append(Preprocessing.bigramVocabularyBuilder);


        rvw.getReviews().parallelStream().forEach((review) -> {
            bigramBagOfWordsChain.run(review.getSecond());
        });

        // sort the vocabulary in descending order and reduce it to n words
        Preprocessing.bigramVocabularyBuilder.sortAndLimit(1000);
        System.out.println(Preprocessing.bigramVocabularyBuilder._vocab);

        // add a feature for every word
        it = Preprocessing.bigramVocabularyBuilder._vocab.entrySet().iterator();
        i = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String word = (String) pair.getKey();
            features.addFeature(new BigramFeature(word));
        }

        // the following pipeline creates vectors (actually maps) for every document, containing the number of occurrences of the words within the vocabulary
        Pipeline<String, HashMap<String,Integer>> stringToBigramVectorChain = Pipeline
                .start(Preprocessing.biGramTokenizer)
                .append(Preprocessing.wordFilter)
                .append(Preprocessing.toBigramVector);

        // Trigrams

        // build a bag of words / vocabulary
        Pipeline<String, Void> trigramBagOfWordChain = Pipeline
                .start(Preprocessing.triGramTokenizer)
                .append(Preprocessing.wordFilter)
                .append(Preprocessing.trigramVocabularyBuilder);


        rvw.getReviews().parallelStream().forEach((review) -> {
            trigramBagOfWordChain.run(review.getSecond());
        });

        // sort the vocabulary in descending order and reduce it to n words
        Preprocessing.trigramVocabularyBuilder.sortAndLimit(1000);
        System.out.println(Preprocessing.trigramVocabularyBuilder._vocab);

        // add a feature for every word
        it = Preprocessing.trigramVocabularyBuilder._vocab.entrySet().iterator();
        i = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String word = (String) pair.getKey();
            features.addFeature(new TrigramFeature(word));
        }

        // the following pipeline creates vectors (actually maps) for every document, containing the number of occurrences of the words within the vocabulary
        Pipeline<String, HashMap<String,Integer>> stringToTrigramVectorChain = Pipeline
                .start(Preprocessing.triGramTokenizer)
                .append(Preprocessing.wordFilter)
                .append(Preprocessing.toTrigramVector);



        // Create training instances
        Feature sentiment = features.addFeature(new NominalFeature("sentiment", new ArrayList<String>(Arrays.asList("positive", "neutral", "negative")), (review) -> { return "neutral";}));
        Instances trainingSet = new Instances("Data", features.getAttributes(), 2000); // trainingSet with our features and a capacity of 1000 records
        trainingSet.setClassIndex(0); // the class attribute is the first one in the vector

        // process every review, extract feature values and add them to the training-set
        rvw.getReviews().parallelStream().forEach((review) -> {
            Instance inst = new DenseInstance(features.getNumberOfFeatures());
            // set the sentiment class to positive or negative label
            features.setClass(inst, review.getFirst());

            HashMap<String,Integer> wordVector = stringToWordVectorChain.run(review.getSecond());
            HashMap<String,Integer> bigramVector = stringToBigramVectorChain.run(review.getSecond());
            HashMap<String,Integer> trigramVector = stringToTrigramVectorChain.run(review.getSecond());
            features.determineFeatureValues(inst, review.getSecond(), wordVector, bigramVector, trigramVector); // for each feature it will do "setValue" on the instance
            inst.setValue(sentiment.attr, review.getFirst());
            trainingSet.add(inst);
        });


        // Create a naïve bayes classifier
        try {
            Classifier cModel = (Classifier) new NaiveBayes();
            cModel.buildClassifier(trainingSet);

            // Test the model
            Evaluation eTest = new Evaluation(trainingSet);
            eTest.crossValidateModel(cModel, trainingSet, 10, new Random(1));

            // print results
            String strSummary = eTest.toSummaryString();
            System.out.println(strSummary);

            //double[][] cmMatrix = eTest.confusionMatrix();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}