package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.friendr.view.viewObject.Event;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;


public class EventChat extends AppCompatActivity {

    private String eventDBRef;
    private String currentUserID;
    private String currentGroupName, currentEventDate, currentEventName, currentEventDesc, currentEventAtt, currentEventMax, currentEventLoc;
    private TextView eventDescripion, eventAttending, eventDate, eventLoc, eventMax;
    private Button btn_attending;
    private LinearLayout messageView;
    DatabaseReference UserRef, RootRef, EventRef;
    int numOfAttendees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_chat);

        RootRef = FirebaseDatabase.getInstance().getReference();

        setup();
        EventRef = RootRef.child("Groups").child(currentGroupName).child("Events").child(eventDBRef);

        btn_attending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvent();
            }
        });

    }


    private void addEvent() {
        HashMap<String, Object> attendingUser = new HashMap<>();
        attendingUser.put(currentUserID, "");


        EventRef.child("attendees").updateChildren(attendingUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EventChat.this, "Joined " + currentEventName + "!", Toast.LENGTH_SHORT).show();
                            btn_attending.setText("Attending");
                        } else {
                            Toast.makeText(EventChat.this, "Something went wrong! Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void setup() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        currentGroupName = getIntent().getExtras().get("groupName").toString();
        currentEventDate = getIntent().getExtras().get("eventDate").toString();
        currentEventName = getIntent().getExtras().get("eventName").toString();
        currentEventDesc = getIntent().getExtras().get("eventDesc").toString();
        currentEventLoc = getIntent().getExtras().get("eventLoc").toString();
        currentEventAtt = getIntent().getExtras().get("eventAtt").toString();
        currentEventMax = getIntent().getExtras().get("eventMax").toString();
        eventDBRef = getIntent().getExtras().get("dbRef").toString();

        eventDescripion = findViewById(R.id.head_ev_desc);
        eventAttending = findViewById(R.id.head_ev_att);
        eventMax = findViewById(R.id.head_ev_maxAtt);
        eventDate = findViewById(R.id.head_ev_dat);
        eventLoc = findViewById(R.id.head_ev_loc);
        btn_attending = findViewById(R.id.ev_attending);
        messageView = findViewById(R.id.ev_list_view);


        eventDescripion.setText(currentEventDesc);
        eventAttending.setText(currentEventAtt);
        eventMax.setText(currentEventMax);
        eventDate.setText(currentEventDate);
        eventLoc.setText(currentEventLoc);

        getSupportActionBar().setTitle(currentEventName);

    }

}
