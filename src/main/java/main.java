// import weka.classifiers.bayes.NaiveBayes;

import preprocessing.PreProcessor;

public class main {

    public static void main(String[] args){
        ReviewData data = new ReviewData("txt_sentoken");
        //System.out.println(data.getPositiveData());
        //System.out.println(data.getNegativeData());
        System.out.println(data.getPositiveData().get(0));

        System.out.println(PreProcessor.filterStopwords(data.getPositiveData().get(0)));

    }
}
