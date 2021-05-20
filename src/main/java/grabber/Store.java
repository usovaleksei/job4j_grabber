package grabber;

import java.util.List;

/**
 * interface for work with DB
 */

public interface Store {

    //method to save post to DB
    void save(Post post);

    //method to getting all posts from DB
    List<Post> getAll();

    //method to find post in DB by ID
    Post findById(String id);
}
