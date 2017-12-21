// import weka.classifiers.bayes.NaiveBayes;

import utils.Pipeline;
import preprocessing.Preprocessing;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class main {

    public static void main(String[] args){
        Features features = new Features();

        ReviewData data = new ReviewData("txt_sentoken");
        //data.getPositiveData() is a list of 1000 positive reviews
        //data.getNegativeData() is a list of 1000 negative reviews
        String firstReview = data.getPositiveData().get(0);

        // Create an empty training set
        Instances trainingSet = new Instances("Rel", features.getAttributes(), 2000); // trainingSet with our features and a capacity of 1000 records
        trainingSet.setClassIndex(0); // the class attribute is the first one in the vector


        for(String review: data.getPositiveData()){
            Instance inst = new DenseInstance(features.getNumberOfFeatuers());
            features.setClass(inst, "positive");
            features.determineFeatureValues(inst, review); // for each feature it will do "setValue" on the instance
            trainingSet.add(inst);
        }

        for(String review: data.getNegativeData()){
            Instance inst = new DenseInstance(features.getNumberOfFeatuers());
            features.setClass(inst, "negative");
            features.determineFeatureValues(inst, review); // for each feature it will do "setValue" on the instance
            trainingSet.add(inst);
        }


        // -----------------------------------------------------------------------------------------------------------------
        // the following is just an example how use the preprocessing stuff within features and has nothing to do with the weka / classifier workflow
        // preprocessing example
        Pipeline<String, String> chain = Pipeline
                .start(Preprocessing.lowercase)
                .append(Preprocessing.stopwordFilter)
                .append(Preprocessing.tokenizer)
                .append(Preprocessing.stemmer);;
        System.out.println(chain.run(firstReview));

        // SentiWordNet example
        System.out.println("good#a "+SentiWordNet.getInstance().extract("good", "a"));
        System.out.println("bad#a "+SentiWordNet.getInstance().extract("bad", "a"));
        // -----------------------------------------------------------------------------------------------------------------

        // Create a naïve bayes classifier
        try {
            Classifier cModel = (Classifier) new NaiveBayes();
            cModel.buildClassifier(trainingSet);

            // Test the model
            Evaluation eTest = new Evaluation(trainingSet);
            eTest.evaluateModel(cModel, trainingSet);

            // print results
            String strSummary = eTest.toSummaryString();
            System.out.println(strSummary);

            //double[][] cmMatrix = eTest.confusionMatrix();

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


}
