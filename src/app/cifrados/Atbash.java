package app.cifrados;

public class Atbash {
    public static String cifrar(String texto) {
        String resultado = "";
        for (char c : texto.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                resultado += (char) (base + (25 - (c - base)));
            } else resultado += c;
        }
        return resultado;
    }
}