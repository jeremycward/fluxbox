package cellgraph;



public class InputCell extends BaseCellImpl<Integer> {
    public static final String BASE_CELL_ID = java.util.UUID.randomUUID().toString();

    @Override
    public String getCaption() {
        return "INPUT";
    }

    @Override
    public Action process(Action input) {
        return input;
    }

    public InputCell() {
        super(Integer.class, BASE_CELL_ID);
    }

}
