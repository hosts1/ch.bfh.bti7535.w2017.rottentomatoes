package preprocessing;

import utils.Pipe;

public class Lowercase implements Pipe<String, String> {
    @Override
    public String process(String text){
        return text.toLowerCase();
    }

}
