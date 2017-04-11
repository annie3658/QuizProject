package annie.com.quizproject.model;

/**
 * Created by Annie on 11/04/2017.
 */

public class User {

    String userId;
    String username;
    String language;
    String score;

    public User()
    {

    }

    public User(String userId, String username, String language, String score) {
        this.userId = userId;
        this.username = username;
        this.language = language;
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getLanguage() {
        return language;
    }

    public String getScore() {
        return score;
    }
}
