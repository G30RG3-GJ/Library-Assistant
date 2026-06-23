package library.assistant.ui.listbook;

public class BookListController {
    public static class Book {
        private String id;
        private String title;
        private String author;
        private String publisher;

        public Book(String title, String id, String author, String publisher, Boolean isAvail) {
            this.title = title;
            this.id = id;
            this.author = author;
            this.publisher = publisher;
        }

        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getPublisher() { return publisher; }
    }
}
