package features;


import weka.core.Attribute;

import java.util.function.Function;

public class UnigramFeature extends Feature<Double> {
    public UnigramFeature(String name){
        super(name, null);
        this.attr = new Attribute(name);
    }

}
