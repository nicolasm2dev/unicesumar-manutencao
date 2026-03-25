/**
 * Exercicios (baseados no capitulo) com gabarito comentado.
 * Estrategia de aula: discutir primeiro a pergunta; revelar gabarito em seguida.
 */
public class ExercisesWithAnswers {
  public static void main(String[] args) {
    System.out.println("Exercicios e gabaritos carregados para discussao em sala.");
  }

  // 1) Erros de formatacao no exemplo de ordenacao (resumo para debate).
  static String gabarito1() {
    return "Indentacao inconsistente; espacos irregulares; break em linha unica; "
        + "faltam padroes uniformes para chaves e alinhamento.";
  }

  // 2) Vantagem de @Override.
  static String gabarito2() {
    return "@Override aumenta legibilidade e seguranca: o leitor sabe que o metodo "
        + "redefine contrato existente e o compilador detecta assinaturas incorretas.";
  }

  // 3) Portugues ou ingles?
  static String gabarito3() {
    return "Use a linguagem ubiqua do dominio. Em times locais, portugues pode facilitar; "
        + "em APIs, bibliotecas publicas e times globais, ingles costuma ser preferivel.";
  }

  // 4) Critica de linguagem ubiqua para List/Map com metodos misturados.
  static String gabarito4() {
    return "Ha inconsistencia semantica: add/get/remove e put/retrieve/delete misturam "
        + "verbos sinonimos; padronizar vocabulario reduz carga cognitiva.";
  }

  // 5) Critica da funcao processarDocumento.
  static String gabarito5() {
    return "Funcao com baixa coesao: faz backup ou impressao conforme flag booleana. "
        + "Melhor separar em duas funcoes explicitas com responsabilidades distintas.";
  }

  // 6) Critica ao uso de excecao em lista vazia.
  static String gabarito6() {
    return "Lista vazia e resultado normal de consulta. Excecao para este caso polui fluxo. "
        + "Retorne lista vazia e deixe excecoes para situacoes realmente excepcionais.";
  }

  // 7) Critica aos catches de matricula.
  static String gabarito7() {
    return "Repeticao sugere falta de consolidacao. Pode-se tratar com hierarquia de excecoes, "
        + "mensagens padronizadas e acao comum quando apropriado.";
  }

  // 8) Critica ao uso de excecao para controle de loop.
  static String gabarito8() {
    return "ArrayIndexOutOfBoundsException nao deve controlar fluxo normal. "
        + "O correto e iterar com limite explicito e evitar catch vazio.";
  }
}
