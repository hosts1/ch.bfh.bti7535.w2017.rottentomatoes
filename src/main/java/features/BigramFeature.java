package features;


import weka.core.Attribute;

public class BigramFeature extends Feature<Double> {
    public BigramFeature(String name){
        super(name, null);
        this.attr = new Attribute(name);
    }

}
