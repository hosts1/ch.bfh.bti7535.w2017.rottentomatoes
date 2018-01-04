package pipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Used to build reusable pre-processing chains.
// Note: Currently, only one argument is allowed --> use class containers like ClassifierArguments

public class PipelineFactory<S, T> {
    private List<Pipe<?, ?>> pipes;

    private PipelineFactory() {
    }

    public static <K, L> PipelineFactory<K, L> start(Pipe<K, L> pipe) {
        PipelineFactory<K, L> chain = new PipelineFactory<K, L>();
        chain.pipes = Collections.<Pipe<?, ?>>singletonList(pipe);;
        return chain;
    }

    public <V> PipelineFactory<S, V> append(Pipe<T, V> pipe) {
        PipelineFactory<S, V> chain = new PipelineFactory<S, V>();
        chain.pipes = new ArrayList<Pipe<?, ?>>(pipes);
        chain.pipes.add(pipe);
        return chain;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public T run(S s) {
        Object source = s;
        Object target = null;
        for (Pipe p : pipes) {
            try {
                target = p.process(source);
            } catch (Exception e) {
                e.printStackTrace();
            }
            source = target;
        }
        return (T) target;
    }


}