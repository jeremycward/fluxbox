package cellgraph;


import java.util.Stack;

public interface Cell<T> {
    String getCaption();
    String getId();
    Stack<T> generations();
    Action process(Action input);

    Class<T> getContentType();

}
