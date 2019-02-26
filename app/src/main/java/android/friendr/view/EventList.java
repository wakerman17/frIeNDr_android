package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.friendr.integration.DatabaseReturner;
import android.friendr.integration.InterestAndGroupDAO;
import android.friendr.view.viewObject.Event;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class EventList extends AppCompatActivity {
    String currentGroupName, currentUserID;
    Button btnCreateEvent;
    LinearLayout eventListView;

    DatabaseReference eventRef;
    InterestAndGroupDAO interestAndGroupDAO = new InterestAndGroupDAO();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Intent intent = getIntent();
        if (null != intent) {
            currentUserID = intent.getStringExtra("currentUserID");
        }

        currentGroupName = getIntent().getExtras().get("groupName").toString();
        eventRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child("Events");

        initFields();
        getEvents();


        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent nextWindow = new Intent(EventList.this, CreateEvent.class);
                nextWindow.putExtra("groupName", currentGroupName);
                nextWindow.putExtra("currentUserID", currentUserID);
                startActivity(nextWindow);
            }
        });

    }

    protected void getEvents() {
        interestAndGroupDAO.getEvents(new DatabaseReturner(){
            @Override
            public void returner(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    displayAllEvents(dataSnapshot);
                }
            }
        }, currentGroupName);
    }


    private void initFields() {
        btnCreateEvent = findViewById(R.id.ev_create_event_button);
        eventListView = findViewById(R.id.ev_list_view);
    }

    private void displayAllEvents(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();

        while (iterator.hasNext()) {
            DataSnapshot attendees = (DataSnapshot) iterator.next();
            boolean alreadyAttended = false;
            for(DataSnapshot attendee : attendees.getChildren()) {
                if(attendee.getValue().equals(currentUserID)) {
                    alreadyAttended = true;
                    break;
                }
            }
            final String evAtt = (String) ""+ attendees.getChildrenCount();
            final String evDate = (String) ((DataSnapshot) iterator.next()).getValue();
            final String evDesc = (String) ((DataSnapshot) iterator.next()).getValue();
            final String evLoc = (String) ((DataSnapshot) iterator.next()).getValue();
            final String evMax = (String) ((DataSnapshot) iterator.next()).getValue();
            final String evTitle = (String) ((DataSnapshot) iterator.next()).getValue();
            String dbRef = evDate.replace("/", "") + evTitle;
            RelativeLayout myEvent = displayEvent(evDate, evTitle, evDesc, evAtt, evMax, dbRef, alreadyAttended);

            myEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(EventList.this,EventChat.class);
                    Event event = new Event(evAtt, evDate, evDesc, evLoc, evMax, evTitle, currentGroupName);
                    intent.putExtra("event",event);
                    intent.putExtra("dbRef", evDate.replace("/", "") + evTitle);
                    startActivity(intent);
                }
            });
            eventListView.addView(myEvent);
        }
    }

    public RelativeLayout displayEvent(String date, final String title, String desc, String att, String max, final String dbRef, boolean alreadyAttended) {
        RelativeLayout my_event = (RelativeLayout) LayoutInflater.from(EventList.this).inflate(R.layout.event, null);
        Button btn_join = new Button(my_event.getContext());
        btn_join.setText("Join");
        LinearLayout ll = (LinearLayout) my_event.getChildAt(0);
        TextView ev_date = (TextView) ll.getChildAt(0);
        final TextView ev_title = (TextView) ll.getChildAt(1);
        TextView ev_max = (TextView) ll.getChildAt(2);
        ev_date.setText(date);
        ev_title.setText(title);
        ev_max.setText(att + "/" + max);
        if(!alreadyAttended) {
            ll.addView(btn_join);

            btn_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RelativeLayout event = (RelativeLayout) view.getParent().getParent();
                    LinearLayout parentLinearLayout = findViewById(R.id.ev_list_view);
                    event.removeAllViews();
                    addAttendee(title, dbRef);
                }
            });
        }

        return my_event;
    }


    private void addAttendee(final String currentEventName, String dbRef) {
        DatabaseReference databaseUser = eventRef.child(dbRef).child("attendees").push();
        String postId = databaseUser.getKey();
        HashMap<String, Object> attendingUser = new HashMap<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        attendingUser.put(postId, currentUserID);
        eventRef.child(dbRef).child("attendees").updateChildren(attendingUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EventList.this, "Joined " + currentEventName + "!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EventList.this, "Something went wrong! Try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
