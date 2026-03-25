import java.util.HashMap;
import java.util.Map;

/**
 * Secao 2.6 - Linguagem ubiqua.
 * O dominio escolhido e biblioteca, com termos consistentes em todo o codigo.
 */
public class UbiquitousLanguageExample {
  public static void main(String[] args) {
    Catalogo catalogo = new Catalogo();
    catalogo.adicionarLivro(new Livro("978-85-7522-xxx-x", "Engenharia de Software"));

    Usuario usuario = new Usuario("u123", "Ana");
    Reserva reserva = catalogo.reservarLivro("978-85-7522-xxx-x", usuario);

    System.out.println("Reserva criada para: " + reserva.getUsuario().getNome());
  }
}

class Catalogo {
  private final Map<String, Livro> livrosPorIsbn = new HashMap<>();

  void adicionarLivro(Livro livro) {
    livrosPorIsbn.put(livro.getIsbn(), livro);
  }

  Reserva reservarLivro(String isbn, Usuario usuario) {
    Livro livro = livrosPorIsbn.get(isbn);
    if (livro == null) {
      throw new IllegalArgumentException("Livro nao encontrado no catalogo: " + isbn);
    }
    return new Reserva(usuario, livro);
  }
}

class Livro {
  private final String isbn;
  private final String titulo;

  Livro(String isbn, String titulo) {
    this.isbn = isbn;
    this.titulo = titulo;
  }

  String getIsbn() {
    return isbn;
  }

  String getTitulo() {
    return titulo;
  }
}

class Usuario {
  private final String id;
  private final String nome;

  Usuario(String id, String nome) {
    this.id = id;
    this.nome = nome;
  }

  String getId() {
    return id;
  }

  String getNome() {
    return nome;
  }
}

class Reserva {
  private final Usuario usuario;
  private final Livro livro;

  Reserva(Usuario usuario, Livro livro) {
    this.usuario = usuario;
    this.livro = livro;
  }

  Usuario getUsuario() {
    return usuario;
  }

  Livro getLivro() {
    return livro;
  }
}
