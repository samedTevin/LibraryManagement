package model;

public class Book {
    private int id;
    private String title;
    private String ImageSrc;
    private String author;
    private int count;
    private int rating;
    
    public Book() {}

    public Book(int id, String title, String author, String imageSrc, int count, int rating) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.ImageSrc = imageSrc;
        this.count = count;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageSrc() {
        return ImageSrc;
    }

    public void setImageSrc(String imageSrc) {
        ImageSrc = imageSrc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
