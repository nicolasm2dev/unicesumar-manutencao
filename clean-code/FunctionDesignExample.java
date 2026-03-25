/**
 * Secao 2.7 - Funcoes coesas e desacopladas.
 * Inclui: separacao de responsabilidade, parametro objeto e early return.
 */
public class FunctionDesignExample {
  public static void main(String[] args) {
    Pedido pedido = new Pedido("CLI-10", new Endereco("Rua A", "100", "Maringa", "PR", "87000-000"));

    PedidoService pedidoService = new PedidoService(new CalculadoraFrete());
    pedidoService.processarPedido(pedido);

    System.out.println("Conceito para nota 82: " + calcularConceito(82));
  }

  // Exemplo bom de early return: reduz aninhamento e melhora leitura.
  static String calcularConceito(int nota) {
    if (nota < 0 || nota > 100) {
      return "Nota invalida";
    }
    if (nota >= 90) {
      return "A";
    }
    if (nota >= 80) {
      return "B";
    }
    if (nota >= 70) {
      return "C";
    }
    if (nota >= 60) {
      return "D";
    }
    return "F";
  }
}

class PedidoService {
  private final CalculadoraFrete calculadoraFrete;

  PedidoService(CalculadoraFrete calculadoraFrete) {
    this.calculadoraFrete = calculadoraFrete;
  }

  void processarPedido(Pedido pedido) {
    // Funcao coesa: foco em orquestrar o processamento do pedido.
    double frete = calculadoraFrete.calcular(pedido.getEnderecoEntrega());
    System.out.println("Processando pedido do cliente " + pedido.getClienteId());
    System.out.println("Frete calculado: " + frete);
  }
}

class CalculadoraFrete {
  double calcular(Endereco enderecoEntrega) {
    if ("PR".equals(enderecoEntrega.getEstado())) {
      return 10.0;
    }
    return 18.0;
  }
}

class Pedido {
  private final String clienteId;
  private final Endereco enderecoEntrega;

  Pedido(String clienteId, Endereco enderecoEntrega) {
    this.clienteId = clienteId;
    this.enderecoEntrega = enderecoEntrega;
  }

  String getClienteId() {
    return clienteId;
  }

  Endereco getEnderecoEntrega() {
    return enderecoEntrega;
  }
}

// Parametro objeto: evita assinaturas longas e confusas.
class Endereco {
  private final String rua;
  private final String numero;
  private final String cidade;
  private final String estado;
  private final String cep;

  Endereco(String rua, String numero, String cidade, String estado, String cep) {
    this.rua = rua;
    this.numero = numero;
    this.cidade = cidade;
    this.estado = estado;
    this.cep = cep;
  }

  String getRua() {
    return rua;
  }

  String getNumero() {
    return numero;
  }

  String getCidade() {
    return cidade;
  }

  String getEstado() {
    return estado;
  }

  String getCep() {
    return cep;
  }
}
