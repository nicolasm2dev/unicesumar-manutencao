
public class BookRequest {
    private final String title;
    private final String author;
    private final int year;
    private final String category;
    private final int total;
    private final int available;
    private final String shelfCode;
    private final String isbn;

    public BookRequest(String title, String author, int year, String category, 
                       int total, int available, String shelfCode, String isbn) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.category = category;
        this.total = total;
        this.available = available;
        this.shelfCode = shelfCode;
        this.isbn = isbn;
    }

    public String title() { return title; }
    public String author() { return author; }
    public int year() { return year; }
    public String category() { return category; }
    public int total() { return total; }
    public int available() { return available; }
    public String shelfCode() { return shelfCode; }
    public String isbn() { return isbn; }
}