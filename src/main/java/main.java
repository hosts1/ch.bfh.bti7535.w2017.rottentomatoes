// import weka.classifiers.bayes.NaiveBayes;

import preprocessing.Pipeline;
import preprocessing.Preprocessing;

public class main {

    public static void main(String[] args){
        ReviewData data = new ReviewData("txt_sentoken");
        //System.out.println(data.getPositiveData());
        //System.out.println(data.getNegativeData());
        String firstReview = data.getPositiveData().get(0);
        System.out.println(firstReview);
        System.out.println();


        Pipeline<String, String> chain = Pipeline
                .start(Preprocessing.lowercase)
                .append(Preprocessing.stopwordFilter)
                .append(Preprocessing.tokenizer)
                .append(Preprocessing.stemmer);
        String result = chain.run(firstReview);

        System.out.println(result);

    }


}
