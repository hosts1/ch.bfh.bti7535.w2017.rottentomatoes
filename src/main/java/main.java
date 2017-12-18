// import weka.classifiers.bayes.NaiveBayes;

import utils.Pipeline;
import preprocessing.Preprocessing;

public class main {

    public static void main(String[] args){
        ReviewData data = new ReviewData("txt_sentoken");
        //data.getPositiveData() is a list of 500 positive reviews
        //data.getNegativeData() is a list of 500 negative reviews
        String firstReview = data.getPositiveData().get(0);
        System.out.println(firstReview);
        System.out.println();

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


    }


}
