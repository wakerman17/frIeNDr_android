package android.friendr.view;

import android.friendr.R;
import android.friendr.view.viewObject.Event;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;


public class EventChat extends AppCompatActivity {

    private String eventDBRef;
    private String currentUserID;
    private String currentGroupName, currentEventDate, currentEventName, currentEventDesc, currentEventAtt, currentEventMax, currentEventLoc;
    private TextView eventDescripion, eventAttending, eventDate, eventLoc, eventMax;
    private Button btn_attending;
    private LinearLayout messageView;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_chat);
        setup();
    }

    private void setup() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        event = (Event) getIntent().getSerializableExtra("event");
        currentUserID = mAuth.getCurrentUser().getUid();

        currentGroupName = event.getGroupName(); //getIntent().getExtras().get("groupName").toString();
        currentEventDate = event.getDate();//getIntent().getExtras().get("eventDate").toString();
        currentEventName = event.getTitle();//getIntent().getExtras().get("eventName").toString();
        currentEventDesc = event.getDescription();//getIntent().getExtras().get("eventDesc").toString();
        currentEventLoc = event.getLocation();//getIntent().getExtras().get("eventLoc").toString();
        currentEventAtt = event.getAttendees();//getIntent().getExtras().get("eventAtt").toString();
        currentEventMax = event.getMax();//getIntent().getExtras().get("eventMax").toString();
        eventDBRef = getIntent().getExtras().get("dbRef").toString();

        eventDescripion = findViewById(R.id.head_ev_desc);
        eventAttending = findViewById(R.id.head_ev_att);
        eventMax = findViewById(R.id.head_ev_maxAtt);
        eventDate = findViewById(R.id.head_ev_dat);
        eventLoc = findViewById(R.id.head_ev_loc);
        messageView = findViewById(R.id.ev_list_view);


        eventDescripion.setText(currentEventDesc);
        eventAttending.setText(currentEventAtt);
        eventMax.setText(currentEventMax);
        eventDate.setText(currentEventDate);
        eventLoc.setText(currentEventLoc);

        getSupportActionBar().setTitle(currentEventName);

    }

}
