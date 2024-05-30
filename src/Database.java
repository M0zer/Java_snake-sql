import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:sqlite:scores.db";

    public Database() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                String sql = "CREATE TABLE IF NOT EXISTS scores ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "name TEXT NOT NULL, "
                        + "score INTEGER NOT NULL)";
                conn.createStatement().execute(sql);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void saveScore(String name, int score) {
        String sql = "INSERT INTO scores(name, score) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Score> getTopScores(int limit) {
        List<Score> scores = new ArrayList<>();
        String sql = "SELECT name, score FROM scores ORDER BY score DESC LIMIT ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                scores.add(new Score(rs.getString("name"), rs.getInt("score")));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return scores;
    }
}
