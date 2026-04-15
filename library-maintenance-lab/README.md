# Laboratório de Manutenção de Software – Sistema Legado de Biblioteca

Este repositório simula um sistema Java legado que evoluiu ao longo do tempo com múltiplas mudanças incrementais, correções rápidas e decisões arquiteturais de curto prazo. O resultado é um código funcional, porém com alta complexidade de manutenção, ideal para práticas reais de manutenção de software.

## Contexto do Sistema

Com base na implementação atual, o sistema oferece:

- cadastro de livros ([BookManager.registerBook](src%20atividade%201/BookManager.java#L10))
- cadastro de usuários ([UserManager.registerUser](src%20atividade%201/UserManager.java#L5))
- empréstimo de livros ([LoanManager.borrowBook](src%20atividade%201/LoanManager.java#L14))
- devolução de livros ([LoanManager.returnBook](src%20atividade%201/LoanManager.java#L90))
- geração de relatórios ([ReportGenerator.generateSimpleReport](src%20atividade%201/ReportGenerator.java#L9))
- operação via menu de linha de comando ([LibrarySystem.startCli](src%20atividade%201/LibrarySystem.java#L23))

O projeto contém intencionalmente problemas de manutenibilidade e bugs sutis para apoiar atividades práticas de manutenção preventiva, corretiva e evolutiva.

## Organização das Atividades

As atividades foram separadas em documentos próprios para deixar objetivos, escopo e formato de entrega mais claros:

1. [ATIVIDADE_1.md](ATIVIDADE_1.md) - Análise de Código e Manutenção Preventiva
2. [ATIVIDADE_2.md](ATIVIDADE_2.md) - Manutenção Corretiva e Evolutiva

Data final de entrega: 16/04.

## Como Executar o Projeto

Compilar:

```bash
javac src/*.java
```

Executar modo interativo:

```bash
java -cp src Main
```

Executar listagem rápida:

```bash
java -cp src Main --list
```

Executar relatório rápido:

```bash
java -cp src Main --report
```

## Visão Geral de Problemas de Manutenibilidade

Problemas detalhados e guias de exploração foram movidos para os arquivos de atividade.

## Observação Final

O objetivo não é reescrever o sistema inteiro do zero.

Os estudantes devem melhorar o sistema incrementalmente, simulando manutenção de software no mundo real com pequenas mudanças seguras, validação contínua e evolução controlada.
