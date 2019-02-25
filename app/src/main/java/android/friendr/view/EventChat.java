package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.friendr.view.viewObject.Event;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class EventChat extends AppCompatActivity {

    private String eventDBRef;
    private String currentUserID;
    private String currentGroupName, currentEventName, currentEventDesc, currentEventAtt, currentEventMax, currentEventLoc;
    private TextView eventDescripion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_chat);

        currentGroupName = getIntent().getExtras().get("groupName").toString();
        currentEventName = getIntent().getExtras().get("eventDate").toString();
        currentEventName = getIntent().getExtras().get("eventName").toString();
        currentEventDesc = getIntent().getExtras().get("eventDesc").toString();
        currentEventLoc = getIntent().getExtras().get("eventLoc").toString();
        currentEventLoc = getIntent().getExtras().get("eventAtt").toString();
        currentEventLoc = getIntent().getExtras().get("eventMax").toString();
        eventDBRef = getIntent().getExtras().get("dbRef").toString();

        eventDescripion = findViewById(R.id.ev_desc);
        eventDescripion.setText(currentEventDesc);

        getSupportActionBar().setTitle(currentEventName);

    }

}
