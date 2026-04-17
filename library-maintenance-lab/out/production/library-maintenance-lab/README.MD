# Lista de 10 problemas indentificados e Code Smells:

 | Arquivo          | Classe/Metodo | Code Smell | Descrição do problema |
 |------------------|--------------| --- | --- |
 | LibrarySystem.java | LibrarySystem | God Class | A classe LibrarySystem acumula todas as responsabilidades do sistema: menu, fluxo de telas, coordenação de managers, debug e relatórios. Viola o Princípio da Responsabilidade Única (SRP). |
 | LegacyDatabase.java | LegacyDataase | Hidden Dependencies | Todos os dados (books, users, loans, logs) são campos estáticos públicos acessados diretamente por qualquer classe. Isso torna o comportamento dependente de ordem de execução e impossibilita testes isolados. |
 | LoanManager.java | LoadManager  | Deep Nesting | O método borrowBook() tem 7 níveis de if aninhados. Esse padrão (arrow anti-pattern) torna o código extremamente difícil de ler, testar e modificar. |
 | BookManager.java | registerBook() | Validation Smell | Quando o título do livro é vazio, o código substitui por um espaço em branco ' ' e continua o fluxo normalmente, persistindo um registro inválido sem alertar o usuário. |
 | Loananager.java  | calculateFineLegacy() | Primitive Obsession | O cálculo de multa compara datas usando compareTo() em Strings. Isso faz uma comparação lexicográfica, não cronológica, podendo gerar multas incorretas. | 
 | LoanManager.java | returnBook() |  Inappropriate Intimacy | Ao calcular a multa na devolução, o código faz debt = debt - fine em vez de debt + fine. Isso faz a dívida do usuário diminuir quando deveria aumentar. |
 | LoanManager.java | borrowBook() | Side Effects | Quando o canal de notificação é 'sms', o método cria dois registros de empréstimo no banco para o mesmo livro/usuário, inflando o histórico e podendo travar o controle de cópias. |
 | UserManager.java | validateUserData() e registerUser() | Duplicated Code | A validação de nome, email e telefone existe em UserManager.validateUserData() e é repetida dentro de registerUser(). |
 | ReportGenerator.java | generateSimpleReport() | Violation of Single Responsibility Principle | ReportGenerator acessa diretamente getBooks(), getUsers() e getLoans() do LegacyDatabase para montar relatórios, misturando acesso a dados com formatação e regras de negócio. |
 | BookManager.java | listBooksSimple() | Dead Code | O método listBooksSimple() tenta acessar temp.get(0) dentro de um bloco que só é executado quando temp.size() == 0, causando IndexOutOfBoundsException garantida. |


# Refatorações

 - LegacyDatabase.java Apresentava código que dificultava a manutenção e testabilidade, uso extensivo de public static, as alterações realizadas foram:
   - Campos estáticos convertidos em instâncias privados.
   - Construtor adicionado para inicializar privates.
   - Metodos (public static) convertidos em (public).

 - 




# 


 
