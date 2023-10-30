package cellgraph;

import cellgraph.mutations.CellMutation;

import java.util.Objects;
import java.util.Optional;
import java.util.Stack;
import java.util.UUID;

abstract public class BaseCellImpl<T> implements Cell<T> {
    private Class<T> contentType;
    private Stack<T> generations= new Stack<T>();
    private final String id;
    public BaseCellImpl(Class<T> type, String id) {
        this.id = id;
        this.contentType = getContentType();
    }

    @Override
    public Class<T> getContentType() {
        return contentType;
    }

    @Override
    public String getCaption() {
        return getId();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Stack<T> generations() {
        return generations;
    }
    public static String generateUniqueId(String nickName){
        return nickName + UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseCellImpl<?> baseCell = (BaseCellImpl<?>) o;
        return id.equals(baseCell.id);
    }
    protected final CellMutation regenerate(T newValue ){
        Optional<T> oldVal = generations.isEmpty() ? Optional.empty() : Optional.of(generations.peek());
        generations.push(newValue);
        return new CellMutation(this.id,oldVal,newValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
