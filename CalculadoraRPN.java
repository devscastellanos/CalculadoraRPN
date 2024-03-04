import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

public class CalculadoraRPN {

    static class Nodo {
        String valor;
        Nodo izquierdo, derecho;

        Nodo(String valor) {
            this.valor = valor;
            izquierdo = derecho = null;
        }
    }

    public static void main(String[] args) {
        Stack<Double> pila = new Stack<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Por favor, ingrese 'fin' para terminar el programa.");
        System.out.println("Ingrese un término y luego un Enter");

        String expresion = "";
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals("fin")) {
                break; // Salir del bucle si se ingresa 'fin'.
            }
            expresion += input + " ";

            try {
                if (esOperador(input)) {
                    double right = pila.pop();
                    double left = pila.pop();
                    double d = evaluarOperador(input, left, right);
                    pila.push(d);
                    System.out.println(d);
                } else {
                    double d = Double.parseDouble(input);
                    pila.push(d);
                }
            } catch (NoSuchElementException | ArithmeticException | NumberFormatException e) {
                System.out.println("Error: Operador requerido pero la pila está vacía, se intentó dividir por cero, o la entrada no es un número");
            }

            Nodo raiz = generarArbol(expresion);
            mostrarArbol(raiz, 0);
            System.out.println("Resultado: " + calcularExpresion(raiz));
        }
        scanner.close();
    }

    public static Nodo generarArbol(String expresion) {
        String[] tokens = expresion.split(" ");
        Stack<Nodo> pila = new Stack<>();

        for (String token : tokens) {
            if (esOperador(token)) {
                Nodo derecho = pila.pop();
                Nodo izquierdo = pila.pop();
                Nodo nodo = new Nodo(token);
                nodo.izquierdo = izquierdo;
                nodo.derecho = derecho;
                pila.push(nodo);
            } else {
                pila.push(new Nodo(token));
            }
        }

        return pila.pop();
    }

    private static boolean esOperador(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    private static void mostrarArbol(Nodo nodo, int nivel) {
        if (nodo != null) {
            mostrarArbol(nodo.derecho, nivel + 1);
            for (int i = 0; i < nivel; i++) {
                System.out.print("\t");
            }
            System.out.println(nodo.valor);
            mostrarArbol(nodo.izquierdo, nivel + 1);
        }
    }

    private static double calcularExpresion(Nodo nodo) {
        if (nodo != null) {
            if (esOperador(nodo.valor)) {
                double izquierdo = calcularExpresion(nodo.izquierdo);
                double derecho = calcularExpresion(nodo.derecho);
                return evaluarOperador(nodo.valor, izquierdo, derecho);
            } else {
                return Double.parseDouble(nodo.valor);
            }
        }
        return 0.0;
    }

    private static double evaluarOperador(String operador, double izquierdo, double derecho) {
        switch (operador) {
            case "+":
                return izquierdo + derecho;
            case "-":
                return izquierdo - derecho;
            case "*":
                return izquierdo * derecho;
            case "/":
                return izquierdo / derecho;
            default:
                return 0.0;
        }
    }
}
