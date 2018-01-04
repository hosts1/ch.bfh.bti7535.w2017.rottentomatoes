package classifier;

import pipeline.Pipe;
import weka.core.converters.ArffSaver;

import java.io.File;
import java.io.IOException;

/**
 * Created by hk on 26.12.2017.
 */
public class DumpToArffFile implements Pipe<ClassifierArguments, ClassifierArguments> {

    static int c = 0;

    @Override
    public ClassifierArguments process(ClassifierArguments input) {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(input.testInstances);
        try {
            saver.setFile(new File("data/attributeDump_"+ input.k+".arff"));
            saver.writeBatch();

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.c += 1;
        return input;
    }
}
