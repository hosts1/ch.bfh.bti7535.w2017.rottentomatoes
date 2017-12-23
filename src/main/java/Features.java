import com.sun.deploy.util.StringUtils;
import features.*;
import pipeline.Pipeline;
import preprocessing.Preprocessing;
import sentimentAnalysis.SentiAnalysis;
import sentimentAnalysis.SentiWordNet;
import weka.Run;
import weka.core.Instance;

import java.util.*;

/**
 * Created by hk on 21.12.2017.
 */
public class Features {
    public static final String classifierName = "#";        // the name of the feature that is used for classification

    public List<Feature> features = new ArrayList<Feature>();

    public Features(){
        // ********************
        // add features here
        // ********************

        // Classifier Feature (the review is "positive" or "negative")
        features.add(new NominalFeature(classifierName, new ArrayList<String>(Arrays.asList("positive", "negative")), (review) -> { return ""; }));

        // Review-Length-Feature
        features.add(new NummericFeature("reviewLengthFeature",
                (review) -> { return (double)review.length(); }
        ));

        // Review-polarity
        features.add(new NummericFeature("reviewPolarity",
                (review) -> {
                    Pipeline<String, Double> chain = Pipeline
                            .start(Preprocessing.tokenizer)
                            .append(Preprocessing.maxEntPosTagger)
                            .append(SentiAnalysis.textPolarity);
                    return chain.run(review);
                }
        ));

        // Review-purity
        features.add(new NummericFeature("reviewPurity",
                (review) -> {
                    Pipeline<String, Double> chain = Pipeline
                            .start(Preprocessing.tokenizer)
                            .append(Preprocessing.maxEntPosTagger)
                            .append(SentiAnalysis.textPurity);
                    return chain.run(review);
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
    public void determineFeatureValues(Instance inst, String review, HashMap<String,Integer> unigramVector, HashMap<String,Integer> bigramVector, HashMap<String,Integer> trigramVector ){
        for(Feature feature: this.features){
            if(feature.name == classifierName)      // skip the classifierFeature
                continue;

            if(feature instanceof NummericFeature)
                inst.setValue(feature.attr, (Double)feature.determineValue(review));
            else if(feature instanceof NominalFeature)
                inst.setValue(feature.attr, (String)feature.determineValue(review));
            else if(feature instanceof UnigramFeature){
                inst.setValue(feature.attr, unigramVector.get(feature.name));
            }else if(feature instanceof BigramFeature){
                inst.setValue(feature.attr, bigramVector.get(feature.name));
            }else if(feature instanceof TrigramFeature){
                inst.setValue(feature.attr, trigramVector.get(feature.name));
            }else{
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
