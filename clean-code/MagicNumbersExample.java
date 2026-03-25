/**
 * Secao 2.5 - Evite numeros magicos.
 * Demonstracao: versao ruim com literais e versao boa com constantes nomeadas.
 */
public class MagicNumbersExample {
  private static final double LIMITE_PESO_KG = 1.5;
  private static final double FRETE_BASICO_POR_KG = 0.315;
  private static final double FRETE_ESPECIAL_POR_KG = 0.378;
  private static final double FATOR_ICMS = 1.18;

  public static void main(String[] args) {
    double precoBase = 100.0;
    double peso = 2.0;

    System.out.println("Preco ruim: " + calcularPrecoFinalRuim(precoBase, peso));
    System.out.println("Preco bom: " + calcularPrecoFinal(precoBase, peso));
  }

  static double calcularPrecoFinalRuim(double preco, double peso) {
    double frete;
    if (peso <= 1.5) {
      frete = 0.315 * peso;
    } else {
      frete = 0.378 * peso;
    }
    return preco * 1.18 + frete;
  }

  static double calcularPrecoFinal(double preco, double peso) {
    double frete;
    if (peso <= LIMITE_PESO_KG) {
      frete = FRETE_BASICO_POR_KG * peso;
    } else {
      frete = FRETE_ESPECIAL_POR_KG * peso;
    }
    return preco * FATOR_ICMS + frete;
  }
}
