# Atividade 1 – Análise de Código e Manutenção Preventiva

Esta atividade foca em manutenção preventiva: entender o sistema legado, classificar problemas de manutenibilidade e refatorar sem alterar o comportamento funcional.

## Objetivos

1. Identificar pelo menos 10 problemas de manutenibilidade.
2. Classificar cada problema com base nos conceitos estudados.
3. Aplicar pelo menos 5 refatorações no código.
4. Garantir que o comportamento externo do sistema permaneça o mesmo.

## Escopo Técnico

- Linguagem: Java (sem frameworks)
- Operação: CLI
- Código-fonte principal em [src](src%20atividade%201)

## Code Smells Identificados no Projeto

| Arquivo | Classe/Método | Code Smell | Descrição | Possível Refatoração |
| --- | --- | --- | --- | --- |
| [src/LibrarySystem.java](src%20atividade%201/LibrarySystem.java) | [LibrarySystem (classe)](src%20atividade%201/LibrarySystem.java#L4) | God Class | Centraliza menu, orquestração, entrada de dados, debug, logs e cenário de demonstração na mesma classe. | Separar em camadas (controller/serviço) e dividir casos de uso por módulo. |
| [src/LibrarySystem.java](src%20atividade%201/LibrarySystem.java) | [startCli()](src%20atividade%201/LibrarySystem.java#L23) | Mixed Responsibilities | Loop principal mistura interface de usuário, regras de execução e tratamento de erros genérico. | Extrair dispatcher de comandos e separar UI de aplicação. |
| [src/LibrarySystem.java](src%20atividade%201/LibrarySystem.java) | [handleDebugArea()](src%20atividade%201/LibrarySystem.java#L225) | Long Method | Método muito extenso para uma ação de menu, com muitos caminhos de execução. | Extract Method por ação de debug. |
| [src/LibrarySystem.java](src%20atividade%201/LibrarySystem.java) | [handleDebugArea()](src%20atividade%201/LibrarySystem.java#L225) | Deep Nesting | Cadeia de if/else em vários níveis dificulta leitura e alteração segura. | Substituir por early return ou mapa de comandos. |
| [src/LibrarySystem.java](src%20atividade%201/LibrarySystem.java) | [campos com new](src%20atividade%201/LibrarySystem.java#L9) | Tight Coupling / No DI | Dependências criadas diretamente com new BookManager, new UserManager, new LoanManager etc. | Injeção por construtor e interfaces para desacoplamento. |
| [src/LibrarySystem.java](src%20atividade%201/LibrarySystem.java) | [handleRegisterBook()](src%20atividade%201/LibrarySystem.java#L81) | Duplicate Code | Validação e normalização repetidas também em BookManager.registerBook. | Consolidar validação em um único ponto. |
| [src/LibrarySystem.java](src%20atividade%201/LibrarySystem.java) | [handleBorrowBook() / handleReturnBook()](src%20atividade%201/LibrarySystem.java#L141) | Primitive Obsession | Fluxos de negócio dirigidos por strings e inteiros mágicos (channel, policyCode, forceFlag). | Criar enums/objetos de valor para parâmetros de domínio. |
| [src/LoanManager.java](src%20atividade%201/LoanManager.java) | [borrowBook(...)](src%20atividade%201/LoanManager.java#L14) | Long Method | Método concentra validação de usuário/livro, criação de empréstimo, atualização de estado e notificação. | Quebrar em validar, criarEmpréstimo, atualizarEstoque e notificar. |
| [src/LoanManager.java](src%20atividade%201/LoanManager.java) | [borrowBook(...)](src%20atividade%201/LoanManager.java#L14) | Long Parameter List | Assinatura com 8 parâmetros primitivos aumenta risco de chamada incorreta. | Introduzir BorrowRequest com campos nomeados. |
| [src/LoanManager.java](src%20atividade%201/LoanManager.java) | [borrowBook(...)](src%20atividade%201/LoanManager.java#L20) | Deep Nesting | Múltiplos ifs encadeados para validações e regras operacionais. | Guard clauses para reduzir aninhamento. |
| [src/LoanManager.java](src%20atividade%201/LoanManager.java) | [returnBook(...)](src%20atividade%201/LoanManager.java#L90) | Inconsistent Error Handling | Quando loan é nulo, retorna silenciosamente (workaround); em outros casos lança RuntimeException. | Definir política única de erro por operação. |
| [src/LoanManager.java](src%20atividade%201/LoanManager.java) | [calculateFineLegacy(...)](src%20atividade%201/LoanManager.java#L144) | Primitive Obsession | Datas e regras representadas por String/int sem tipos de domínio. | Usar tipo de data e enum de política de multa. |
| [src/LoanManager.java](src%20atividade%201/LoanManager.java) | [campo notificationService](src%20atividade%201/LoanManager.java#L9) | Tight Coupling | Dependência concreta instanciada internamente. | Injeção de dependência e contrato de notificação. |
| [src/ReportGenerator.java](src%20atividade%201/ReportGenerator.java) | [generateSimpleReport(...)](src%20atividade%201/ReportGenerator.java#L9) | Mixed Responsibilities | Método monta texto, consulta base global e calcula estatísticas ao mesmo tempo. | Separar cálculo do relatório da formatação. |
| [src/ReportGenerator.java](src%20atividade%201/ReportGenerator.java) | [totalLoans](src%20atividade%201/ReportGenerator.java#L24) | Magic Numbers / Regra obscura | totalLoans usa ajuste fixo (+1), sem contexto de negócio. | Remover ajuste mágico e cobrir com testes. |
| [src/ReportGenerator.java](src%20atividade%201/ReportGenerator.java) | [closedLoans](src%20atividade%201/ReportGenerator.java#L33) | Calculation Bug / Smell de lógica frágil | closedLoans incrementa mesmo para empréstimo aberto. | Reorganizar cálculo com regras explícitas e testes. |
| [src/BookManager.java](src%20atividade%201/BookManager.java) | [registerBook(...)](src%20atividade%201/BookManager.java#L10) | Long Parameter List | Muitos parâmetros tornam manutenção e evolução mais caras. | Introduzir DTO/entidade Book e construtor com validação. |
| [src/BookManager.java](src%20atividade%201/BookManager.java) | [registerBook(...)](src%20atividade%201/BookManager.java#L17) | Validation Smell | Fluxo aceita título em branco por workaround legado, contrariando regra de negócio. | Centralizar validação e falhar com mensagem consistente. |
| [src/BookManager.java](src%20atividade%201/BookManager.java) | [listBooksSimple()](src%20atividade%201/BookManager.java#L51) | Edge Case mal tratado | Acesso a temp.get(0) quando lista vazia pode causar falha. | Fazer early return com mensagem de sem registros. |
| [src/BookManager.java](src%20atividade%201/BookManager.java) | [updateAvailableWithLegacyRule(...)](src%20atividade%201/BookManager.java#L75) | Long Parameter List / Mixed Responsibilities | Mistura atualização de estado, regras por opCode e logging com vários parâmetros de controle. | Separar por operações de estoque e reduzir parâmetros. |
| [src/UserManager.java](src%20atividade%201/UserManager.java) | [registerUser(...)](src%20atividade%201/UserManager.java#L5) | Long Parameter List | Cadastro recebe 7 parâmetros primitivos/strings sem objeto de contexto. | Criar UserData e validação centralizada. |
| [src/UserManager.java](src%20atividade%201/UserManager.java) | [validateUserData(...)](src%20atividade%201/UserManager.java#L102) | Duplicate Code | Regras de validação semelhantes às usadas no fluxo de cadastro. | Reutilizar único validador de usuário. |
| [src/LegacyDatabase.java](src%20atividade%201/LegacyDatabase.java) | [classe e campos estaticos](src%20atividade%201/LegacyDatabase.java#L6) | Hidden Dependencies | Estado global compartilhado por todo o sistema torna testes dependentes de ordem de execução. | Encapsular estado em repositórios com ciclo de vida controlado. |
| [src/LegacyDatabase.java](src%20atividade%201/LegacyDatabase.java) | [getBooks/getUsers/getLoans](src%20atividade%201/LegacyDatabase.java#L128) | Breaking Encapsulation | Estruturas internas mutáveis são expostas diretamente para qualquer classe. | Retornar cópias defensivas ou coleções imutáveis. |
| [src/LegacyDatabase.java](src%20atividade%201/LegacyDatabase.java) | [unsafeUpdateBookField/unsafeUpdateUserField](src%20atividade%201/LegacyDatabase.java#L144) | Encapsulation Violation | Atualizações dinâmicas por nome de campo ignoram validação e regras de negócio. | Criar métodos explícitos de atualização por caso de uso. |
| [src/LegacyDatabase.java](src%20atividade%201/LegacyDatabase.java) | [countOpenLoansByBook()](src%20atividade%201/LegacyDatabase.java#L181) | Poor Naming / Lógica inconsistente | Nome sugere filtro por livro, mas o filtro usa userId. | Corrigir filtro e alinhar nome/implementação. |
| [src/LegacyDatabase.java](src%20atividade%201/LegacyDatabase.java) | [clearLogsIfTooBig()](src%20atividade%201/LegacyDatabase.java#L200) | Magic Numbers | Limites 500 e 400 são números mágicos sem semântica explícita. | Extrair constantes nomeadas e documentar política. |
| [src/DataUtil.java](src%20atividade%201/DataUtil.java) | [classe utilitaria estatica](src%20atividade%201/DataUtil.java#L5) | Utility Class Overuse | Muitos métodos globais estáticos e estado compartilhado (scanner/retryCounter). | Reduzir estado global e separar utilitários por responsabilidade. |
| [src/DataUtil.java](src%20atividade%201/DataUtil.java) | [datePlusDaysApprox(...)](src%20atividade%201/DataUtil.java#L169) | Primitive Obsession / Regra frágil | Cálculo de data por concatenação de string. | Usar API de data e regra de prazo real. |
| [src/Main.java](src%20atividade%201/Main.java) | [main(...)](src%20atividade%201/Main.java#L3) | Feature Envy / Coordination Leakage | Main conhece detalhes de modos e aciona componentes internos diretamente. | Encapsular modos de execução em serviço de aplicação. |

## Hints (Guia de Exploração)

Hint 1  
Observe [LoanManager.borrowBook](src%20atividade%201/LoanManager.java#L14) em [src/LoanManager.java](src%20atividade%201/LoanManager.java).  
Ele mistura validação, regra de negócio e manipulação de estado em um único fluxo.

Hint 2  
Observe [LoanManager.returnBook](src%20atividade%201/LoanManager.java#L90) em [src/LoanManager.java](src%20atividade%201/LoanManager.java).  
Compare os caminhos de erro e note inconsistências entre lançar exceção e retornar silenciosamente.

Hint 3  
Observe [ReportGenerator.generateSimpleReport](src%20atividade%201/ReportGenerator.java#L9) em [src/ReportGenerator.java](src%20atividade%201/ReportGenerator.java).  
Confira os cálculos em torno de [totalLoans](src%20atividade%201/ReportGenerator.java#L24) e [closedLoans](src%20atividade%201/ReportGenerator.java#L33).

Hint 4  
Observe [LibrarySystem.handleDebugArea](src%20atividade%201/LibrarySystem.java#L225) em [src/LibrarySystem.java](src%20atividade%201/LibrarySystem.java).  
É um bom ponto para praticar redução de aninhamento e decomposição de responsabilidades.

Hint 5  
Observe [BookManager.registerBook](src%20atividade%201/BookManager.java#L10) em [src/BookManager.java](src%20atividade%201/BookManager.java).  
A assinatura longa sugere oportunidade para encapsular dados de entrada.

Hint 6  
Observe [BookManager.listBooksSimple](src%20atividade%201/BookManager.java#L51) em [src/BookManager.java](src%20atividade%201/BookManager.java).  
Teste o comportamento quando não houver livros cadastrados.

Hint 7  
Observe [LegacyDatabase.countOpenLoansByBook](src%20atividade%201/LegacyDatabase.java#L181) em [src/LegacyDatabase.java](src%20atividade%201/LegacyDatabase.java).  
Valide se o critério de filtro condiz com o nome e objetivo do método.

Hint 8  
Observe [LibrarySystem](src%20atividade%201/LibrarySystem.java#L4) como um todo em [src/LibrarySystem.java](src%20atividade%201/LibrarySystem.java).  
A classe centraliza muitas responsabilidades e depende diretamente de várias outras classes.

## Conceitos Praticados

- manutenção de software
- leitura e compreensão de código
- code smells
- coesão e acoplamento
- refatoração
- legibilidade de código
- versionamento e commits

## Formato de Entrega - Atividade 1

Data final de entrega: 16/04.

1. Pull request ou branch dedicada com as refatorações aplicadas.
2. Documento curto (no README da branch ou arquivo markdown) contendo:
   - lista dos 10+ problemas encontrados
   - classificação dos smells
   - lista das 5+ refatorações realizadas
3. Evidência de que o comportamento original foi preservado (logs de execução antes/depois ou roteiro de validação manual).
4. Commits pequenos e descritivos, com mensagem clara da intenção de manutenção preventiva.

## Regras de Submissão

### Repositório da Entrega

1. A entrega deve ser feita em um repositório público no GitHub.
2. O repositório deve ser um fork do repositório da disciplina.

### Envio por E-mail

Após publicar o repositório no GitHub, enviar o link por e-mail para:

joao.vsantos@unicesumar.edu.br

O e-mail deve seguir este formato:

Título do e-mail:

Atividade 1 Manutenção ESOFT5S

Substitua o número conforme o trabalho (Atividade 1 ou Atividade 2).

Corpo do e-mail:

1. Nome completo de cada integrante da equipe.
2. Usuário do GitHub de cada integrante.
3. Link do repositório no GitHub.

Exemplo:

Integrantes:

João Silva - github.com/joaosilva  
Maria Souza - github.com/mariasouza  
Pedro Lima - github.com/pedrolima

Repositório: https://github.com/grupo-exemplo/trabalho-mobile

### Cópia do E-mail (CC)

O e-mail deve incluir em cópia (CC) o endereço de e-mail de todos os integrantes da equipe.

### Trabalho em Grupo

Os trabalhos podem ser realizados em grupos de até 6 participantes.

### Participação Individual

Cada integrante do grupo deve possuir pelo menos um commit relevante relacionado ao desenvolvimento da atividade no repositório.

### Vídeo da Entrega

Deve ser gravado um vídeo da equipe para esta atividade.

1. Cada integrante deve explicar pelo menos uma manutenção realizada na atividade.
2. A explicação deve descrever o que foi feito e porque.
3. O vídeo deve mostrar o código correspondente à manutenção explicada.
4. O vídeo deve incluir a execução do código para demonstrar o funcionamento.

## Observação

Não reescreva o sistema do zero. O foco é melhorar incrementalmente.