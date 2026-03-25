import java.util.ArrayList;
import java.util.List;

/**
 * Secao 2.8 - Separacao de fluxo normal e excepcional.
 * Mostra praticas recomendadas e anti-padroes comuns.
 */
public class ExceptionHandlingExample {
  public static void main(String[] args) {
    ProcessadorArquivos processadorArquivos = new ProcessadorArquivos();

    try {
      processadorArquivos.processar("dados.txt");
      System.out.println("Fluxo normal concluido com sucesso.");
    } catch (ArquivoInexistenteException e) {
      System.err.println("Erro de negocio: " + e.getMessage());
    } catch (ArquivoVazioException e) {
      System.err.println("Erro de validacao: " + e.getMessage());
    }

    List<String> produtos = buscarProdutosPorCategoria("ferramentas");
    System.out.println("Produtos encontrados: " + produtos.size());
  }

  // Boa pratica (Secao 2.8.1): caso esperado retorna lista vazia, sem excecao.
  static List<String> buscarProdutosPorCategoria(String categoria) {
    List<String> produtos = new ArrayList<>();
    if ("livros".equals(categoria)) {
      produtos.add("Clean Code");
    }
    return produtos;
  }
}

class ProcessadorArquivos {
  void processar(String nomeArquivo) throws ArquivoInexistenteException, ArquivoVazioException {
    try {
      validarArquivo(nomeArquivo);
      byte[] dados = lerArquivo(nomeArquivo);
      processarDados(dados);
    } catch (IllegalStateException e) {
      // Nunca usar catch vazio: registrar contexto ajuda manutencao futura.
      System.err.println("Falha tecnica ao processar arquivo: " + e.getMessage());
      throw e;
    }
  }

  private void validarArquivo(String nomeArquivo) throws ArquivoInexistenteException, ArquivoVazioException {
    if (nomeArquivo == null || nomeArquivo.isBlank()) {
      throw new ArquivoInexistenteException("Nome de arquivo nao informado");
    }
    if ("vazio.txt".equals(nomeArquivo)) {
      throw new ArquivoVazioException("Arquivo sem dados");
    }
  }

  private byte[] lerArquivo(String nomeArquivo) {
    if ("corrompido.txt".equals(nomeArquivo)) {
      throw new IllegalStateException("Conteudo corrompido");
    }
    return new byte[] {1, 2, 3};
  }

  private void processarDados(byte[] dados) {
    System.out.println("Processando " + dados.length + " bytes.");
  }
}

class ArquivoInexistenteException extends Exception {
  ArquivoInexistenteException(String mensagem) {
    super(mensagem);
  }
}

class ArquivoVazioException extends Exception {
  ArquivoVazioException(String mensagem) {
    super(mensagem);
  }
}
