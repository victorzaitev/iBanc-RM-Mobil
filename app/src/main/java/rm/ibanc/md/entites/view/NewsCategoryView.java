package rm.ibanc.md.entites.view;

/**
 * Created by Cepraga Mihail on 1/2/17.
 */
public class NewsCategoryView {
    private String nameNews;
    private String numOfNews;
    private int thumbnail;

    public NewsCategoryView() {
    }

    public NewsCategoryView(String nameNews, String numOfNews, int thumbnail) {
        this.nameNews = nameNews;
        this.numOfNews = numOfNews;
        this.thumbnail = thumbnail;
    }


    public String getNameNews() {
        return nameNews;
    }

    public void setNameNews(String nameNews) {
        this.nameNews = nameNews;
    }

    public String getNumOfNews() {
        return numOfNews;
    }

    public void setNumOfNews(String numOfNews) {
        this.numOfNews = numOfNews;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
