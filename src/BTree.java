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

    // =========================================================================
    //  MÉTODOS DEL INTEGRANTE 2 – stubs para compilación
    // =========================================================================

    /**
     * Inserta una clave en el árbol B.
     * Implementado por el Integrante 2.
     *
     * @param key La clave a insertar.
     */
    public void insert(String key) {
        // Implementación a cargo del Integrante 2
    }

    /**
     * Busca una clave en el árbol B.
     * Implementado por el Integrante 2.
     *
     * @param key La clave a buscar.
     * @return true si la clave existe en el árbol, false en caso contrario.
     */
    public boolean search(String key) {
        // Implementación a cargo del Integrante 2
        return false;
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
