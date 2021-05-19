import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * class for get vacancy list by link
 * @since 19/05/2021
 */

public class SqlRuParse implements Parse {

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> postsList = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            String url = link + "/" + i;
            Document doc = Jsoup.connect(url).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                postsList.add(detail(href.attr("href")));
            }
        }
        return postsList;
    }

    @Override
    public Post detail(String link) throws IOException {
        Post post;
        Document doc = Jsoup.connect(link).get();
        String title = doc.selectFirst(".messageHeader").text();
        Element postDescription = doc.getElementsByClass("msgBody").get(1);
        String description = postDescription.text();
        Element date = doc.getElementsByClass("msgFooter").get(0);
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        String parseDate = date.text().split("\\[")[0].trim();
        LocalDateTime createdDate = parser.parse(parseDate);
        post = new Post(link, title, description, createdDate);
        return post;
    }
}
