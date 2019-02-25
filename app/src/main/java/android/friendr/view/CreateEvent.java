package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.friendr.view.viewObject.Event;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateEvent extends AppCompatActivity {

    EditText titleInput;
    EditText descriptionInput;
    EditText maxAttendeesInput;
    MyEditTextDatePicker date;
    Button btnCreateEvent;

    FirebaseAuth mAuth;
    DatabaseReference EventRef, RootRef;
    String currentUserID, currentGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        currentGroupName = getIntent().getExtras().get("groupName").toString();

        date = new MyEditTextDatePicker(this, R.id.ev_date);
        titleInput = findViewById(R.id.ev_title);
        descriptionInput = findViewById(R.id.ev_description);
        maxAttendeesInput = findViewById(R.id.ev_max);
        btnCreateEvent = findViewById(R.id.ev_save_button);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        EventRef = FirebaseDatabase.getInstance().getReference().child("Events");

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_title = titleInput.getText().toString();
                String txt_desc = descriptionInput.getText().toString();
                String txt_date = date._editText.getText().toString();
                String txt_max = maxAttendeesInput.getText().toString();

                if (TextUtils.isEmpty(txt_date) || TextUtils.isEmpty(txt_desc) ||
                        TextUtils.isEmpty(txt_date) || TextUtils.isEmpty(txt_max)) {
                    Toast.makeText(CreateEvent.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                } else {
                    createNewEvent(txt_title, txt_desc, txt_date, txt_max);
                }
            }
        });
    }

    private void createNewEvent(final String txt_title, String txt_desc, String txt_date, String txt_max) {
        HashMap<String, String> newEvent = new HashMap<>();
        newEvent.put("title", txt_title);
        newEvent.put("description", txt_desc);
        newEvent.put("date", txt_date);
        newEvent.put("attendees", "0");
        newEvent.put("max", txt_max);

        RootRef.child("Groups").child(currentGroupName).child("Events").child(txt_date.replace("/","")+txt_title).setValue(newEvent)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateEvent.this, txt_title + " event created!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CreateEvent.this, EventList.class);
                            intent.putExtra("groupName", currentGroupName);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(CreateEvent.this, "Something went wrong! Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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


