package app.cifrados;

public class Cesar {
    private static final String ALFABETO = "abcdefghijklmn\u00f1opqrstuvwxyz";

    public static String cifrar(String texto, String clave) {
        return transformar(texto, clave, 1);
    }

    public static String descifrar(String texto, String clave) {
        return transformar(texto, clave, -1);
    }

    private static String transformar(String texto, String clave, int direccion) {
        int desplazamiento = 3;
        try { desplazamiento = Integer.parseInt(clave); } catch (Exception e) {}
        desplazamiento = ((desplazamiento % ALFABETO.length()) + ALFABETO.length()) % ALFABETO.length();

        String resultado = "";
        for (char c : texto.toCharArray()) {
            char minuscula = Character.toLowerCase(c);
            int indice = ALFABETO.indexOf(minuscula);

            if (indice >= 0) {
                int nuevoIndice = (indice + direccion * desplazamiento + ALFABETO.length()) % ALFABETO.length();
                char transformado = ALFABETO.charAt(nuevoIndice);
                resultado += Character.isUpperCase(c) ? Character.toUpperCase(transformado) : transformado;
            } else {
                resultado += c;
            }
        }
        return resultado;
    }
}
