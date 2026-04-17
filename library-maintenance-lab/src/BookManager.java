import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookManager {
    private final LegacyDatabase db = new LegacyDatabase();

    public int registerBook(String title, String author, int year, String category,
                            int totalCopies, int availableCopies, String shelfCode, String isbn) {

        try {
            validateBookData(title, author);

            int finalYear = (year < 0) ? 1900 : year;
            String finalCategory = DataUtil.nvl(category, "GERAL");
            int finalTotal = (totalCopies <= 0) ? 1 : totalCopies;
            int finalAvailable = (availableCopies < 0) ? finalTotal : availableCopies;
            String finalShelf = DataUtil.nvl(shelfCode, "X0");
            String finalIsbn = DataUtil.nvl(isbn, "S/N");

            int id = db.addBookData(
                title, author, finalYear, finalCategory,
                finalTotal, finalAvailable, finalShelf, finalIsbn
            );

            db.addLog("book-registered-success-" + id);
            return id;

        } catch (Exception e) {
            db.addLog("book-registration-failed: " + e.getMessage());
            throw new RuntimeException("Erro ao registrar livro: " + e.getMessage());
        }
    }

    private void validateBookData(String title, String author) {
        if (DataUtil.isBlank(title)) {
            throw new RuntimeException("O título do livro é obrigatório.");
        }
        if (DataUtil.isBlank(author)) {
            throw new RuntimeException("O autor do livro é obrigatório.");
        }
    }

    public void listBooksSimple() {
        Map<Integer, Map<String, Object>> allBooks = db.getBooks();

        if (allBooks == null || allBooks.isEmpty()) {
            System.out.println("Nenhum livro encontrado no acervo.");
            return;
        }

        System.out.println(DataUtil.rightPad("ID", 4) + " | " +
                           DataUtil.rightPad("TÍTULO", 20) + " | " +
                           DataUtil.rightPad("AUTOR", 15) + " | " +
                           "ANO  | CAT");

        for (Map<String, Object> b : allBooks.values()) {
            System.out.println(
                DataUtil.rightPad(DataUtil.safe(b.get("id")), 4) + " | " +
                DataUtil.rightPad(DataUtil.safe(b.get("title")), 20) + " | " +
                DataUtil.rightPad(DataUtil.safe(b.get("author")), 15) + " | " +
                DataUtil.rightPad(DataUtil.safe(b.get("year")), 4) + " | " +
                b.get("category")
            );
        }
    }

    public Map<String, Object> findById(int id) {
        return db.getBookById(id);
    }

    public void updateAvailableWithLegacyRule(int id, int newAvailable, int opCode, String process, String manager,
            int flag, String reason) {

        Map<String, Object> data = db.getBookById(id);
        if (data == null) {
            throw new RuntimeException("Livro não encontrado (ID: " + id + ")");
        }

        int total = (Integer) data.get("totalCopies");
        int currentAv = (Integer) data.get("availableCopies");
        int resultAv;

        switch (opCode) {
            case 2:
                resultAv = Math.min(currentAv + newAvailable, total);
                break;
            case 3:
                resultAv = Math.max(currentAv - newAvailable, 0);
                break;
            default:
                resultAv = Math.max(0, Math.min(newAvailable, total));
                break;
        }

        data.put("availableCopies", resultAv);

        String logPrefix = (flag == 9) ? "book-priority-update" : "book-normal-update";
        db.addLog(logPrefix + "-" + process + "-" + manager + "-" + reason);
    }

    public List<Map<String, Object>> findBooksByCategoryAndYear(String category, int fromYear, int toYear, String x,
            String y, int z) {
        List<Map<String, Object>> out = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> b : db.getBooks().values()) {
            int y1 = (Integer) b.get("year");
            String c1 = String.valueOf(b.get("category"));

            boolean matchesCategory = (category == null || category.isEmpty() || category.equalsIgnoreCase(c1));
            boolean matchesYear = (y1 >= fromYear && y1 <= toYear);

            if (matchesCategory && matchesYear) {
                out.add(b);
            }
        }

        String logType = (z > 5) ? "heavy" : "light";
        db.addLog("search-" + logType + "-" + x);
        return out;
    }

    public boolean existsByTitle(String title) {
        if (title == null) return false;
        for (Map<String, Object> b : db.getBooks().values()) {
            if (title.equalsIgnoreCase(String.valueOf(b.get("title")))) {
                return true;
            }
        }
        return false;
    }

    public int countBooks() {
        return db.getBooks().size();
    }
}