import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;

public class SqlRuParse {

    private void getVacancyInfo(String href) throws IOException {
        Document doc = Jsoup.connect(href).get();
        Element postDescription = doc.getElementsByClass("msgBody").get(1);
        String description = postDescription.text();
        Element date = doc.getElementsByClass("msgFooter").get(0);
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        String parseDate = date.text().split("\\[")[0].trim();
        LocalDateTime createdDate = parser.parse(parseDate);
        System.out.println("Описание вакансии: ");
        System.out.println(description);
        System.out.println("Дата создания вакансии: ");
        System.out.println(createdDate);
    }

    public static void main(String[] args) throws IOException {
        for (int i = 1; i < 6; i++) {
            String url = "https://www.sql.ru/forum/job-offers" + "/" + i;
            Document doc = Jsoup.connect(url).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                System.out.println(href.attr("href"));
                System.out.println(href.text());
                System.out.println(td.parent().child(5).text());
            }
        }
    }
}
