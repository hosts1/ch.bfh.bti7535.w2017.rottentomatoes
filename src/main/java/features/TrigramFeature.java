package features;


import weka.core.Attribute;

public class TrigramFeature extends Feature<Double> {
    public TrigramFeature(String name){
        super(name, null);
        this.attr = new Attribute(name);
    }

}
