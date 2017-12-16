package preprocessing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pipeline<S, T> {
    private List<Pipe<?, ?>> pipes;

    private Pipeline() {
    }

    public static <K, L> Pipeline<K, L> start(Pipe<K, L> pipe) {
        Pipeline<K, L> chain = new Pipeline<K, L>();
        chain.pipes = Collections.<Pipe<?, ?>>singletonList(pipe);;
        return chain;
    }

    public <V> Pipeline<S, V> append(Pipe<T, V> pipe) {
        Pipeline<S, V> chain = new Pipeline<S, V>();
        chain.pipes = new ArrayList<Pipe<?, ?>>(pipes);
        chain.pipes.add(pipe);
        return chain;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public T run(S s) {
        Object source = s;
        Object target = null;
        for (Pipe p : pipes) {
            target = p.process(source);
            source = target;
        }
        return (T) target;
    }


}