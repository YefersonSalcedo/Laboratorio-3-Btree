import java.util.Scanner;

public class Main {

    public static int askOrder(Scanner scanner) {
        int order = -1;
        while (order < 4 || order > 9) {
            System.out.print("Ingrese el orden del árbol B (4-9): ");
            String input = scanner.nextLine().trim();
            try {
                order = Integer.parseInt(input);
                if (order < 4 || order > 9) {
                    System.out.println("El orden debe estar entre 4 y 9.");
                    order = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero válido.");
            }
        }
        return order;
    }

    
    public static String readInput(Scanner scanner, String prompt) {
        String input = "";
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("El nombre no puede estar vacío.");
            } else if (input.length() > 30) {
                System.out.println("El nombre no puede superar 30 caracteres.");
            } else {
                break;
            }
        }
        return input;
    }

    public static void showMenu() {
        System.out.println("\n===== ÁRBOL B =====");
        System.out.println("1. Insertar nombre");
        System.out.println("2. Eliminar nombre");
        System.out.println("3. Buscar nombre");
        System.out.println("4. Mostrar árbol por niveles");
        System.out.println("5. Salir");
        System.out.println("===================");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Paso 1: configurar el orden del árbol
        int order = askOrder(scanner);
        BTree tree = new BTree(order);
        System.out.println("Árbol B de orden " + order + " creado correctamente.\n");

        // Paso 2: bucle principal do-while con switch de 5 casos
        int option;
        do {
            showMenu();
            System.out.print("Seleccione una opción: ");
            String raw = scanner.nextLine().trim();

            try {
                option = Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entre 1 y 5.");
                option = -1;
                continue;
            }

            switch (option) {
                case 1: {
                    // Insertar
                    String name = readInput(scanner, "Ingrese el nombre a insertar: ");
                    tree.insert(name);
                    System.out.println("'" + name + "' insertado.");
                    break;
                }
                case 2: {
                    // Eliminar
                    String name = readInput(scanner, "Ingrese el nombre a eliminar: ");
                    tree.delete(name);
                    break;
                }
                case 3: {
                    // Buscar
                    String name = readInput(scanner, "Ingrese el nombre a buscar: ");
                    boolean found = tree.search(name);
                    if (found) {
                        System.out.println("'" + name + "' SÍ se encuentra en el árbol.");
                    } else {
                        System.out.println("'" + name + "' NO se encuentra en el árbol.");
                    }
                    break;
                }
                case 4: {
                    // Mostrar árbol por niveles
                    tree.printByLevels();
                    break;
                }
                case 5: {
                    // Salir
                    System.out.println("Saliendo del programa. ¡Hasta luego!");
                    break;
                }
                default: {
                    System.out.println("Opción inválida. Intente de nuevo.");
                    break;
                }
            }

        } while (option != 5);

        scanner.close();
    }
}
