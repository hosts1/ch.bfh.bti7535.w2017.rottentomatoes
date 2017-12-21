package features;

import weka.core.FastVector;
import weka.core.Attribute;

import java.util.ArrayList;
import java.util.function.Function;


public abstract class Feature<T> {
   public Attribute attr;
   public String name;
   private Function<String, T> detValue;        // this function will calculate the value of a feature

   public Feature(String name, Function<String, T> fx){
       this.name = name;
       this.detValue = fx;
   }

    public void addToFeatureVector(ArrayList fvWekaAttributes){
        fvWekaAttributes.add(this);
    }

    public T determineValue(String review){
        return detValue.apply(review);
    }
}
