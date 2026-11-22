package MASAMORRA.logica;

import java.util.ArrayList;

/**
 * Aquí se construyen las máquinas ya armadas, con sus cadenas de ejemplo.
 * La interfaz y las pruebas solo llaman a estos métodos.
 */
public class Ejemplos {

    // Agrega a la máquina las cadenas que debe aceptar y las que debe rechazar
    private static void agregarEjemplos(Maquina m, String[] aceptadas, String[] rechazadas) {
        for (String cadena : aceptadas) {
            m.agregarAceptada(cadena);
        }
        for (String cadena : rechazadas) {
            m.agregarRechazada(cadena);
        }
    }

    // ===================== NIVEL 3: AFD =====================

    /** Cadenas con {a,b} que tienen una cantidad par de 'a'. */
    public static AFD afdCantidadParDeA() {
        AFD m = new AFD("Cantidad par de a",
                "Alfabeto {a,b}. Acepta las cadenas que tienen un número par de 'a' (q0 = par, q1 = impar).", "q0");
        m.agregarTransicion("q0", 'a', "q1");
        m.agregarTransicion("q0", 'b', "q0");
        m.agregarTransicion("q1", 'a', "q0");
        m.agregarTransicion("q1", 'b', "q1");
        m.marcarComoAceptacion("q0");
        agregarEjemplos(m, new String[] {"", "aa", "baab", "abab"},
                           new String[] {"a", "aaa", "bab"});
        return m;
    }

    /** Cadenas con {a,b} que terminan en "ab". */
    public static AFD afdTerminaEnAb() {
        AFD m = new AFD("Termina en ab",
                "Alfabeto {a,b}. Acepta las cadenas que terminan exactamente en \"ab\".", "q0");
        m.agregarTransicion("q0", 'a', "q1");
        m.agregarTransicion("q0", 'b', "q0");
        m.agregarTransicion("q1", 'a', "q1");
        m.agregarTransicion("q1", 'b', "q2");
        m.agregarTransicion("q2", 'a', "q1");
        m.agregarTransicion("q2", 'b', "q0");
        m.marcarComoAceptacion("q2");
        agregarEjemplos(m, new String[] {"ab", "aab", "bbab"},
                           new String[] {"", "a", "abb", "ba"});
        return m;
    }

    /**
     * PARAMETRIZABLE: números binarios múltiplos de n.
     * Cada estado guarda el residuo (valor leído hasta ahora, módulo n).
     * Al leer un bit b, el nuevo residuo es (2 * residuo + b) mod n.
     */
    public static AFD afdMultiplosDe(int n) {
        AFD m = new AFD("Binarios múltiplos de " + n,
                "Alfabeto {0,1}. Acepta los números binarios divisibles entre " + n
                + ". El estado r_i guarda el residuo i.", "r0");
        for (int residuo = 0; residuo < n; residuo++) {
            m.agregarTransicion("r" + residuo, '0', "r" + ((2 * residuo) % n));
            m.agregarTransicion("r" + residuo, '1', "r" + ((2 * residuo + 1) % n));
        }
        m.marcarComoAceptacion("r0");

        String[] buenas = {Integer.toBinaryString(n), Integer.toBinaryString(2 * n), Integer.toBinaryString(3 * n)};
        String[] malas = new String[0];
        if (n > 1) {
            malas = new String[] {"1", Integer.toBinaryString(n + 1), Integer.toBinaryString(2 * n + 1)};
        }
        agregarEjemplos(m, buenas, malas);
        return m;
    }

    // ===================== NIVEL 2: AUTÓMATA DE PILA =====================

    /** L = { a^n b^n | n >= 1 }. Apila una A por cada 'a' y desapila una por cada 'b'. */
    public static AutomataPila pilaAnBn() {
        AutomataPila m = new AutomataPila("a^n b^n (n ≥ 1)",
                "Apila una A por cada 'a' y desapila una A por cada 'b'. Si al final la pila queda en el fondo (ε), acepta.", "q0");
        char e = AutomataPila.EPSILON;   // transiciones que no leen entrada
        char f = AutomataPila.FONDO;     // símbolo del fondo de la pila
        m.agregarTransicion("q0", 'a', f, "q0", "A" + f);   // primera a: apila A sobre el fondo
        m.agregarTransicion("q0", 'a', 'A', "q0", "AA");    // otras a: apila otra A
        m.agregarTransicion("q0", 'b', 'A', "q1", "");      // primera b: desapila
        m.agregarTransicion("q1", 'b', 'A', "q1", "");      // otras b: desapila
        m.agregarTransicion("q1", e, f, "q2", "" + f);      // pila en el fondo: pasa a aceptación
        m.marcarComoAceptacion("q2");
        agregarEjemplos(m, new String[] {"ab", "aabb", "aaabbb"},
                           new String[] {"", "aab", "abb", "ba", "abab"});
        return m;
    }

    /** Paréntesis balanceados. Apila en '(' y desapila en ')'. */
    public static AutomataPila pilaParentesis() {
        AutomataPila m = new AutomataPila("Paréntesis balanceados",
                "Alfabeto { ( , ) }. Apila una P por cada '(' y desapila una por cada ')'. La cadena vacía también es válida.", "q0");
        char e = AutomataPila.EPSILON;
        char f = AutomataPila.FONDO;
        m.agregarTransicion("q0", '(', f, "q0", "P" + f);
        m.agregarTransicion("q0", '(', 'P', "q0", "PP");
        m.agregarTransicion("q0", ')', 'P', "q0", "");
        m.agregarTransicion("q0", e, f, "q1", "" + f);
        m.marcarComoAceptacion("q1");
        agregarEjemplos(m, new String[] {"", "()", "(())", "()()", "(()(()))"},
                           new String[] {"(", ")", "(()", "())", ")("});
        return m;
    }

    // ===================== NIVEL 1: MT CON CINTA ACOTADA =====================

    /**
     * L = { a^n b^n c^n | n >= 1 } (no es libre de contexto).
     * En cada pasada tacha una a (X), una b (Y) y una c (Z).
     */
    public static MaquinaTuring mtAnBnCn() {
        MaquinaTuring m = new MaquinaTuring("a^n b^n c^n (cinta acotada)",
                "Nivel 1. En cada pasada tacha una a (X), una b (Y) y una c (Z). La entrada va entre los delimitadores [ y ].",
                true, "q0", "qA");

        // q0: busca la siguiente 'a' sin tachar
        m.agregarTransicion("q0", 'a', 'X', 'R', "q1");
        m.agregarTransicion("q0", 'Y', 'Y', 'R', "q4");   // ya no hay 'a': verificar el resto
        // q1: avanza hasta la primera 'b' sin tachar
        m.agregarTransicion("q1", 'a', 'a', 'R', "q1");
        m.agregarTransicion("q1", 'Y', 'Y', 'R', "q1");
        m.agregarTransicion("q1", 'b', 'Y', 'R', "q2");
        // q2: avanza hasta la primera 'c' sin tachar
        m.agregarTransicion("q2", 'b', 'b', 'R', "q2");
        m.agregarTransicion("q2", 'Z', 'Z', 'R', "q2");
        m.agregarTransicion("q2", 'c', 'Z', 'L', "q3");
        // q3: regresa a la izquierda hasta la última X
        m.agregarTransicion("q3", 'a', 'a', 'L', "q3");
        m.agregarTransicion("q3", 'b', 'b', 'L', "q3");
        m.agregarTransicion("q3", 'Y', 'Y', 'L', "q3");
        m.agregarTransicion("q3", 'Z', 'Z', 'L', "q3");
        m.agregarTransicion("q3", 'X', 'X', 'R', "q0");
        // q4 y q5: solo deben quedar Y's, luego Z's, y después el delimitador
        m.agregarTransicion("q4", 'Y', 'Y', 'R', "q4");
        m.agregarTransicion("q4", 'Z', 'Z', 'R', "q5");
        m.agregarTransicion("q5", 'Z', 'Z', 'R', "q5");
        m.agregarTransicion("q5", Cinta.FIN, Cinta.FIN, 'S', "qA");

        agregarEjemplos(m, new String[] {"abc", "aabbcc", "aaabbbccc"},
                           new String[] {"", "aabbc", "abcabc", "abbc", "cba", "aabbccc"});
        return m;
    }

    // ===================== NIVEL 0: MÁQUINA DE TURING =====================

    /** Suma 1 a un número binario. Va al final y retrocede pasando el acarreo. */
    public static MaquinaTuring mtIncremento() {
        MaquinaTuring m = new MaquinaTuring("Incremento binario (n + 1)",
                "Nivel 0. Suma 1 a un número binario. El resultado queda escrito en la cinta.",
                false, "q0", "qA");
        m.agregarTransicion("q0", '0', '0', 'R', "q0");
        m.agregarTransicion("q0", '1', '1', 'R', "q0");
        m.agregarTransicion("q0", Cinta.BLANCO, Cinta.BLANCO, 'L', "q1");
        m.agregarTransicion("q1", '1', '0', 'L', "q1");               // 1 + 1 = 0 y llevo 1
        m.agregarTransicion("q1", '0', '1', 'S', "qA");               // 0 + 1 = 1 y termina
        m.agregarTransicion("q1", Cinta.BLANCO, '1', 'S', "qA");      // desborde: 111 + 1 = 1000
        agregarEjemplos(m, new String[] {"0", "1", "1011", "111", ""},
                           new String[] {"12", "1a0"});
        return m;
    }

    /** Suma en unario: 11+111 termina como 11111. Cambia el '+' por 1 y borra el último 1. */
    public static MaquinaTuring mtSumaUnaria() {
        MaquinaTuring m = new MaquinaTuring("Suma unaria (a + b)",
                "Nivel 0. Entrada: unos separados por '+'. Ejemplo: 11+111 deja 11111 en la cinta.",
                false, "q0", "qA");
        m.agregarTransicion("q0", '1', '1', 'R', "q0");
        m.agregarTransicion("q0", '+', '1', 'R', "q1");               // el + se vuelve 1
        m.agregarTransicion("q1", '1', '1', 'R', "q1");
        m.agregarTransicion("q1", Cinta.BLANCO, Cinta.BLANCO, 'L', "q2");
        m.agregarTransicion("q2", '1', Cinta.BLANCO, 'S', "qA");      // borra el último 1
        agregarEjemplos(m, new String[] {"11+111", "1+1", "111+", "+11"},
                           new String[] {"111", "", "11+1a"});
        return m;
    }

    // ===================== LISTAS PARA LA INTERFAZ =====================

    public static ArrayList<Maquina> nivel3() {
        ArrayList<Maquina> lista = new ArrayList<>();
        lista.add(afdCantidadParDeA());
        lista.add(afdTerminaEnAb());
        lista.add(afdMultiplosDe(3));
        return lista;
    }

    public static ArrayList<Maquina> nivel2() {
        ArrayList<Maquina> lista = new ArrayList<>();
        lista.add(pilaAnBn());
        lista.add(pilaParentesis());
        return lista;
    }

    public static ArrayList<Maquina> nivel1() {
        ArrayList<Maquina> lista = new ArrayList<>();
        lista.add(mtAnBnCn());
        return lista;
    }

    public static ArrayList<Maquina> nivel0() {
        ArrayList<Maquina> lista = new ArrayList<>();
        lista.add(mtIncremento());
        lista.add(mtSumaUnaria());
        return lista;
    }
}
