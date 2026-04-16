package app.cifrados;

public class Vigenere {
    public static String cifrar(String texto, String clave) {
        return transformar(texto, clave, 1);
    }

    public static String descifrar(String texto, String clave) {
        return transformar(texto, clave, -1);
    }

    private static String transformar(String texto, String clave, int direccion) {
        if (clave.isEmpty()) return texto;

        String resultado = "";
        String claveNormalizada = clave.toLowerCase();
        int j = 0;

        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (Character.isLetter(c)) {
                int shift = claveNormalizada.charAt(j % claveNormalizada.length()) - 'a';
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int desplazado = (c - base + direccion * shift + 26) % 26;
                resultado += (char) (desplazado + base);
                j++;
            } else {
                resultado += c;
            }
        }

        return resultado;
    }
}
