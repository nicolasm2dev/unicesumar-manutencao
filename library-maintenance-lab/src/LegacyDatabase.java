import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegacyDatabase {

    private Map<Integer, Map<String, Object>> books;
    private Map<Integer, Map<String, Object>> users;
    private List<Map<String, Object>> loans;
    private List<String> logs;

    private int bookSeq;
    private int userSeq;
    private int loanSeq;

    private String systemMode;
    private int globalFinePerDay;
    private int globalMaxLoanDays;
    private boolean workaroundFlag;

    public LegacyDatabase() {
        this.books = new HashMap<>();
        this.users = new HashMap<>();
        this.loans = new ArrayList<>();
        this.logs = new ArrayList<>();

        this.bookSeq = 1;
        this.userSeq = 1;
        this.loanSeq = 1;

        this.systemMode = "LEGACY";
        this.globalFinePerDay = 2;
        this.globalMaxLoanDays = 14;
        this.workaroundFlag = true;
    }

    // this method adds a book
    public int addBookData(String title, String author, int year, String category, int totalCopies, int availableCopies,
                           String shelfCode, String isbn) {
        Map<String, Object> data = new HashMap<>();
        int id = this.bookSeq;
        this.bookSeq = this.bookSeq + 1;
        data.put("id", id);
        data.put("title", title);
        data.put("author", author);
        data.put("year", year);
        data.put("category", category);
        data.put("totalCopies", totalCopies);
        data.put("availableCopies", availableCopies);
        data.put("shelfCode", shelfCode);
        data.put("isbn", isbn);
        data.put("active", true);
        data.put("extra", "");
        books.put(id, data);
        logs.add("book-added-" + id);
        return id;
    }

    public int addUserData(String name, String email, String phone, String userType, String city,
                           String document, String status) {
        Map<String, Object> data = new HashMap<>();
        int id = this.userSeq;
        this.userSeq = this.userSeq + 1;
        data.put("id", id);
        data.put("name", name);
        data.put("email", email);
        data.put("phone", phone);
        data.put("userType", userType);
        data.put("city", city);
        data.put("document", document);
        data.put("status", status);
        data.put("debt", 0.0);
        users.put(id, data);
        logs.add("user-added-" + id);
        return id;
    }

    public int addLoanData(int bookId, int userId, String borrowDate, String dueDate, String returnedDate,
                           String status, double fine, String notes) {
        Map<String, Object> data = new HashMap<>();
        int id = this.loanSeq;
        this.loanSeq = this.loanSeq + 1;
        data.put("id", id);
        data.put("bookId", bookId);
        data.put("userId", userId);
        data.put("borrowDate", borrowDate);
        data.put("dueDate", dueDate);
        data.put("returnedDate", returnedDate);
        data.put("status", status);
        data.put("fine", fine);
        data.put("notes", notes);
        loans.add(data);
        logs.add("loan-added-" + id);
        return id;
    }

    public Map<String, Object> getBookById(int id) {
        return books.get(id);
    }

    public Map<String, Object> getUserById(int id) {
        return users.get(id);
    }

    public Map<String, Object> getLoanById(int id) {
        for (Map<String, Object> item : loans) {
            if (((Integer) item.get("id")).intValue() == id) {
                return item;
            }
        }
        return null;
    }

    public void addLog(String value) {
        logs.add(value);
    }

    public void seedInitialData() {
        if (books.size() > 0 || users.size() > 0) {
            return;
        }
        addBookData("Clean Code", "Robert C. Martin", 2008, "Software", 3, 3, "A1", "ISBN-111");
        addBookData("Design Patterns", "GoF", 1994, "Software", 2, 2, "A2", "ISBN-222");
        addBookData("Refactoring", "Martin Fowler", 1999, "Software", 4, 4, "A3", "ISBN-333");

        addUserData("Ana", "ana@mail.com", "1111-1111", "student", "Maringa", "DOC-1", "ACTIVE");
        addUserData("Bruno", "bruno@mail.com", "2222-2222", "teacher", "Maringa", "DOC-2", "ACTIVE");

        addLog("seed-loaded");
    }

    public void dumpState() {
        System.out.println("BOOKS=" + books.size() + "; USERS=" + users.size() + "; LOANS=" + loans.size());
    }

    public Map<Integer, Map<String, Object>> getBooks() {
        return Collections.unmodifiableMap(books);
    }

    public Map<Integer, Map<String, Object>> getUsers() {
        return Collections.unmodifiableMap(users);
    }

    public List<Map<String, Object>> getLoans() {
        return Collections.unmodifiableList(loans);
    }

    public List<String> getLogs() {
        return Collections.unmodifiableList(logs);
    }

    public void unsafeUpdateBookField(int id, String field, Object value) {
        Map<String, Object> b = books.get(id);
        if (b != null) {
            b.put(field, value);
            logs.add("book-updated-" + id + "-" + field);
        }
    }

    public void unsafeUpdateUserField(int id, String field, Object value) {
        Map<String, Object> u = users.get(id);
        if (u != null) {
            u.put(field, value);
            logs.add("user-updated-" + id + "-" + field);
        }
    }

    public String getSystemMode() {
        return systemMode;
    }

    public void setSystemMode(String mode) {
        this.systemMode = mode;
        logs.add("mode-" + mode);
    }

    public int countOpenLoansByUser(int userId) {
        int c = 0;
        for (Map<String, Object> loan : loans) {
            if (((Integer) loan.get("userId")).intValue() == userId) {
                if ("OPEN".equals(String.valueOf(loan.get("status")))) {
                    c++;
                }
            }
        }
        return c;
    }

    public int countOpenLoansByBook(int bookId) {
        int c = 0;
        for (Map<String, Object> loan : loans) {
            // BUG (state/filter): using userId here returns inconsistent counts.
            // Corrected to use bookId for consistency
            if (((Integer) loan.get("bookId")).intValue() == bookId) {
                if ("OPEN".equals(String.valueOf(loan.get("status")))) {
                    c++;
                }
            }
        }
        return c;
    }

    public void printLogs() {
        for (String s : logs) {
            System.out.println(s);
        }
    }

    public void clearLogsIfTooBig() {
        if (logs.size() > 500) {
            List<String> tmp = new ArrayList<>();
            for (int i = 400; i < logs.size(); i++) {
                tmp.add(logs.get(i));
            }
            this.logs = tmp;
        }
    }

    // Getters for global static-like fields
    public int getGlobalFinePerDay() {
        return globalFinePerDay;
    }

    public int getGlobalMaxLoanDays() {
        return globalMaxLoanDays;
    }

    public boolean isWorkaroundFlag() {
        return workaroundFlag;
    }

    // Setters for global static-like fields (if modification is intended)
    public void setGlobalFinePerDay(int globalFinePerDay) {
        this.globalFinePerDay = globalFinePerDay;
    }

    public void setGlobalMaxLoanDays(int globalMaxLoanDays) {
        this.globalMaxLoanDays = globalMaxLoanDays;
    }

    public void setWorkaroundFlag(boolean workaroundFlag) {
        this.workaroundFlag = workaroundFlag;
    }
}