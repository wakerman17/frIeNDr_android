package android.friendr.view;

import android.content.Context;
import android.content.Intent;
import android.friendr.R;
import android.friendr.view.viewObject.Event;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;

public class EventList extends AppCompatActivity {
    String currentGroupName;
    Button btnCreateEvent;
    LinearLayout eventListView;

    DatabaseReference EventRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        currentGroupName = getIntent().getExtras().get("groupName").toString();
        EventRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child("Events");

        initFields();
        getEvents();


        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent nextWindow = new Intent(EventList.this, CreateEvent.class);
                nextWindow.putExtra("groupName", currentGroupName);
                startActivity(nextWindow);
            }
        });

    }

    protected void getEvents() {

        EventRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()) {
                    displayAllEvents(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()) {
                    displayAllEvents(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void initFields() {
        btnCreateEvent = findViewById(R.id.ev_create_event_button);
        eventListView = findViewById(R.id.ev_list_view);
    }

    private void displayAllEvents(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();

        while (iterator.hasNext()) {
            final String evAtt = (String) ""+ ((DataSnapshot) iterator.next()).getChildrenCount();
            final String evDate = (String) ((DataSnapshot) iterator.next()).getValue();
            final String evDesc = (String) ((DataSnapshot) iterator.next()).getValue();
            final String evLoc = (String) ((DataSnapshot) iterator.next()).getValue();
            final String evMax = (String) ((DataSnapshot) iterator.next()).getValue();
            final String evTitle = (String) ((DataSnapshot) iterator.next()).getValue();
            String dbRef = evDate.replace("/", "") + evTitle;
            RelativeLayout myEvent = displayEvent(evDate, evTitle, evDesc, evAtt, evMax, dbRef);

            myEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(EventList.this,EventChat.class);
                    intent.putExtra("dbRef", evDate.replace("/", "") + evTitle);
                    intent.putExtra("eventName", evTitle);
                    intent.putExtra("eventDate", evDate);
                    intent.putExtra("eventDesc", evDesc);
                    intent.putExtra("eventLoc", evLoc);
                    intent.putExtra("eventAtt", evAtt);
                    intent.putExtra("eventMax", evMax);
                    intent.putExtra("groupName", currentGroupName);
                    startActivity(intent);
                }
            });

            eventListView.addView(myEvent);
        }
    }

    public RelativeLayout displayEvent(String date, final String title, String desc, String att, String max, final String dbRef) {
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
        ll.addView(btn_join);

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvent(title, dbRef);
            }
        });

        return my_event;
    }


    private void addEvent(final String currentEventName, String dbRef) {
        HashMap<String, Object> attendingUser = new HashMap<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserID = mAuth.getCurrentUser().getUid();
        attendingUser.put(currentUserID, "true");


        EventRef.child(dbRef).child("attendees").updateChildren(attendingUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
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
