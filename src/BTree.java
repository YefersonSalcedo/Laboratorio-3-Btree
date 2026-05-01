public class BTree {


    /**
     * Elimina la clave especificada del árbol B.
     * Delega la lógica de eliminación a {BTreeDeletion}, que maneja
     * internamente los casos de underflow, préstamos y fusiones de nodos.
     *
     * @param key La clave a eliminar.
     */
    /*
    public void delete(String key) {
        new BTreeDeletion(this, order).delete(key);
    }
    */

    /**
     * Imprime el contenido del árbol nivel por nivel.
     * Utiliza una cola para procesar los nodos en orden de amplitud.
     * Cada vez que cambia el nivel actual, imprime un salto de línea y
     * etiqueta el nuevo nivel antes de continuar
     * Ejemplo de salida para un árbol de orden 3:
     *   Nivel 0: [M]
     *   Nivel 1: [D G]  [P T X]
     *   Nivel 2: [A C]  [E F]  ...
     */
    /*
    public void printByLevels() {
        if (isEmpty()) {
            System.out.println("El árbol está vacío.");
            return;
        }

        Queue<NodeLevel> queue = new LinkedList<>();
        queue.add(new NodeLevel(root, 0));
        int currentLevel = -1;  // Centinela: indica que aún no se ha imprimido ningún nivel

        while (!queue.isEmpty()) {
            NodeLevel current = queue.poll();

            // Detecta cambio de nivel para imprimir la etiqueta correspondiente
            if (current.level != currentLevel) {
                if (currentLevel != -1) System.out.println();  // Salto de línea entre niveles
                currentLevel = current.level;
                System.out.print("Nivel " + currentLevel + ": ");
            }

            System.out.print(current.node + "  ");

            // Encola los hijos del nodo actual para continuar el recorrido
            if (!current.node.isLeaf) {
                for (Node child : current.node.children) {
                    queue.add(new NodeLevel(child, current.level + 1));
                }
            }
        }
        System.out.println();
    }
    */
}