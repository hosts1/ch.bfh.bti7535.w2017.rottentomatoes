package features;


import weka.core.Attribute;
import weka.core.FastVector;

import java.util.function.Function;

public class NummericFeature extends Feature<Double> {
    public NummericFeature(String name, Function<String, Double> fx){
        super(name, fx);
        this.attr = new Attribute(name);
    }

}
