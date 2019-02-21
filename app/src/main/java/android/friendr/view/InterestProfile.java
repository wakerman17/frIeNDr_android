package android.friendr.view;

public class InterestProfile {

    private int id;
    private String interest;
    private int user_id;

    public InterestProfile() {}

    public InterestProfile(int id, String interest, int user_id) {
        this.id = id;
        this.interest = interest;
        this.user_id = user_id;
    }

    String getInterest() {
        return interest;
    }
}
