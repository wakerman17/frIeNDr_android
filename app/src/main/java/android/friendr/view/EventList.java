package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Iterator;

public class EventList extends AppCompatActivity {
    Button btnCreateEvent;
    LinearLayout eventListView;

    DatabaseReference EventRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        // TODO add group coupling
        String groupName = "Test Group Name";
        EventRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName).child("Events");

        initFields();

        // todo add click listeners to list items (events)

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent nextWindow = new Intent(EventList.this, CreateEvent.class);
                startActivity(nextWindow);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

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
            String evDate = (String) ((DataSnapshot) iterator.next()).getValue();
            String evDesc = (String) ((DataSnapshot) iterator.next()).getValue();
            String evMax = (String) ((DataSnapshot) iterator.next()).getValue();
            String evTitle = (String) ((DataSnapshot) iterator.next()).getValue();

            displayEvent(evDate, evTitle, evDesc, evMax);
        }
    }

    public void displayEvent(String date, String title, String desc, String max) {
        RelativeLayout my_event = (RelativeLayout) LayoutInflater.from(EventList.this).inflate(R.layout.event, null);
        LinearLayout ll = (LinearLayout) my_event.getChildAt(0);
        System.out.println(">>> Children: " + ll.getChildCount());
        TextView ev_date = (TextView) ll.getChildAt(0);
        TextView ev_title = (TextView) ll.getChildAt(1);
        TextView ev_max = (TextView) ll.getChildAt(2);
        ev_date.setText(date);
        ev_title.setText(title);
        ev_max.setText(max);
        eventListView.addView(my_event);
    }

}
