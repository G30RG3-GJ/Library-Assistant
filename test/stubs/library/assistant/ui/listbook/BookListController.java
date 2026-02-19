package library.assistant.ui.listbook;

public class BookListController {
    public static class Book {
        private String id;
        private String title;
        private String author;
        private String publisher;

        public Book(String id, String title, String author, String publisher) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.publisher = publisher;
        }

        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getPublisher() { return publisher; }
    }
}
