import java.util.ArrayList;
import java.util.List;

public class Node {

    List<String> keys;
    List<Node> children;
    boolean isLeaf;

   
    Node(boolean isLeaf) {
        this.isLeaf = isLeaf;
        this.keys = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < keys.size(); i++) {
            sb.append(keys.get(i));
            if (i < keys.size() - 1) sb.append(" | ");
        }
        sb.append("]");
        return sb.toString();
    }
}
