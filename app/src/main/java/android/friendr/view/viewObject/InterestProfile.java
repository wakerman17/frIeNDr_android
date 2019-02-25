package android.friendr.view.viewObject;

public class InterestProfile {

    private String interest;
    private String user_id;

    public InterestProfile() {}

    public InterestProfile(int id, String interest, String user_id) {
        this.interest = interest;
        this.user_id = user_id;
    }

    public String getInterest() {
        return interest;
    }

    public String getUser_id() {
        return user_id;
    }
}
