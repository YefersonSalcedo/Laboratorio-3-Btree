import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BTree {

    int order;
    Node root;

  
    public BTree(int order) {
        this.order = order;
        this.root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

 
    public int findPosition(Node node, String key) {
        int i = 0;
        while (i < node.keys.size() && node.keys.get(i).compareTo(key) < 0) {
            i++;
        }
        return i;
    }

    public void insertSorted(List<String> list, String key) {
        int pos = 0;
        while (pos < list.size() && list.get(pos).compareTo(key) < 0) {
            pos++;
        }
        list.add(pos, key);
    }

    public SplitResult splitNode(Node node) {
        int mid = (node.keys.size() - 1) / 2;
        String promotedKey = node.keys.get(mid);

        Node left = new Node(node.isLeaf);
        Node right = new Node(node.isLeaf);

        // Claves a la izquierda del pivote -> nodo left
        for (int i = 0; i < mid; i++) {
            left.keys.add(node.keys.get(i));
        }
        // Claves a la derecha del pivote -> nodo right
        for (int i = mid + 1; i < node.keys.size(); i++) {
            right.keys.add(node.keys.get(i));
        }

        // Redistribuir hijos si el nodo no es hoja
        if (!node.isLeaf) {
            for (int i = 0; i <= mid; i++) {
                left.children.add(node.children.get(i));
            }
            for (int i = mid + 1; i < node.children.size(); i++) {
                right.children.add(node.children.get(i));
            }
        }

        return new SplitResult(promotedKey, left, right);
    }

    public InsertResult insert(String key) {
        if (root == null) {
            root = new Node(true);
            root.keys.add(key);
            return new InsertResult(true, false);
        }

        InsertState state = new InsertState();
        SplitResult split = insertRecursive(root, key, state);

        if (!state.inserted) {
            return new InsertResult(false, false);
        }

        if (split != null) {
            Node newRoot = new Node(false);
            newRoot.keys.add(split.promotedKey);
            newRoot.children.add(split.left);
            newRoot.children.add(split.right);
            root = newRoot;
            System.out.println("[SPLIT RAÍZ] Nueva raíz creada con clave: '" + split.promotedKey + "'");
        }

        return new InsertResult(true, state.hadOverflow);
    }

    private SplitResult insertRecursive(Node node, String key, InsertState state) {
        int pos = findPosition(node, key);

        if (pos < node.keys.size() && node.keys.get(pos).compareTo(key) == 0) {
            state.inserted = false;
            return null;
        }

        if (node.isLeaf) {
            insertSorted(node.keys, key);
            state.inserted = true;

            if (node.keys.size() >= order) {
                state.hadOverflow = true;
                SplitResult split = splitNode(node);
                System.out.println("[SPLIT] Nodo dividido. Clave promovida: '" + split.promotedKey + "'");
                return split;
            }

            return null;
        }

        SplitResult childSplit = insertRecursive(node.children.get(pos), key, state);

        if (!state.inserted) {
            return null;
        }

        if (childSplit != null) {
            node.keys.add(pos, childSplit.promotedKey);
            node.children.set(pos, childSplit.left);
            node.children.add(pos + 1, childSplit.right);

            if (node.keys.size() >= order) {
                state.hadOverflow = true;
                SplitResult split = splitNode(node);
                System.out.println("[SPLIT] Nodo dividido. Clave promovida: '" + split.promotedKey + "'");
                return split;
            }
        }

        return null;
    }

    public boolean search(String key) {
        if (root == null) {
            return false;
        }
        return searchRecursive(root, key);
    }

    private boolean searchRecursive(Node node, String key) {
        int pos = findPosition(node, key);

        if (pos < node.keys.size() && node.keys.get(pos).compareTo(key) == 0) {
            return true;
        }

        if (node.isLeaf) {
            return false;
        }

        return searchRecursive(node.children.get(pos), key);
    }

    /**
     * Elimina una clave del árbol B.
     * Delega la lógica de eliminación a BTreeDeletion.
     * Implementado por el Integrante 2.
     *
     * @param key La clave a eliminar.
     */
    public void delete(String key) {
        new BTreeDeletion(this, order).delete(key);
    }

    /**
     * Imprime el contenido del árbol nivel por nivel.
     * Utiliza una cola para procesar los nodos en orden de amplitud.
     * Cada vez que cambia el nivel actual, imprime un salto de línea y
     * etiqueta el nuevo nivel antes de continuar.
     * Ejemplo de salida para un árbol de orden 4:
     *   Nivel 0: [M]
     *   Nivel 1: [D G]  [P T X]
     *   Nivel 2: [A C]  [E F]  ...
     */
    public void printByLevels() {
        if (isEmpty()) {
            System.out.println("El árbol está vacío.");
            return;
        }
        Queue<NodeLevel> queue = new LinkedList<>();
        queue.add(new NodeLevel(root, 0));
        int currentLevel = -1;

        while (!queue.isEmpty()) {
            NodeLevel current = queue.poll();

            if (current.level != currentLevel) {
                if (currentLevel != -1) System.out.println();
                currentLevel = current.level;
                System.out.print("Nivel " + currentLevel + ": ");
            }

            System.out.print(current.node + "  ");

            if (!current.node.isLeaf) {
                for (Node child : current.node.children) {
                    queue.add(new NodeLevel(child, current.level + 1));
                }
            }
        }
        System.out.println();
    }
}
