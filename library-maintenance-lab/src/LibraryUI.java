
public class LibraryUI {
    private final LibraryController controller;
    private boolean running = true;
    private int menuCounter = 0;

    public LibraryUI(LibraryController controller) {
        this.controller = controller;
    }

    public void start() {
        DataUtil.printHeader("Legacy University Library");
        while (running) {
            try {
                showMenu();
                handleMainMenu();
                checkMaintenanceTasks();
            } catch (Exception e) {
                System.out.println("Erro no sistema: " + e.getMessage());
                LegacyDatabase.addLog("ui-error-" + e.getMessage());
            }
        }
    }

    private void showMenu() {
        DataUtil.printSeparator();
        System.out.println("1 - Registrar Livro");
        System.out.println("2 - Registrar Utilizador");
        System.out.println("3 - Emprestar Livro");
        System.out.println("4 - Devolver Livro");
        System.out.println("5 - Listar Livros");
        System.out.println("6 - Gerar Relatório");
        System.out.println("7 - Listar Utilizadores");
        System.out.println("8 - Listar Empréstimos");

        System.out.println("0 - Sair");
        DataUtil.printSeparator();
    }

    private void handleMainMenu() {
        String option = DataUtil.readLine("Selecione uma opção: ");
        menuCounter++;

        switch (option) {
            case "1":
                uiRegisterBook();
                break;
            case "2":
                uiRegisterUser();
                break;
            case "3":
                uiBorrow();
                break;
            case "4":
                uiReturn();
                break;
            case "5":
                controller.listBooks();
                break;
            case "6":
                uiReport();
                break;
            case "7":
                controller.listUsers();
                break;
            case "8":
                controller.listLoans();
                break;
            case "9":
                showDebugMenu();
                break;
            case "0":
                running = false;
                System.out.println("Encerrando...");
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }
    }

    private void uiRegisterBook() {
        String title = DataUtil.readLine("Título: ");
        String author = DataUtil.readLine("Autor: ");
        int year = DataUtil.askInt("Ano: ", 2000);
        String category = DataUtil.ask("Categoria: ", "GERAL");
        int total = DataUtil.askInt("Total: ", 1);
        String shelf = DataUtil.ask("Prateleira: ", "X0");
        String isbn = DataUtil.ask("ISBN: ", "S/N");

        BookRequest req = new BookRequest(title, author, year, category, total, total, shelf, isbn);
        int id = controller.registerBook(req);
        System.out.println("Livro registado com ID: " + id);
    }

    private void uiRegisterUser() {
        String name = DataUtil.readLine("Nome: ");
        String email = DataUtil.readLine("Email: ");
        String phone = DataUtil.readLine("Telefone: ");
        String type = DataUtil.ask("Tipo (student/teacher): ", "student");
        
        int id = controller.registerUser(name, email, phone, type, "Unknown", "NO-DOC", "ACTIVE");
        System.out.println("Utilizador registado com ID: " + id);
    }

    private void uiBorrow() {
        int uId = DataUtil.askInt("ID Utilizador: ", -1);
        int bId = DataUtil.askInt("ID Livro: ", -1);
        int loanId = controller.borrowBook(uId, bId);
        System.out.println("Empréstimo " + loanId + " criado.");
    }

    private void uiReturn() {
        int loanId = DataUtil.askInt("ID Empréstimo: ", -1);
        int force = DataUtil.askInt("Forçar (0/1): ", 0);
        controller.returnBook(loanId, force);
        System.out.println("Devolução concluída.");
    }

    private void uiReport() {
        String name = DataUtil.ask("Nome do Relatório: ", "Relatório Geral");
        int mode = DataUtil.askInt("Modo (0/1): ", 1);
        String report = controller.generateReport(name, mode, 0, "");
        System.out.println(report);
    }

    private void showDebugMenu() {
        System.out.println("--- DEBUG ---");
        System.out.println("1-Logs | 2-Estado | 3-Mudar Modo | 4-Histograma | 0-Voltar");
        String opt = DataUtil.readLine("Opção Debug: ");
        
        switch (opt) {
            case "1":
                controller.printLogs();
                break;
            case "2":
                controller.dumpState();
                break;
            case "3":
                controller.setMode(DataUtil.readLine("Novo modo: "));
                break;
            case "4":
                controller.printHistogram();
                break;
            default:
                System.out.println("Voltando...");
                break;
        }
    }

    private void checkMaintenanceTasks() {
        if (menuCounter % 3 == 0) {
            LegacyDatabase.clearLogsIfTooBig();
        }
    }
}