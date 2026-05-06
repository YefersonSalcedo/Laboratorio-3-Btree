public class InsertResult {
    boolean overflow = false;
    SplitResult splitResult = null;

    /** Resultado sin overflow. */
    InsertResult() {}

    /** Resultado con overflow: se debe propagar la división. */
    InsertResult(SplitResult splitResult) {
        this.overflow = true;
        this.splitResult = splitResult;
    }
}
