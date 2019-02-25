package android.friendr.view.viewObject;

import java.io.Serializable;

public class Group implements Serializable {

    private String description, interest_base, name, user_id;

    public Group() {}

    public Group(String description, String interest, String name, String user_id) {
        this.description = description;
        this.interest_base = interest;
        this.name = name;
        this.user_id = user_id;
    }


    public String getDescription() {
        return description;
    }

    public String getInterest_base() {
        return interest_base;
    }

    public String getName() {
        return name;
    }

    public String getUser_id() {
        return user_id;
    }
}
