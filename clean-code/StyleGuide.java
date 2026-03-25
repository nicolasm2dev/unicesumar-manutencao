/**
 * Secao 2.2 - Guia de estilo.
 * Este arquivo mostra regras basicas de formatacao e nomenclatura em Java.
 */

 class MensagemUtil {
  private MensagemUtil() {
  }

  static void imprimirCabecalho(String tituloAula) {
    System.out.println("=== " + tituloAula + " ===");
  }
}
public class StyleGuide {
  public static void main(String[] args) {
    imprimirContador(5);

    // Boa pratica do guia: acesso a membro estatico qualificado pela classe.
    MensagemUtil.imprimirCabecalho("Aula de Codigo Limpo");
  }

  static void imprimirContador(int limite) {
    // Chave de abertura na mesma linha e espaco entre for e parenteses.
    for (int i = 1; i <= limite; i++) {
      System.out.println("Contador: " + i);
    }
  }
}

