package features;


import weka.core.Attribute;

import java.util.function.Function;

public class BagOfWordFeature extends Feature<Double> {
    public BagOfWordFeature(String name){
        super(name, null);
        this.attr = new Attribute(name);
    }

}
