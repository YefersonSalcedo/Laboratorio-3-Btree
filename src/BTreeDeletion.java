/**
 * Encapsula la lógica completa de eliminación en un árbol B.
 * Casos de eliminación cubiertos<
 * Caso 1 – Nodo hoja: La clave se encuentra en una hoja y se elimina
 *       directamente. No requiere reestructuración si el nodo no queda con underflow.
 * Caso 2a – Préstamo (rotación): Tras la eliminación, el nodo queda con
 *       menos claves de las permitidas (underflow). Si un hermano adyacente tiene claves
 *       de sobra, se realiza una rotación a través del padre:
 *         -Préstamo desde la izquierda: la clave separadora del padre baja al
 *                     hijo, y la mayor clave del hermano izquierdo sube al padre.
 *         -Préstamo desde la derecha: la clave separadora del padre baja al
 *                     hijo, y la menor clave del hermano derecho sube al padre.
 * Caso 2b – Fusión: Ningún hermano puede prestar claves. Se fusionan el
 *       nodo con underflow y un hermano adyacente, bajando la clave separadora del padre
 *       al nodo resultante. Esto puede propagar underflow hacia arriba en cascada.
 * Caso 3 – Nodo interno: La clave a eliminar está en un nodo interno.
 *       Se reemplaza por su predecesor en inorden (la mayor clave del subárbol
 *       izquierdo) y se elimina ese predecesor recursivamente en la hoja donde reside.
 * Reducción de altura
 * Si tras la eliminación la raíz queda vacía pero tiene un hijo, ese hijo se convierte
 * en la nueva raíz y el árbol reduce su altura en un nivel.
 */

public class BTreeDeletion {

    private final BTree tree;
    private final int order;

    /**
     * Construye una instancia de eliminación para el árbol y orden dados.
     *
     * @param tree  El árbol B sobre el que se realizará la eliminación.
     * @param order El orden del árbol.
     */
    public BTreeDeletion(BTree tree, int order) {
        this.tree = tree;
        this.order = order;
    }

    public void delete(String key) {
        if (tree.isEmpty()) {
            System.out.println("El árbol está vacío. No hay nada que eliminar.");
            return;
        }

        if (!tree.search(key)) {
            System.out.println("[DELETE] '" + key + "' no existe en el árbol.");
            return;
        }

        if (tree.root != null && tree.root.isLeaf) {
            int pos = tree.findPosition(tree.root, key);
            if (pos < tree.root.keys.size() && tree.root.keys.get(pos).equals(key)) {
                tree.root.keys.remove(pos);
                if (tree.root.keys.isEmpty()) {
                    tree.root = null;
                }
                System.out.println("[DELETE] '" + key + "' eliminado del árbol.");
                return;
            }
        }

        System.out.println("[DELETE] '" + key + "' no pudo eliminarse con la implementación actual.");
    }

    /**
     * Punto de entrada público para eliminar una clave del árbol.
     * Antes de eliminar verifica que el árbol no esté vacío y que la clave
     * efectivamente exista. Tras la eliminación recursiva, colapsa la raíz si
     * quedó sin claves (reducción de altura).
     *
     * @param key La clave a eliminar.
     */
    /*
    public void delete(String key) {
        if (tree.isEmpty()) {
            System.out.println("El árbol está vacío. No hay nada que eliminar.");
            return;
        }
        // Verifica existencia antes de intentar eliminar (evita descensos fallidos)
        if (!tree.search(key)) {
            System.out.println("[DELETE] '" + key + "' no existe en el árbol.");
            return;
        }

        tree.root = deleteRecursive(tree.root, key);

        // Reducción de altura: si la raíz quedó sin claves pero conserva un hijo,
        // ese hijo se convierte en la nueva raíz del árbol.
        if (tree.root != null && tree.root.keys.isEmpty() && !tree.root.isLeaf) {
            tree.root = tree.root.children.get(0);
        }
    }
     */

    /**
     * Núcleo recursivo de la eliminación. Desciende por el árbol buscando la clave
     * y aplica el caso de eliminación correspondiente según la posición donde se encuentre.
     * - Si el nodo actual es una hoja -> Caso 1 (eliminación directa).
     * - Si la clave está en un nodo interno -> Caso 3 (reemplazo con predecesor).
     * - Si la clave no está en el nodo actual -> desciende al hijo correspondiente.
     * Tras cada descenso recursivo se invoca fixUnderflow para reparar
     * cualquier violación de la propiedad mínima de claves.
     *
     * @param node El nodo actual del recorrido.
     * @param key  La clave que se desea eliminar.
     * @return El nodo (posiblemente modificado) tras la operación.
     */
    /*
    private Node deleteRecursive(Node node, String key) {
        // Ubica el índice donde está o debería estar la clave en este nodo
        int pos = tree.findPosition(node, key);
        boolean found = pos < node.keys.size() && node.keys.get(pos).equals(key);

        if (node.isLeaf) {
            // CASO 1: la clave está en un nodo hoja
            // Se elimina directamente. No es necesario reestructurar el árbol
            // si el nodo resultante conserva al menos el mínimo de claves.
            node.keys.remove(pos);
            int minKeys = (order - 1) / 2;
            if (node.keys.size() >= minKeys) {
                System.out.println("[CASO 1] '" + key + "' eliminado de nodo hoja sin underflow.");
            }
            return node;
        }

        if (found) {
            // CASO 3: la clave está en un nodo INTERNO
            // No se puede eliminar directamente (rompería la estructura del árbol).
            // Estrategia: reemplazar la clave con su PREDECESOR EN INORDEN,
            // es decir, la mayor clave del subárbol izquierdo (hijo en posición `pos`).
            // Luego se elimina ese predecesor de forma recursiva, lo que garantiza
            // que siempre se termina eliminando una clave de una hoja.
            String predecessor = getPredecessor(node, pos);
            System.out.println("[CASO 3] Eliminación de nodo interno. Reemplazo con predecesor '" + predecessor + "'.");
            node.keys.set(pos, predecessor);  // La clave objetivo es reemplazada por el predecesor
            node.children.set(pos, deleteRecursive(node.children.get(pos), predecessor));
            fixUnderflow(node, pos);          // El subárbol izquierdo puede haber quedado con underflow
        } else {
            // DESCENSO: la clave no está en este nodo
            // `pos` apunta al hijo donde debe continuar la búsqueda según el orden
            // de las claves del árbol B.
            node.children.set(pos, deleteRecursive(node.children.get(pos), key));
            fixUnderflow(node, pos);  // El hijo pudo haber quedado con underflow tras la recursión
        }

        return node;
    }
     */

    /**
     * Detecta y corrige el underflow en el hijo ubicado en childIndex
     * dentro del nodo padre dado.
     * Un nodo entra en underflow cuando tiene menos de ⌈(order-1)/2⌉ claves.
     * La corrección se intenta en este orden de prioridad:
     * - Préstamo desde el hermano izquierdo (Caso 2a): si existe y tiene
     *       claves de sobra, se rota una clave a través del padre hacia el hijo.
     * - Préstamo desde el hermano derecho(Caso 2a): análogo al anterior
     *       pero en dirección contraria.
     * - Fusión con un hermano (Caso 2b): si ningún hermano puede prestar,
     *       el hijo se fusiona con uno de sus hermanos y la clave separadora del padre
     *       desciende. Esto puede propagar underflow hacia arriba en cadena.
     *
     * @param parent     El nodo padre del hijo que puede tener underflow.
     * @param childIndex Índice del hijo afectado dentro de parent.children.
     */
    /*
    private void fixUnderflow(Node parent, int childIndex) {
        int minKeys = (order - 1) / 2;
        Node child = parent.children.get(childIndex);

        // Si el hijo tiene suficientes claves, no hay underflow -> no se hace nada
        if (child.keys.size() >= minKeys) return;

        // Intento 1: préstamo desde el hermano IZQUIERDO
        if (childIndex > 0) {
            Node leftSibling = parent.children.get(childIndex - 1);
            if (leftSibling.keys.size() > minKeys) {
                borrowFromLeft(parent, childIndex);
                System.out.println("[CASO 2a] Underflow detectado. Préstamo realizado desde hermano izquierdo.");
                return;
            }
        }

        // Intento 2: préstamo desde el hermano DERECHO
        if (childIndex < parent.children.size() - 1) {
            Node rightSibling = parent.children.get(childIndex + 1);
            if (rightSibling.keys.size() > minKeys) {
                borrowFromRight(parent, childIndex);
                System.out.println("[CASO 2a] Underflow detectado. Préstamo realizado desde hermano derecho.");
                return;
            }
        }

        // Intento 3: fusión con un hermano (ninguno pudo prestar)
        // Se prefiere fusionar con el hermano izquierdo si existe; de lo contrario,
        // se fusiona con el derecho. La clave separadora del padre desciende al nodo
        // fusionado, lo que puede provocar underflow en el padre (propagación en cascada).
        if (childIndex > 0) {
            merge(parent, childIndex - 1);  // Fusión: left = hijo[childIndex-1], right = child
        } else {
            merge(parent, childIndex);      // Fusión: left = child, right = hijo[childIndex+1]
        }
        System.out.println("[CASO 2b] Underflow resuelto por fusión con hermano.");
    }
     */

    // ==========================================================================
    //  MÉTODOS AUXILIARES
    // ==========================================================================

    /**
     * Obtiene el predecesor en inorden de la clave en la posición "pos".
     * El predecesor en inorden es la mayor clave del subárbol izquierdo
     * de la clave objetivo. Se obtiene descendiendo siempre por el hijo más a la
     * derecha de cada nodo hasta llegar a una hoja, y retornando su última clave.
     * Ejemplo con  un Árbol parcial:
     *         [M]
     *        /   \
     *    [D G]   ...
     *         \
     *        [K L]   <- predecesor de M es 'L'
     *
     *
     * @param node El nodo interno que contiene la clave a reemplazar.
     * @param pos  Índice de la clave en node.keys.
     * @return La clave predecesora.
     */
    /*
    private String getPredecessor(Node node, int pos) {
        Node current = node.children.get(pos);  // Raíz del subárbol izquierdo
        // Desciende siempre por el hijo más derecho hasta alcanzar una hoja
        while (!current.isLeaf) {
            current = current.children.get(current.children.size() - 1);
        }
        return current.keys.get(current.keys.size() - 1);  // Última (mayor) clave de la hoja
    }
     */

    /**
     * Realiza un préstamo desde el hermano IZQUIERDO hacia el hijo con underflow
     * mediante una rotación de claves a través del padre.
     * Pasos de la rotación:
     * 1- La clave separadora del padre (entre el hermano izquierdo y el hijo)
     *       desciende e ingresa como primera clave del hijo.
     * 2- La mayor clave del hermano izquierdo sube al padre, reemplazando
     *       al separador anterior.
     * 3- Si el hijo no es hoja, el hijo más derecho del hermano izquierdo
     *       se transfiere como primer hijo del nodo con underflow.
     *
     * @param parent      El nodo padre que contiene el separador.
     * @param childIndex  Índice del hijo con underflow dentro de parent.children.
     */
    /*
    private void borrowFromLeft(Node parent, int childIndex) {
        Node child = parent.children.get(childIndex);
        Node leftSibling = parent.children.get(childIndex - 1);

        // El separador del padre baja al inicio del hijo (rota hacia la derecha)
        child.keys.add(0, parent.keys.get(childIndex - 1));
        // La mayor clave del hermano izquierdo sube al padre como nuevo separador
        parent.keys.set(childIndex - 1, leftSibling.keys.remove(leftSibling.keys.size() - 1));

        // Transferencia del subárbol: el hijo más derecho del hermano pasa al hijo
        if (!child.isLeaf) {
            child.children.add(0, leftSibling.children.remove(leftSibling.children.size() - 1));
        }
    }
     */

    /**
     * Realiza un préstamo desde el hermano DERECHO hacia el hijo con underflow
     * mediante una rotación de claves a través del padre.
     * Pasos de la rotación:
     * 1- La clave separadora del padre (entre el hijo y el hermano derecho)
     *       desciende e ingresa como última clave del hijo.
     * 2- La menor clave del hermano derecho sube al padre, reemplazando
     *       al separador anterior.
     * 3- Si el hijo no es hoja, el hijo más izquierdo del hermano derecho
     *       se transfiere como último hijo del nodo con underflow.
     *
     * @param parent      El nodo padre que contiene el separador.
     * @param childIndex  Índice del hijo con underflow dentro de {@code parent.children}.
     */
    /*
    private void borrowFromRight(Node parent, int childIndex) {
        Node child = parent.children.get(childIndex);
        Node rightSibling = parent.children.get(childIndex + 1);

        // El separador del padre baja al final del hijo (rota hacia la izquierda)
        child.keys.add(parent.keys.get(childIndex));
        // La menor clave del hermano derecho sube al padre como nuevo separador
        parent.keys.set(childIndex, rightSibling.keys.remove(0));

        // Transferencia del subárbol: el hijo más izquierdo del hermano pasa al hijo
        if (!child.isLeaf) {
            child.children.add(rightSibling.children.remove(0));
        }
    }
     */

    /**
     * Fusiona el hijo derecho del separador con el hijo izquierdo, absorbiendo
     * la clave separadora del padre en el nodo resultante.
     * Pasos de la fusión:
     * 1- La clave separadora del padre sepIndex desciende y se
     *       agrega al final del nodo izquierdo.
     * 2- Todas las claves del nodo derecho se transfieren al nodo izquierdo.
     * 3- Si los nodos no son hojas, todos los hijos del nodo derecho también
     *       se transfieren al nodo izquierdo.
     * 4- El separador y el nodo derecho se eliminan del padre. Esto puede
     *       dejar al padre con underflow, propagando la corrección hacia arriba.
     * Ejemplo:
     *   Antes:  padre [..., sepKey, ...]
     *                       /         \
     *                   [left]       [right]
     *   ________________________________________________________________
     *   Después: padre [..., ...]          (sepKey eliminado del padre)
     *                      |
     *              [left + sepKey + right]  (nodo fusionado)
     *
     * @param parent   El nodo padre del que desciende el separador.
     * @param sepIndex Índice de la clave separadora dentro de parent.keys.
     */
    /*
    private void merge(Node parent, int sepIndex) {
        Node left = parent.children.get(sepIndex);
        Node right = parent.children.get(sepIndex + 1);

        // 1. El separador del padre desciende al nodo izquierdo
        left.keys.add(parent.keys.get(sepIndex));
        // 2. Todas las claves del nodo derecho migran al izquierdo
        left.keys.addAll(right.keys);

        // 3. Si no son hojas, los hijos del nodo derecho también migran
        if (!left.isLeaf) {
            left.children.addAll(right.children);
        }

        // 4. Se elimina el separador del padre y se descarta el nodo derecho
        parent.keys.remove(sepIndex);
        parent.children.remove(sepIndex + 1);  // El nodo derecho queda huérfano y será recolectado por el GC
    }

     */
}
