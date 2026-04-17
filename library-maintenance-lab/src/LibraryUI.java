public class LibraryUI {

    private final LibraryController controller;
    private boolean running = true;

    public LibraryUI(LibraryController controller) {
        this.controller = controller;
    }

    public void start() {
        DataUtil.printHeader("Library System");

        while (running) {
            showMenu();
            String option = DataUtil.readLine("Select option: ");

            try {
                switch (option) {
                    case "1":
                        handleRegisterBook();
                        break;
                    case "2":
                        handleRegisterUser();
                        break;
                    case "3":
                        handleBorrowBook();
                        break;
                    case "4":
                        handleReturnBook();
                        break;
                    case "5":
                        controller.listBooks();
                        break;
                    case "6":
                        handleGenerateReport();
                        break;
                    case "7":
                        controller.listUsers();
                        break;
                    case "8":
                        controller.listLoans();
                        break;
                    case "9":
                        handleDebug();
                        break;
                    case "10":
                        handleReserveBook(); // 🔥 NOVA FUNCIONALIDADE
                        break;
                    case "0":
                        running = false;
                        System.out.println("bye");
                        break;
                    default:
                        System.out.println("invalid option");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private void showMenu() {
        DataUtil.printSeparator();
        System.out.println("1 - Register book");
        System.out.println("2 - Register user");
        System.out.println("3 - Borrow book");
        System.out.println("4 - Return book");
        System.out.println("5 - List books");
        System.out.println("6 - Generate report");
        System.out.println("7 - List users");
        System.out.println("8 - List loans");
        System.out.println("9 - Debug area");
        System.out.println("10 - Reserve book"); // 🔥 NOVO
        System.out.println("0 - Exit");
        DataUtil.printSeparator();
    }

    private void handleRegisterBook() {
        String title = DataUtil.readLine("Title: ");
        String author = DataUtil.readLine("Author: ");
        int year = DataUtil.askInt("Year: ", 2000);
        String category = DataUtil.ask("Category: ", "GENERAL");
        int total = DataUtil.askInt("Total copies: ", 1);
        int available = DataUtil.askInt("Available copies: ", total);
        String shelf = DataUtil.ask("Shelf: ", "X0");
        String isbn = DataUtil.ask("ISBN: ", "NO-ISBN");

        BookRequest req = new BookRequest(title, author, year, category, total, available, shelf, isbn);

        int id = controller.registerBook(req);
        System.out.println("Book registered with id " + id);
    }

    private void handleRegisterUser() {
        String name = DataUtil.readLine("Name: ");
        String email = DataUtil.readLine("Email: ");
        String phone = DataUtil.readLine("Phone: ");
        String type = DataUtil.ask("Type: ", "student");
        String city = DataUtil.ask("City: ", "Unknown");
        String document = DataUtil.ask("Document: ", "NO-DOC");
        String status = DataUtil.ask("Status: ", "ACTIVE");

        int id = controller.registerUser(name, email, phone, type, city, document, status);
        System.out.println("User registered with id " + id);
    }

    private void handleBorrowBook() {
        int userId = DataUtil.askInt("User ID: ", -1);
        int bookId = DataUtil.askInt("Book ID: ", -1);

        int loanId = controller.borrowBook(userId, bookId);
        System.out.println("Loan created with id " + loanId);
    }

    private void handleReturnBook() {
        int loanId = DataUtil.askInt("Loan ID: ", -1);
        int forceFlag = DataUtil.askInt("Force flag (0/1/2): ", 0);

        controller.returnBook(loanId, forceFlag);
        System.out.println("Return completed");
    }


    private void handleReserveBook() {
        int userId = DataUtil.askInt("User ID: ", -1);
        int bookId = DataUtil.askInt("Book ID: ", -1);

        controller.reserveBook(userId, bookId);
    }

    private void handleGenerateReport() {
        String name = DataUtil.ask("Report name: ", "Report");
        int mode = DataUtil.askInt("Mode (0/1): ", 1);
        int year = DataUtil.askInt("Year filter: ", 0);
        String category = DataUtil.ask("Category: ", "");

        String report = controller.generateReport(name, mode, year, category);
        System.out.println(report);
    }

    private void handleDebug() {
        DataUtil.printHeader("Debug");

        System.out.println("1 - Print logs");
        System.out.println("2 - Print state");
        System.out.println("3 - Change mode");
        System.out.println("4 - Histogram");
        System.out.println("5 - Notify");
        System.out.println("0 - Back");

        String option = DataUtil.readLine("Option: ");

        switch (option) {
            case "1":
                controller.printLogs();
                break;
            case "2":
                controller.dumpState();
                break;
            case "3":
                String mode = DataUtil.readLine("New mode: ");
                controller.setMode(mode);
                break;
            case "4":
                controller.printHistogram();
                break;
            case "5":
                String x = DataUtil.ask("x: ", "x");
                String y = DataUtil.ask("y: ", "y");
                String z = DataUtil.ask("z: ", "z");
                int p = DataUtil.askInt("priority: ", 1);
                int r = DataUtil.askInt("retry: ", 0);
                controller.sendGenericNotify(x, y, z, p, r);
                break;
            case "0":
                break;
            default:
                System.out.println("invalid option");
        }
    }
}