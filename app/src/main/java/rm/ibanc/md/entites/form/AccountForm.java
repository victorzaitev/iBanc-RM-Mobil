package rm.ibanc.md.entites.form;

/**
 * Created by PC01017745 on 21.10.2016.
 */
public class AccountForm {
    private String title;
    private String genre;
    private String year;

    public AccountForm() {
    }

    public AccountForm(String title, String genre, String year) {
        this.title = title;
        this.genre = genre;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
