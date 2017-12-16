package preprocessing;

/**
 * Created by hk on 16.12.2017.
 */
public interface Pipe<T, U> {
    public U process(T input);
}