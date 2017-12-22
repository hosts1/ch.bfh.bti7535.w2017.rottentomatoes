package features;


import weka.core.Attribute;

import java.util.function.Function;

public class NummericWordFeature extends Feature<Double> {
    public NummericWordFeature(String name){
        super(name, null);
        this.attr = new Attribute(name);
    }

}
