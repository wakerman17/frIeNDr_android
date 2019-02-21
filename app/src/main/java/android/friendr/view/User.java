package android.friendr.view;

public class User {

    private String username;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
