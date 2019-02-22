package android.friendr.model;

import android.text.Editable;

public class Event {
    private Editable title;
    private Editable description;
    private Editable date;
    private Integer attendees;
    private Integer maxAttendees;

    public Event(Editable title, Editable description, Editable date, Integer maxAttendees) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.attendees = 0;
        this.maxAttendees = maxAttendees;
    }

    public Editable getTitle() {
        return title;
    }

    public void setTitle(Editable title) {
        this.title = title;
    }

    public Editable getDescription() {
        return description;
    }

    public void setDescription(Editable description) {
        this.description = description;
    }

    public Editable getDate() {
        return date;
    }

    public void setDate(Editable date) {
        this.date = date;
    }

    public Integer getAttendees() {
        return attendees;
    }

    public void setAttendees(Integer attendees) {
        this.attendees = attendees;
    }

    public Integer getMaxAttendees() {
        return maxAttendees;
    }

    public void setMaxAttendees(Integer maxAttendees) {
        this.maxAttendees = maxAttendees;
    }
}
