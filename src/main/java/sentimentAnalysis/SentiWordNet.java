package sentimentAnalysis;

import utils.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SentiWordNet{
    ConcurrentMap<Integer, List<Double>> cache = new ConcurrentHashMap();

    private Map<String, Double> dictionary;

    protected SentiWordNet(String pathToSWN) {
        // This is our main dictionary representation
        dictionary = new HashMap<String, Double>();

        // From String to list of doubles.
        HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();

        BufferedReader csv = null;
        try {
            //Get file from resources folder
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(pathToSWN).getFile());
            csv = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            int lineNumber = 0;

            String line;
            while ((line = csv.readLine()) != null) {
                lineNumber++;

                // If it's a comment, skip this line.
                if (!line.trim().startsWith("#")) {
                    // We use tab separation
                    String[] data = line.split("\t");
                    String wordTypeMarker = data[0];

                    // Example line:
                    // POS ID PosS NegS SynsetTerm#sensenumber Desc
                    // a 00009618 0.5 0.25 spartan#4 austere#3 ascetical#2
                    // ascetic#2 practicing great self-denial;...etc

                    // Is it a valid line? Otherwise, through exception.
                    if (data.length != 6) {
                        throw new IllegalArgumentException(
                                "Incorrect tabulation format in file, line: "
                                        + lineNumber);
                    }

                    // Calculate synset score as score = PosS - NegS
                    Double synsetScore = Double.parseDouble(data[2])
                            - Double.parseDouble(data[3]);

                    // Get all Synset terms
                    String[] synTermsSplit = data[4].split(" ");

                    // Go through all terms of current synset.
                    for (String synTermSplit : synTermsSplit) {
                        // Get synterm and synterm rank
                        String[] synTermAndRank = synTermSplit.split("#");
                        String synTerm = synTermAndRank[0] + "#"
                                + wordTypeMarker;

                        int synTermRank = Integer.parseInt(synTermAndRank[1]);
                        // What we get here is a map of the type:
                        // term -> {score of synset#1, score of synset#2...}

                        // Add map to term if it doesn't have one
                        if (!tempDictionary.containsKey(synTerm)) {
                            tempDictionary.put(synTerm,
                                    new HashMap<Integer, Double>());
                        }

                        // Add synset link to synterm
                        tempDictionary.get(synTerm).put(synTermRank,
                                synsetScore);
                    }
                }
            }

            // Go through all the terms.
            for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary
                    .entrySet()) {
                String word = entry.getKey();
                Map<Integer, Double> synSetScoreMap = entry.getValue();

                // Calculate weighted average. Weigh the synsets according to
                // their rank.
                // Score= 1/2*first + 1/3*second + 1/4*third ..... etc.
                // Sum = 1/1 + 1/2 + 1/3 ...
                double score = 0.0;
                double sum = 0.0;
                for (Map.Entry<Integer, Double> setScore : synSetScoreMap
                        .entrySet()) {
                    score += setScore.getValue() / (double) setScore.getKey();
                    sum += 1.0 / (double) setScore.getKey();
                }
                score /= sum;

                dictionary.put(word, score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (csv != null) {
                try {
                    csv.close();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    public double extract(String word, String pos) {
        try {
            return dictionary.get(word + "#" + pos);
        }catch(Exception ex){
            return 0.0;
        }
    }

    public List<Double> getTextPolarity(Pair<List<String>, List<String>> input){
        if(this.cache.containsKey(input.hashCode()))
            return this.cache.get(input.hashCode());

        List<String> words = input.getFirst();
        List<String> posTags = input.getSecond();

        List<Double> pol = new ArrayList<Double>();

        int index = 0;
        for(String s: words){
            pol.add(extract(s, posTags.get(index)));
            index += 1;
        }

        // try to fix negations
        index = 0;
        List<String> negations = new ArrayList<>(Arrays.asList("not", "don't", "didn't", "n't", "no"));

        for(String word: input.getFirst()){
            for(String neg: negations){
                if(word.equals(neg) && pol.size()-1 >= index+1){
                    pol.set(index+1, -pol.get(index+1));
                }
            }
            index += 1;
        }

        this.cache.put(input.hashCode(), pol);
        return pol;
    }



}
