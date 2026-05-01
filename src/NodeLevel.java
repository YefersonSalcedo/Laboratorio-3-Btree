/**
 * Representa un par (nodo, nivel) utilizado durante el recorrido por niveles del árbol B.
 * Permite asociar cada nodo con su profundidad dentro del árbol para imprimirlos
 * agrupados por nivel.
 */
public class NodeLevel {
    Node node;
    int level;

    NodeLevel(Node node, int level) {
        this.node = node;
        this.level = level;
    }
}