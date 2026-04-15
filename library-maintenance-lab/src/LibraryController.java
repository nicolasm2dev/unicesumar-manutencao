
public class LibraryController {
    private final BookManager bookManager = new BookManager();
    private final UserManager userManager = new UserManager();
    private final LoanManager loanManager = new LoanManager();
    private final ReportGenerator reportGenerator = new ReportGenerator();
    private final NotificationService notificationService = new NotificationService();

    public int registerBook(BookRequest req) {
        if (DataUtil.isBlank(req.title()) || DataUtil.isBlank(req.author())) {
            throw new RuntimeException("Título ou autor não podem estar vazios.");
        }

        int id = bookManager.registerBook(
            req.title(), req.author(), req.year(), req.category(),
            req.total(), req.available(), req.shelfCode(), req.isbn()
        );

        String logMsg = (id % 2 == 0) ? "book-even-id" : "book-odd-id";
        LegacyDatabase.addLog(logMsg);
        
        return id;
    }

    public int registerUser(String name, String email, String phone, String type, 
                            String city, String document, String status) {
        return userManager.registerUser(name, email, phone, type, city, document, status);
    }

    public int borrowBook(int userId, int bookId) {
        String borrowDate = DataUtil.nowDate();
        String dueDate = DataUtil.datePlusDaysApprox(borrowDate, 14);
        return loanManager.borrowBook(userId, bookId, borrowDate, dueDate, "email", 14, "main", 0);
    }

    public void returnBook(int loanId, int forceFlag) {
        String returnDate = DataUtil.nowDate();
        loanManager.returnBook(loanId, returnDate, "email", forceFlag, "main", "handle");
    }

    public void listBooks() {
        bookManager.listBooksSimple();
    }

    public void listUsers() {
        userManager.listUsers();
    }

    public void listLoans() {
        loanManager.listAllLoans();
    }

    public String generateReport(String name, int mode, int year, String category) {
        return reportGenerator.generateSimpleReport(name, mode, "manager", "helper", year, category);
    }

    public void runDemo() {
        try {
            int idBook = bookManager.registerBook("Legacy Java", "Unknown", 2010, "CS", 2, 2, "B1", "ISBN-999");
            int idUser = userManager.registerUser("Carlos", "carlos@mail.com", "3333-3333", "student", "Maringa", "DOC-3", "ACTIVE");
            int loanId = borrowBook(idUser, idBook);
            returnBook(loanId, 0);
        } catch (Exception e) {
            LegacyDatabase.addLog("demo-error-" + e.getMessage());
        }
    }

    public ReportGenerator getReportGenerator() {
        return reportGenerator;
    }

    public void printLogs() { LegacyDatabase.printLogs(); }
    public void dumpState() { LegacyDatabase.dumpState(); }
    public void setMode(String mode) { LegacyDatabase.setSystemMode(mode); }
    public void printHistogram() { reportGenerator.printLoanHistogram(); }
    public void sendGenericNotify(String x, String y, String z, int p, int r) {
        notificationService.genericNotify(x, y, z, p, r, "debug");
    }
}