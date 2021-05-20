package grabber;

import java.util.List;

/**
 * interface for parsing site
 */

public interface Parse {

    //method to load posts by link
    List<Post> list(String link);

    //method to load post detail by link
    Post detail(String link);
}
