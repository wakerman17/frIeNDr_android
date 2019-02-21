package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

public class CreateEvent extends AppCompatActivity {

    EditText titleInput;
    EditText descriptionInput;
    EditText maxAttendeesInput;
    MyEditTextDatePicker date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        date = new MyEditTextDatePicker(this, R.id.event_date);
        titleInput = findViewById(R.id.title);
        descriptionInput = findViewById(R.id.description);
        maxAttendeesInput = findViewById(R.id.maxAttendees);
    }


    public void createEvent(View view) {
        Editable title = titleInput.getText();
        Editable description = descriptionInput.getText();
        Editable evDate = date._editText.getText();
        Integer maxAtt = Integer.getInteger(String.valueOf(maxAttendeesInput.getText()));

        Event e = new Event(title, description, evDate, maxAtt);
        // todo: do something with the event  (show on "events page")
        System.out.println(e.getTitle() +": "+e.getDescription()+", "+e.getDate());
    }

    public void toEvent(View view) {
        Intent eventIntent = new Intent(this, EventChat.class);
        startActivity(eventIntent);
    }


    public void toCreateEvent(View view) {
        Intent createEventIntent = new Intent(this, CreateEvent.class);
        startActivity(createEventIntent);
    }

}


