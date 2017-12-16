import java.util.StringTokenizer;

public class PreProcessor {

    public static StringTokenizer tokenize(String input) {
        StringTokenizer stok = new StringTokenizer(input, " ");
        return stok;
    }

    public static String toLowerCase(String input) {
        return input.toLowerCase();
    }

    public static String filterStopwords(String input) {
        return StopwordFilter.filter(input);
    }

    public static String stem(String input){
        PorterStemmer porterStemmer = new PorterStemmer();
        String stem = porterStemmer.stemWord("incorporated"); // returns "incorpor"
    }
}
