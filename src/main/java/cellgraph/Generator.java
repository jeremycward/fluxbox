package cellgraph;

@FunctionalInterface
public interface Generator<ContentType> {
    ContentType generate();
}
