package app.cifrados;

public class Cesar {
    public static String cifrar(String texto, String clave) {
        int desplazamiento = 3;
        try { desplazamiento = Integer.parseInt(clave); } catch (Exception e) {}

        String resultado = "";
        for (char c : texto.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                resultado += (char) ((c - base + desplazamiento) % 26 + base);
            } else resultado += c;
        }
        return resultado;
    }
}