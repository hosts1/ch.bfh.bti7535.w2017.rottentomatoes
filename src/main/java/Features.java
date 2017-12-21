import features.Feature;
import features.NominalFeature;
import features.NummericFeature;
import weka.core.FastVector;
import weka.core.Instance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hk on 21.12.2017.
 */
public class Features {
    public static final String classifierName = "THECLASSIFIER";
    public List<Feature> features = new ArrayList<Feature>();

    public Features(){
        Feature classifierFeature = new NominalFeature(classifierName, new ArrayList<String>(Arrays.asList("positive", "negative")),
                (review) -> { return ""; }
                );
        features.add(classifierFeature);

        Feature reviewLengthFeature = new NummericFeature("reviewLengthFeature",
                (review) -> { return (double)review.length(); }
        );
        features.add(reviewLengthFeature);

    }

    public ArrayList getAttributes(){
        ArrayList featureAttributes = new ArrayList(features.size());
        for(Feature feature: this.features){
            featureAttributes.add(feature.attr);
        }
        return featureAttributes;
    }

    public void determineFeatureValues(Instance inst, String review){
        for(Feature feature: this.features){
            if(feature.name == classifierName)      // skip the classifierFeature
                continue;

            if(feature instanceof NummericFeature)
                inst.setValue(feature.attr, (Double)feature.determineValue(review));
            else
                inst.setValue(feature.attr, (String)feature.determineValue(review));

            System.out.println(inst.value(feature.attr));

        }
    }

    public void setClass(Instance inst, String classValue){
        inst.setValue(this.features.get(0).attr, classValue);
    }

    public int getNumberOfFeatuers(){
        return this.features.size();
    }

}
