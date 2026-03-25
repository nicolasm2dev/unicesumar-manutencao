/**
 * Secao 2.4 - Escolha nomes legiveis.
 * A literatura recomenda: nomes devem revelar claramente seu proposito.
 */
public class NamingExamples {
  public static void main(String[] args) {
    BadConta c = new BadConta(1000.0);
    c.sac(100.0);

    ContaBancaria contaBancaria = new ContaBancaria(1000.0);
    contaBancaria.sacar(100.0);
    contaBancaria.depositar(50.0);

    System.out.println("Saldo final: " + contaBancaria.getSaldo());
    System.out.println("Tem saldo? " + contaBancaria.hasSaldoDisponivel());
  }
}

// Exemplo ruim: abreviacoes e nomes genericos dificultam leitura e manutencao.
class BadConta {
  private double s;

  BadConta(double s) {
    this.s = s;
  }

  void sac(double x) {
    s -= x;
  }
}

// Exemplo bom: nomes completos e orientados ao dominio.
class ContaBancaria {
  private double saldo;

  ContaBancaria(double saldoInicial) {
    this.saldo = saldoInicial;
  }

  void sacar(double valor) {
    saldo -= valor;
  }

  void depositar(double valor) {
    saldo += valor;
  }

  double getSaldo() {
    return saldo;
  }

  // Regra da Secao 2.4: nome booleano positivo e sem negacao confusa.
  boolean hasSaldoDisponivel() {
    return saldo > 0;
  }
}

interface Relatorio {
  String gerar();
}

class RelatorioSaldo implements Relatorio {
  @Override
  public String gerar() {
    return "Relatorio de saldo gerado";
  }
}
