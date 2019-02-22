package android.friendr.view.viewObject;

import java.io.Serializable;

public class Group implements Serializable {

    private int amount_of_members;
    private String description;
    private String interest;
    private String name;
    private int user_id;

    public Group() {}

    public Group(int amount_of_members, String description, String interest, String name, int user_id) {
        this.amount_of_members = amount_of_members;
        this.description = description;
        this.interest = interest;
        this.name = name;
        this.user_id = user_id;
    }

    public int getAmountOfMembers() {
        return amount_of_members;
    }

    public String getDescription() {
        return description;
    }

    public String getInterest() {
        return interest;
    }

    public String getName() {
        return name;
    }

    public int getUserID() {
        return user_id;
    }
}
