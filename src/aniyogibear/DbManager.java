package aniyogibear;

/**
 *
 * @author ani
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DbManager {

    int max;
    PreparedStatement insertStatement;
    PreparedStatement deleteStatement;
    Connection cn;
    private static DbManager db;

    private DbManager(int max) {
        try {
            this.max = max;
            String dbURL = "jdbc:mysql://localhost:3306/aniyogibear";
            cn = DriverManager.getConnection(dbURL, "root", "61152592@k");
            String insertQuery = "INSERT INTO highscores (NAME, SCORE) VALUES (?, ?)";
            insertStatement = cn.prepareStatement(insertQuery);
            String deleteQuery = "DELETE FROM highscores WHERE SCORE=?";
            deleteStatement = cn.prepareStatement(deleteQuery);
        } catch (SQLException ex) {
            System.out.println("SQL ERR: " + ex);
        }
    }

    public static DbManager getDbManager() {
        if (db == null) {
            db = new DbManager(10);
        }
        return db;
    }

    public ArrayList< MaxPoints> getHighScores() throws SQLException {
        String query = "select * from highscores";
        ArrayList< MaxPoints> highScores = new ArrayList<>();
        Statement stmt = cn.createStatement();
        ResultSet results = stmt.executeQuery(query);
        while (results.next()) {
            String name = results.getString("NAME");
            int score = results.getInt("SCORE");
            highScores.add(new MaxPoints(name, score));
        }
        sortHighScores(highScores);
        return highScores;
    }

    private void sortHighScores(ArrayList< MaxPoints> highScores) {
        Collections.sort(highScores, new Comparator< MaxPoints>() {
            @Override
            public int compare(MaxPoints t, MaxPoints t1) {
                return t1.getScore() - t.getScore();
            }
        });
    }

    public void insertScore(String name, int score) {
        try {
            ArrayList< MaxPoints> scores = getHighScores();
            if (scores.size() >= max) {
                int lowscore = scores.get(scores.size() - 1).getScore();
                deleteScores(lowscore);
            }
            insertStatement.setString(1, name);
            insertStatement.setInt(2, score);
            insertStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQL insert Err" + ex);
        }
    }

    private void deleteScores(int score) throws SQLException {
        deleteStatement.setInt(1, score);
        deleteStatement.executeUpdate();
    }
}
