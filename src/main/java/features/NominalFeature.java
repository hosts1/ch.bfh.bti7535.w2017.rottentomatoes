package features;


import weka.core.Attribute;
import weka.core.FastVector;

import java.util.ArrayList;
import java.util.function.Function;

public class NominalFeature extends Feature<String> {
    public NominalFeature(String name, ArrayList attributeValues, Function<String, String> fx){
        super(name, fx);
        this.attr = new Attribute(name, attributeValues);
    }

}
