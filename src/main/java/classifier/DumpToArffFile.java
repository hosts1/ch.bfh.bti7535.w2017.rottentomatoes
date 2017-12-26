package classifier;

import pipeline.Pipe;
import weka.core.converters.ArffSaver;

import java.io.File;
import java.io.IOException;

/**
 * Created by hk on 26.12.2017.
 */
public class DumpToArffFile implements Pipe<ClassifierArguments, ClassifierArguments> {

    @Override
    public ClassifierArguments process(ClassifierArguments input) {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(input.instances);
        try {
            saver.setFile(new File("data/imdb_optimized_attributes.arff"));
            saver.writeBatch();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return input;

    }
}
