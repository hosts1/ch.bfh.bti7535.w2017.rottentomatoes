package preprocessing;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import utils.Pair;
import pipeline.Pipe;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.stream.Collectors.toList;

public class MaxEntPosTagger implements Pipe<List<String>, Pair<List<String>, List<String>>> {
    POSTaggerME tagger = null;
    POSModel model = null;
    ConcurrentMap<Integer, Pair<List<String>, List<String>>> cache = new ConcurrentHashMap();

    public MaxEntPosTagger(){
        initialize("/en-pos-maxent.bin");
    }

    public void initialize(String lexiconFileName) {
        try {
            InputStream modelStream =  getClass().getResourceAsStream(lexiconFileName);
            model = new POSModel(modelStream);
            tagger = new POSTaggerME(model);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<String> mapToSentiWordNet(List<String> in){
        return in.stream().map(s -> {
            if (s.contains("NN") || s.contains("NNS")
                    || s.contains("NNP")
                    || s.contains("NNPS"))
                return "n";
            else if (s.contains("VB") || s.contains("VBD")
                    || s.contains("VBG") || s.contains("VBN")
                    || s.contains("VBP") || s.contains("VBZ"))
                return "v";
            else if (s.contains("JJ") || s.contains("JJR")
                    || s.contains("JJS"))
                return "a";
            else if (s.contains("RB") || s.contains("RBR")
                    || s.contains("RBS"))
                return "r";
            else
                return null;
        }).collect(toList());

    }

    public List<String> tag(List<String> text){
        try {
            if (model != null) {
                POSTaggerME tagger = new POSTaggerME(model);
                String[] ar = text.toArray(new String[0]);
                String[] tags = tagger.tag(ar);
                List<String> lst = Arrays.asList(tags);
                return lst;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  Arrays.asList();
    }

    @Override
    public Pair<List<String>, List<String>> process(List<String> input) {
        // return a pair of lists of words and the posTags since this is required by the sentiWordNet
        if(this.cache.containsKey(input.hashCode()))
            return this.cache.get(input.hashCode());

        Pair<List<String>, List<String>> res = new Pair(input, mapToSentiWordNet(tag(input)));
        this.cache.put(input.hashCode(), res);

        return res;
    }
}
