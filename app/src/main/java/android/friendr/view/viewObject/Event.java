package android.friendr.view.viewObject;

import android.text.Editable;

import java.io.Serializable;

public class Event implements Serializable {
    private String attendees;
    private String date;
    private String description;
    private String location;
    private String max;
    private String title;
    private String groupName;

    public Event() {}

    public Event(String attendees, String date, String description, String location, String max, String title, String groupName) {
        this.attendees = attendees;
        this.date = date;
        this.description = description;
        this.location = location;
        this.max = max;
        this.title = title;
        this.groupName = groupName;
    }

    public void setAttendees(String attendees) {
        this.attendees = attendees;
    }

    public String getAttendees() {
        return attendees;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getMax() {
        return max;
    }

    public String getTitle() {
        return title;
    }

    public String getGroupName() {
        return groupName;
    }
}
