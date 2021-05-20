package grabber;

import java.sql.*;
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
        try (PreparedStatement ps = this.connection.prepareStatement("insert into post(name, text, link, created) values(?, ?, ?, ?) on conflict do nothing")) {
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
}
