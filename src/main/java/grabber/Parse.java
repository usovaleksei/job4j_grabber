package grabber;

import java.io.IOException;
import java.util.List;

/**
 * interface for parsing site
 */

public interface Parse {

    //method to load posts by link
    List<Post> list(String link) throws IOException;

    //method to load post detail by link
    Post detail(String link) throws IOException;
}
