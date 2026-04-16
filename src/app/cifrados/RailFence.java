package app.cifrados;

public class RailFence {
    public static String cifrar(String texto, String clave) {
        int rails = obtenerRails(clave);
        texto = texto.replaceAll("\\s+", "");
        if (rails <= 1 || texto.isEmpty()) return texto;

        String[] rail = new String[rails];
        for (int i = 0; i < rails; i++) rail[i] = "";

        int dir = 1;
        int row = 0;
        for (char c : texto.toCharArray()) {
            rail[row] += c;
            if (row == 0) {
                dir = 1;
            } else if (row == rails - 1) {
                dir = -1;
            }
            row += dir;
        }

        String resultado = "";
        for (String r : rail) resultado += r;
        return resultado;
    }

    public static String descifrar(String texto, String clave) {
        int rails = obtenerRails(clave);
        texto = texto.replaceAll("\\s+", "");
        if (rails <= 1 || texto.isEmpty()) return texto;

        boolean[][] patron = new boolean[rails][texto.length()];
        int dir = 1;
        int row = 0;

        for (int columna = 0; columna < texto.length(); columna++) {
            patron[row][columna] = true;
            if (row == 0) {
                dir = 1;
            } else if (row == rails - 1) {
                dir = -1;
            }
            row += dir;
        }

        char[][] matriz = new char[rails][texto.length()];
        int indice = 0;
        for (int fila = 0; fila < rails; fila++) {
            for (int columna = 0; columna < texto.length(); columna++) {
                if (patron[fila][columna]) {
                    matriz[fila][columna] = texto.charAt(indice++);
                }
            }
        }

        String resultado = "";
        dir = 1;
        row = 0;

        for (int columna = 0; columna < texto.length(); columna++) {
            resultado += matriz[row][columna];
            if (row == 0) {
                dir = 1;
            } else if (row == rails - 1) {
                dir = -1;
            }
            row += dir;
        }

        return resultado;
    }

    private static int obtenerRails(String clave) {
        int rails = 2;
        try { rails = Integer.parseInt(clave); } catch (Exception e) {}
        return rails;
    }
}
