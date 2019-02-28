package android.friendr.view.viewObject;

public class Interest {

    private String name, user_id;

    public Interest() {}

    public Interest(String name, String user_id) {

        this.name = name;
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public String getUser_id() {
        return user_id;
    }
}
