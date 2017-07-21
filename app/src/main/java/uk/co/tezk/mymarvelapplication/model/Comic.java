package uk.co.tezk.mymarvelapplication.model;

/**
 * Created by tezk on 19/07/17.
 */

public class Comic {
    private String title;
    private String description;
    private int pageCount;
    private double price;
    private String thumbnail;
    private String authors;
    public Comic() {}

    public Comic(String title, String description, int pageCount, double price, String authors, String thumbnail) {
        this.title = title;
        this.description = description;
        this.pageCount = pageCount;
        this.price = price;
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }
}
