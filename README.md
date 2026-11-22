# Simulador de la Jerarquía de Chomsky

Aplicación de escritorio en **Java Swing** que permite visualizar y validar el comportamiento de los modelos
computacionales de los cuatro niveles de la Jerarquía de Chomsky. Cada módulo procesa una cadena **paso a paso**
y se puede recorrer con los botones *Siguiente*, *Anterior* y *Reproducir*.

| Nivel | Lenguajes | Máquina | Qué muestra la interfaz |
|---|---|---|---|
| 3 | Regulares | Autómata Finito Determinista (AFD) | Diagrama de estados y el estado actual en cada símbolo |
| 2 | Libres de contexto | Autómata de pila | La pila (LIFO) creciendo y decreciendo mientras se consume la cadena |
| 1 | Sensibles al contexto | Máquina de Turing con cinta acotada (LBA) | Cinta entre delimitadores `[ ]` y cabezal que se mueve en ambos sentidos |
| 0 | Recursivamente enumerables | Máquina de Turing | Cinta extensible, alfabeto de la cinta, cabezal y estado activo |

## Requisitos

- JDK 8 o superior (probado con JDK 17).
- No se usan librerías externas, solo la librería estándar de Java.

## Cómo ejecutar

**Con VS Code:** abrir la carpeta del proyecto, abrir `src/chomsky/Main.java` y presionar *Run*.

**Desde la consola** (en la carpeta raíz del proyecto):

```
javac -encoding UTF-8 -d bin src/chomsky/*.java src/chomsky/logica/*.java src/chomsky/interfaz/*.java src/chomsky/pruebas/*.java
java -cp bin chomsky.Main
```

Para probar solo la lógica, sin interfaz gráfica:

```
java -cp bin chomsky.pruebas.PruebasConsola
```

## Cómo usar la interfaz

1. Escoger un nivel en el menú de arriba.
2. Seleccionar una máquina y un ejemplo del desplegable, o escribir una cadena propia.
3. Presionar **Simular**.
4. Recorrer la simulación con **Siguiente**, **Anterior** o **Reproducir** (avanza un paso cada 0,8 segundos).
   El paso actual se resalta en la lista de la derecha.
5. Al llegar al último paso se muestra si la cadena fue **aceptada** o **rechazada** y el motivo.

Cada nivel tiene su propio color (verde, azul, naranja y fucsia). Los símbolos también tienen color propio, el
estado o la celda activa brilla en amarillo y, en el AFD, la flecha que se acaba de usar se resalta.

En el nivel 3 se puede crear un AFD **parametrizable**: se escoge un número *n* (de 2 a 7) y se genera un autómata que
acepta los números binarios múltiplos de *n*.

## Cadenas de prueba por nivel

Todas están también en los desplegables de ejemplos de la interfaz.

### Nivel 3 (AFD)

| Máquina | Aceptadas | Rechazadas |
|---|---|---|
| Cantidad par de `a` (alfabeto {a,b}) | (vacía), `aa`, `baab`, `abab` | `a`, `aaa`, `bab` |
| Termina en `ab` (alfabeto {a,b}) | `ab`, `aab`, `bbab` | (vacía), `a`, `abb`, `ba` |
| Binarios múltiplos de n (con n = 3) | `11`, `110`, `1001` | `1`, `100`, `111` |

### Nivel 2 (autómata de pila)

El símbolo del fondo de la pila se muestra como **ε**.

| Máquina | Aceptadas | Rechazadas |
|---|---|---|
| aⁿbⁿ (n ≥ 1) | `ab`, `aabb`, `aaabbb` | (vacía), `aab`, `abb`, `ba`, `abab` |
| Paréntesis balanceados | (vacía), `()`, `(())`, `()()`, `(()(()))` | `(`, `)`, `(()`, `())`, `)(` |

### Nivel 1 (Máquina de Turing con cinta acotada)

| Máquina | Aceptadas | Rechazadas |
|---|---|---|
| aⁿbⁿcⁿ (n ≥ 1) | `abc`, `aabbcc`, `aaabbbccc` | (vacía), `aabbc`, `abcabc`, `abbc`, `cba`, `aabbccc` |

### Nivel 0 (Máquina de Turing)

| Máquina | Entrada | Resultado en la cinta |
|---|---|---|
| Incremento binario (n + 1) | `1011` | `1100` |
| | `111` | `1000` |
| | `0` | `1` |
| Suma unaria | `11+111` | `11111` |
| | `1+1` | `11` |

Entradas que la máquina rechaza: `12` y `1a0` (incremento); `111`, (vacía) y `11+1a` (suma unaria).

## Estructura del proyecto

```
src/chomsky/
  Main.java                 Punto de entrada
  logica/                   Las máquinas (no dependen de la interfaz)
    Maquina.java              Clase padre: nombre, descripción y ejemplos
    AFD.java, TransicionAFD.java
    AutomataPila.java, TransicionPila.java
    MaquinaTuring.java, TransicionMT.java, Cinta.java
    Paso.java, Resultado.java Una "foto" de la máquina y la lista completa de pasos
    Ejemplos.java             Construye las máquinas de ejemplo
  interfaz/                 Pantallas Swing (tema oscuro con un color por nivel)
    VentanaPrincipal.java     La ventana (JFrame)
    PanelPrincipal.java       Menú superior y pantallas de cada nivel (CardLayout)
    PanelSimulacion.java      Pantalla de un nivel, con los controles de reproducción
    PanelDibujo.java          Clase padre de los dibujos (fondo con cuadrícula)
    DibujoAFD.java, DibujoPila.java, DibujoCinta.java, PanelCadena.java
    BotonColor.java, BotonMenu.java, PanelDegradado.java, PanelRedondeado.java,
    FranjaColores.java, ScrollOscuro.java, RenderPasos.java   Componentes visuales propios
    Estilo.java               Colores, fuentes y herramientas de dibujo
  pruebas/
    PruebasConsola.java       Prueba la lógica sin interfaz
```

## Idea de diseño

La lógica y la interfaz están separadas. Cada máquina tiene un método `simular(cadena)` que no dibuja nada:
devuelve un `Resultado` con la lista de `Paso`. La interfaz solo recorre esa lista y le pide al dibujo
correspondiente (autómata, pila o cinta) que pinte el paso actual.

## Autor

EDER SANTIAGO CADENA MORENO — POLITECNICO GRANCOLOMBIANO
