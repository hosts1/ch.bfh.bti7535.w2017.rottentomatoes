package features;

import pipeline.PipelineFactory;
import preprocessing.Preprocessing;
import sentimentAnalysis.SentiAnalysis;
import weka.core.Instance;

import java.util.*;

/**
 * Created by hk on 21.12.2017.
 */
public class Features {
    public static final String classifierName = "#";        // the name of the feature that is used for classification

    public List<Feature> features = new ArrayList<Feature>();

    public Features(){
        // Classifier Feature (the review is "positive" or "negative")
        features.add(new NominalFeature(classifierName, new ArrayList<String>(Arrays.asList("positive", "negative")), (review) -> { return ""; }));

        // ********************
        // add features here
        // ********************

        // Review-Length-Feature
        this.addFeature(new NummericFeature("reviewLengthFeature",
                (review) -> {
                    return (double) review.length();
                }
        ));

        // Review-polarity
        this.addFeature(new NummericFeature("reviewPolarity",
                (review) -> {
                    return PipelineFactory
                            .start(Preprocessing.negationFilter)
                            .append(Preprocessing.tokenizer)
                            .append(Preprocessing.maxEntPosTagger)
                            .append(SentiAnalysis.textPolarity)
                            .run(review);
                }
        ));

        // Review-purity
        this.addFeature(new NummericFeature("reviewPurity",
                (review) -> {
                    return PipelineFactory
                            .start(Preprocessing.negationFilter)
                            .append(Preprocessing.tokenizer)
                            .append(Preprocessing.maxEntPosTagger)
                            .append(SentiAnalysis.textPurity)
                            .run(review);
                }
        ));

        // count positive words
        this.addFeature(new NummericFeature("countPositiveWords",
                (review) -> {
                    return PipelineFactory
                            .start(Preprocessing.negationFilter)
                            .append(Preprocessing.tokenizer)
                            .append(Preprocessing.maxEntPosTagger)
                            .append(SentiAnalysis.countPositiveWords)
                            .run(review);
                }
        ));

        // count negative words
        this.addFeature(new NummericFeature("countNegativeWords",
                (review) -> {
                    return PipelineFactory
                            .start(Preprocessing.negationFilter)
                            .append(Preprocessing.tokenizer)
                            .append(Preprocessing.maxEntPosTagger)
                            .append(SentiAnalysis.countNegativeWords)
                            .run(review);
                }
        ));

    }
    public Feature addFeature(Feature feature){
        this.features.add(feature); return feature;
    }

    // returns the Attribute Objects of all features (needed for weka stuff)
    public ArrayList getAttributes(){
        ArrayList featureAttributes = new ArrayList(features.size());
        for(Feature feature: this.features){
            featureAttributes.add(feature.attr);
        }
        return featureAttributes;
    }

    // Determines the value of all features from a review (except the classifier feature)
    public void determineFeatureValues(Instance inst, String review, HashMap<String,Integer> wordVector){
        for(Feature feature: this.features){
            if(feature.name == classifierName)      // skip the classifierFeature
                continue;

            if(feature instanceof NummericFeature)
                inst.setValue(feature.attr, (Double)feature.determineValue(review));
            else if(feature instanceof NominalFeature)
                inst.setValue(feature.attr, (String)feature.determineValue(review));
            else if(feature instanceof BagOfWordFeature) {
                inst.setValue(feature.attr, wordVector.get(feature.name));
            }
            else{
                throw new RuntimeException("Unknown Feature Class");
            }

        }
    }

    // sets the classifier feature to some value (since it shouldn't be determines automatically for training data)
    public void setClass(Instance inst, String classValue){
        inst.setValue(this.features.get(0).attr, classValue);
    }

    // return the number of features (required by some weka stuff)
    public int getNumberOfFeatures(){
        return this.features.size();
    }

}
