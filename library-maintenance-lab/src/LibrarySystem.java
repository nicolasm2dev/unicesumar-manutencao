
public class LibrarySystem {
    private final LibraryController controller;
    private final LibraryUI ui;

    public LibrarySystem() {
        LegacyDatabase.seedInitialData();
        this.controller = new LibraryController();
        this.ui = new LibraryUI(controller);
    }

   
    public void startCli() {
        ui.start();
    }

    public void runDemoScenario() {
        controller.runDemo();
    }

    public void handleListBooks() {
        controller.listBooks();
    }

    public void handleListUsers() {
        controller.listUsers();
    }

    public void handleListLoans() {
        controller.listLoans();
    }

    public ReportGenerator getReportGenerator() {
        return controller.getReportGenerator();
    }
}