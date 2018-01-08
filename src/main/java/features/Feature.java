package features;

import weka.core.FastVector;
import weka.core.Attribute;

import java.util.ArrayList;
import java.util.function.Function;


public abstract class Feature<T> {
   public Attribute attr;
   public String name;
   public Function<String, T> fxDetermine;        // this function will calculate the value of a feature

   public Feature(String name, Function<String, T> fx){
       this.name = name;
       this.fxDetermine = fx;
   }

    public void addToFeatureVector(ArrayList vec){
        vec.add(this);
    }
    public T determineValue(String review){
        return fxDetermine.apply(review);
    }
}
