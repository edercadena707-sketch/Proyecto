package MASAMORRA.pruebas;

import java.util.ArrayList;

import MASAMORRA.logica.Ejemplos;
import MASAMORRA.logica.Maquina;
import MASAMORRA.logica.Paso;
import MASAMORRA.logica.Resultado;

/**
 * Prueba la lógica SIN interfaz gráfica: corre los ejemplos de cada máquina y
 * verifica que las cadenas "aceptadas" se acepten y las "rechazadas" se rechacen.
 */
public class PruebasConsola {
    private static int fallas = 0;

    public static void main(String[] args) {
        probarNivel("NIVEL 3 (AFD)", Ejemplos.nivel3());
        probarNivel("NIVEL 2 (Autómata de pila)", Ejemplos.nivel2());
        probarNivel("NIVEL 1 (Máquina de Turing con cinta acotada)", Ejemplos.nivel1());
        probarNivel("NIVEL 0 (Máquina de Turing)", Ejemplos.nivel0());

        System.out.println();
        System.out.println("=== Traza del autómata de pila con aabb ===");
        mostrarTraza(Ejemplos.pilaAnBn(), "aabb");

        System.out.println();
        System.out.println("=== Traza de la Máquina de Turing (incremento) con 1011 ===");
        mostrarTraza(Ejemplos.mtIncremento(), "1011");

        System.out.println();
        if (fallas == 0) {
            System.out.println("TODO OK");
        } else {
            System.out.println("HAY FALLAS: " + fallas);
        }
    }

    private static void probarNivel(String titulo, ArrayList<Maquina> maquinas) {
        System.out.println("=== " + titulo + " ===");
        for (Maquina m : maquinas) {
            System.out.println("-- " + m.getNombre());
            for (String cadena : m.getAceptadas()) {
                verificar(m, cadena, true);
            }
            for (String cadena : m.getRechazadas()) {
                verificar(m, cadena, false);
            }
        }
        System.out.println();
    }

    private static void verificar(Maquina m, String cadena, boolean debeAceptar) {
        Resultado r = m.simular(cadena);
        String marca = "OK   ";
        if (r.isAceptada() != debeAceptar) {
            marca = "FALLA";
            fallas++;
        }
        String obtenido = "RECHAZA";
        if (r.isAceptada()) {
            obtenido = "ACEPTA";
        }
        System.out.println("   " + marca + " \"" + cadena + "\" -> " + obtenido);
    }

    private static void mostrarTraza(Maquina m, String cadena) {
        Resultado r = m.simular(cadena);
        for (Paso p : r.getPasos()) {
            System.out.println("   " + p.getDescripcion() + "   pila=[" + p.getPila() + "] cinta=[" + p.getCinta() + "]");
        }
        System.out.println("   => " + r.getMotivo());
    }
}
