package grabber;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {

    private Connection connection;

    public PsqlStore(Properties config) {
        try {
            Class.forName(config.getProperty("jdbc.driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(
                    config.getProperty("jdbc.url"),
                    config.getProperty("jdbc.login"),
                    config.getProperty("jdbc.password"));
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement ps = this.connection.prepareStatement("insert into post(name, text, link, created) values(?, ?, ?, ?)")) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getText());
            ps.setString(3, post.getLink());
            ps.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            ps.execute();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> postList = new ArrayList<>();
        try (PreparedStatement ps = this.connection.prepareStatement("select * from post")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    postList.add(new Post(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("text"),
                            rs.getString("link"),
                            rs.getTimestamp("created").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return postList;
    }

    @Override
    public Post findById(String id) {
        int postId = Integer.parseInt(id);
        Post post = new Post(postId, null, null, null, null);
        try (PreparedStatement ps = this.connection.prepareStatement("select * from post where id = ?")) {
            ps.setInt(1, postId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    post.setName(rs.getString("name"));
                    post.setText(rs.getString("text"));
                    post.setLink(rs.getString("link"));
                    post.setCreated(rs.getTimestamp("created").toLocalDateTime());
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public static void main(String[] args) {
        Properties config = new Properties();
        ClassLoader loader = PsqlStore.class.getClassLoader();
        try (InputStream io = loader.getResourceAsStream("grabber.properties")) {
            config.load(io);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PsqlStore store = new PsqlStore(config);
        Post postTwo = new Post("Вакансия Java-разработчик", "Писать java-код", "hh.ru/2", LocalDateTime.now());
        Post postThree = new Post("Вакансия Java-разработчик", "Писать java-код", "hh.ru/3", LocalDateTime.now());
        store.save(postTwo);
        store.save(postThree);
        System.out.println(store.getAll());
        System.out.println(store.findById("5"));
    }
}
