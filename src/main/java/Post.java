import java.time.LocalDateTime;

public class Post {

    private String link;
    private String vacancyTitle;
    private String vacancyDescription;
    private LocalDateTime date;

    public Post(String link, String vacancyTitle, String vacancyDescription, LocalDateTime date) {
        this.link = link;
        this.vacancyTitle = vacancyTitle;
        this.vacancyDescription = vacancyDescription;
        this.date = date;
    }

    public String getVacancyTitle() {
        return vacancyTitle;
    }

    public void setVacancyTitle(String vacancyTitle) {
        this.vacancyTitle = vacancyTitle;
    }

    public String getVacancyDescription() {
        return vacancyDescription;
    }

    public void setVacancyDescription(String vacancyDescription) {
        this.vacancyDescription = vacancyDescription;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Post{"
                + "link='" + link + '\''
                + ", vacancyTitle='" + vacancyTitle + '\''
                + ", vacancyDescription='" + vacancyDescription + '\''
                + ", date=" + date
                + '}';
    }
}
