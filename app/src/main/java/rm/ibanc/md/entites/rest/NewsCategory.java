package rm.ibanc.md.entites.rest;

/**
 * Created by victor.zaitev on 01.12.2016.
 */
public class NewsCategory {

    private String nameCategory;
    private String numberOfNews;
    private String imagePath;

    public NewsCategory() {
    }

    public NewsCategory(String nameCategory, String numberOfNews, String imagePath) {
        this.nameCategory = nameCategory;
        this.numberOfNews = numberOfNews;
        this.imagePath = imagePath;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getNumberOfNews() {
        return numberOfNews;
    }

    public void setNumberOfNews(String numberOfNews) {
        this.numberOfNews = numberOfNews;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
