import java.util.List;
import java.util.Map;

public class LoanManager {

    private final NotificationService notificationService;
    private final LegacyDatabase db;

    public LoanManager(NotificationService notificationService, LegacyDatabase db) {
        this.notificationService = notificationService;
        this.db = db;
    }

    public LoanManager() {
        this(new NotificationService(), new LegacyDatabase());
    }


    public int borrowBook(int userId, int bookId, String borrowDate, String dueDate,
                          String channel, int maxDays, String process, int policyCode) {
        try {
            Map<String, Object> user = db.getUserById(userId);
            Map<String, Object> book = db.getBookById(bookId);

            validateBorrowRequirements(user, book, userId, bookId);

            if (DataUtil.isBlank(borrowDate)) borrowDate = DataUtil.nowDate();
            if (DataUtil.isBlank(dueDate))    dueDate = DataUtil.datePlusDaysApprox(borrowDate, maxDays);

            int loanId = db.addLoanData(bookId, userId, borrowDate, dueDate, "", "OPEN", 0.0, "loan-created");

            decrementAvailableCopies(book);
            notificationService.notifyLoanCreated(userId, bookId, borrowDate, dueDate, channel, "TPL1", "manager");
            logPolicy(policyCode, process);
            db.addLog("loan-created-ok-" + loanId);

            return loanId;

        } catch (Exception e) {
            db.addLog("borrow-error-" + e.getMessage());
            throw new RuntimeException("Cannot borrow book now: " + e.getMessage());
        }
    }


    private void validateBorrowRequirements(Map<String, Object> user, Map<String, Object> book,
                                            int userId, int bookId) {
        if (user == null)
            throw new RuntimeException("User not found");

        if (book == null)
            throw new RuntimeException("Book not found");

        if (!"ACTIVE".equals(String.valueOf(user.get("status"))))
            throw new RuntimeException("User not active");

        if ((double) user.get("debt") > 100.0)
            throw new RuntimeException("User debt too high");

        if ((int) book.get("availableCopies") <= 0)
            throw new RuntimeException("No available copies");

        if (db.countOpenLoansByUser(userId) >= 5)
            throw new RuntimeException("User has too many open loans");

        int totalCopies = (int) book.get("totalCopies");
        if (db.countOpenLoansByBook(bookId) >= totalCopies)
            throw new RuntimeException("No book copies by open loan count");
    }

    private void decrementAvailableCopies(Map<String, Object> book) {
        int current = (int) book.get("availableCopies");
        book.put("availableCopies", current - 1);
    }

    private void logPolicy(int policyCode, String process) {
        String label = switch (policyCode) {
            case 7  -> "7";
            case 8  -> "8";
            default -> "default";
        };
        db.addLog("loan-policy-" + label + "-" + process);
    }


    public void returnBook(int loanId, String returnedDate, String channel,
                           int forceFlag, String process, String handler) {

        Map<String, Object> loan = db.getLoanById(loanId);

        if (loan == null) {
            db.addLog("loan-not-found-ignored-" + loanId);
            throw new RuntimeException("Loan não encontrado");
        }

        if (!"OPEN".equals(String.valueOf(loan.get("status"))))
            throw new RuntimeException("Loan already closed");

        int userId = (int) loan.get("userId");
        int bookId = (int) loan.get("bookId");

        Map<String, Object> user = db.getUserById(userId);
        Map<String, Object> book = db.getBookById(bookId);

        if (user == null || book == null)
            throw new RuntimeException("User/book missing for return");

        if (DataUtil.isBlank(returnedDate)) returnedDate = DataUtil.nowDate();

        double fine = calculateFine(String.valueOf(loan.get("dueDate")), returnedDate, forceFlag, process, handler, userId, bookId);

        loan.put("returnedDate", returnedDate);
        loan.put("status", "CLOSED");
        loan.put("fine", fine);

        incrementAvailableCopies(book);
        applyFineToUserDebt(user, fine);

        notificationService.notifyReturn(userId, bookId, "CLOSED", fine, channel);
        db.addLog("loan-return-ok-" + loanId + "-" + process + "-" + handler);
    }

    private void incrementAvailableCopies(Map<String, Object> book) {
        int current = (int) book.get("availableCopies");
        int total   = (int) book.get("totalCopies");
        book.put("availableCopies", Math.min(current + 1, total));
    }

    private void applyFineToUserDebt(Map<String, Object> user, double fine) {
        if (fine <= 0) return;
        double currentDebt = (double) user.get("debt");
        user.put("debt", currentDebt + fine);
    }


    public double calculateFine(String dueDate, String returnedDate, int forceFlag,
                                String process, String helper, int userId, int bookId) {
        double fine = computeRawFine(dueDate, returnedDate, forceFlag);

        sendDebtAlertIfNeeded(userId, fine, process);

        String parity = (bookId % 2 == 0) ? "even" : "odd";
        db.addLog("fine-book-" + parity + "-" + helper);

        return fine;
    }

    private double computeRawFine(String dueDate, String returnedDate, int forceFlag) {
        if (dueDate == null || returnedDate == null) return 0.0;
        if (returnedDate.compareTo(dueDate) <= 0)   return 0.0;

        int days = DataUtil.daysBetween(dueDate, returnedDate);

        return switch (forceFlag) {
            case 1  -> 0.0;
            case 2  -> days * 1.0;
            default -> days * db.getGlobalFinePerDay();
        };
    }


    private void sendDebtAlertIfNeeded(int userId, double fine, String process) {
        if (fine > 100) {
            notificationService.sendDebtAlert(userId, fine, 2, process);
        } else if (fine > 50) {
            notificationService.sendDebtAlert(userId, fine, 3, process);
        }
    }



    public void listOpenLoans() {
        System.out.println("ID | USER | BOOK | BORROW | DUE | STATUS | FINE");
        for (Map<String, Object> loan : db.getLoans()) {
            if ("OPEN".equals(String.valueOf(loan.get("status")))) {
                printLoanRow(loan, false);
            }
        }
    }

    public void listAllLoans() {
        System.out.println("ID | USER | BOOK | BORROW | DUE | RETURNED | STATUS | FINE");
        for (Map<String, Object> loan : db.getLoans()) {
            printLoanRow(loan, true);
        }
    }

    private void printLoanRow(Map<String, Object> loan, boolean includeReturned) {
        StringBuilder sb = new StringBuilder();
        sb.append(loan.get("id")).append(" | ")
                .append(loan.get("userId")).append(" | ")
                .append(loan.get("bookId")).append(" | ")
                .append(loan.get("borrowDate")).append(" | ")
                .append(loan.get("dueDate")).append(" | ");
        if (includeReturned) sb.append(loan.get("returnedDate")).append(" | ");
        sb.append(loan.get("status")).append(" | ")
                .append(loan.get("fine"));
        System.out.println(sb);
    }

    public void borrowFromConsole() {
        int    userId     = DataUtil.askInt("User ID: ", -1);
        int    bookId     = DataUtil.askInt("Book ID: ", -1);
        String borrowDate = DataUtil.ask("Borrow date (yyyy-MM-dd): ", DataUtil.nowDate());
        String dueDate    = DataUtil.ask("Due date (yyyy-MM-dd): ", DataUtil.datePlusDaysApprox(borrowDate, 14));
        String channel    = DataUtil.ask("Channel (email/sms): ", "email");
        int    maxDays    = DataUtil.askInt("Max days: ", 14);
        int    policyCode = DataUtil.askInt("Policy code: ", 0);

        int loanId = borrowBook(userId, bookId, borrowDate, dueDate, channel, maxDays, "cli", policyCode);
        System.out.println("Loan created with id " + loanId);
    }

    public void returnFromConsole() {
        int    loanId       = DataUtil.askInt("Loan ID: ", -1);
        String returnedDate = DataUtil.ask("Returned date (yyyy-MM-dd): ", DataUtil.nowDate());
        String channel      = DataUtil.ask("Channel (email/sms): ", "email");
        int    forceFlag    = DataUtil.askInt("Force flag (0/1/2): ", 0);

        returnBook(loanId, returnedDate, channel, forceFlag, "cli", "handler");
        System.out.println("Return processed");
    }
}