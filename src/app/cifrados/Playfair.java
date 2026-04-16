package app.cifrados;

public class Playfair {
    public static String cifrar(String texto, String clave) {
        return transformar(texto, clave, true);
    }

    public static String descifrar(String texto, String clave) {
        return transformar(texto, clave, false);
    }

    public static String obtenerTablero(String clave) {
        String claveNormalizada = normalizar(clave);
        if (claveNormalizada.isEmpty()) return "";

        char[][] matriz = construirMatriz(claveNormalizada);
        String tablero = "";

        for (int fila = 0; fila < 5; fila++) {
            for (int columna = 0; columna < 5; columna++) {
                tablero += matriz[fila][columna];
                if (columna < 4) tablero += " ";
            }
            if (fila < 4) tablero += "\n";
        }

        return tablero;
    }

    private static String transformar(String texto, String clave, boolean cifrar) {
        String textoNormalizado = normalizar(texto);
        String claveNormalizada = normalizar(clave);

        if (textoNormalizado.isEmpty()) return "";
        if (claveNormalizada.isEmpty()) return textoNormalizado;

        char[][] matriz = construirMatriz(claveNormalizada);
        int[][] posiciones = construirPosiciones(matriz);
        String preparado = cifrar ? prepararTexto(textoNormalizado) : prepararTextoDescifrado(textoNormalizado);
        String resultado = "";

        for (int i = 0; i < preparado.length(); i += 2) {
            char a = preparado.charAt(i);
            char b = preparado.charAt(i + 1);

            int filaA = posiciones[a - 'A'][0];
            int colA = posiciones[a - 'A'][1];
            int filaB = posiciones[b - 'A'][0];
            int colB = posiciones[b - 'A'][1];

            if (filaA == filaB) {
                int paso = cifrar ? 1 : 4;
                resultado += matriz[filaA][(colA + paso) % 5];
                resultado += matriz[filaB][(colB + paso) % 5];
            } else if (colA == colB) {
                int paso = cifrar ? 1 : 4;
                resultado += matriz[(filaA + paso) % 5][colA];
                resultado += matriz[(filaB + paso) % 5][colB];
            } else {
                resultado += matriz[filaA][colB];
                resultado += matriz[filaB][colA];
            }
        }

        return resultado;
    }

    private static String normalizar(String valor) {
        String resultado = "";

        for (char c : valor.toUpperCase().toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                resultado += (c == 'J') ? 'I' : c;
            }
        }

        return resultado;
    }

    private static char[][] construirMatriz(String clave) {
        boolean[] usado = new boolean[26];
        usado['J' - 'A'] = true;
        String letras = "";

        for (char c : clave.toCharArray()) {
            if (!usado[c - 'A']) {
                usado[c - 'A'] = true;
                letras += c;
            }
        }

        for (char c = 'A'; c <= 'Z'; c++) {
            if (!usado[c - 'A']) {
                usado[c - 'A'] = true;
                letras += c;
            }
        }

        char[][] matriz = new char[5][5];
        int indice = 0;

        for (int fila = 0; fila < 5; fila++) {
            for (int columna = 0; columna < 5; columna++) {
                matriz[fila][columna] = letras.charAt(indice++);
            }
        }

        return matriz;
    }

    private static int[][] construirPosiciones(char[][] matriz) {
        int[][] posiciones = new int[26][2];

        for (int fila = 0; fila < 5; fila++) {
            for (int columna = 0; columna < 5; columna++) {
                char c = matriz[fila][columna];
                posiciones[c - 'A'][0] = fila;
                posiciones[c - 'A'][1] = columna;
            }
        }

        posiciones['J' - 'A'][0] = posiciones['I' - 'A'][0];
        posiciones['J' - 'A'][1] = posiciones['I' - 'A'][1];
        return posiciones;
    }

    private static String prepararTexto(String texto) {
        String preparado = "";
        int i = 0;

        while (i < texto.length()) {
            char a = texto.charAt(i);
            char b = (i + 1 < texto.length()) ? texto.charAt(i + 1) : 'X';

            if (a == b) {
                preparado += a;
                preparado += (a == 'X') ? 'Q' : 'X';
                i++;
            } else {
                preparado += a;
                preparado += b;
                i += 2;
            }
        }

        if (preparado.length() % 2 != 0) {
            preparado += 'X';
        }

        return preparado;
    }

    private static String prepararTextoDescifrado(String texto) {
        if (texto.length() % 2 == 0) return texto;
        return texto + "X";
    }
}
