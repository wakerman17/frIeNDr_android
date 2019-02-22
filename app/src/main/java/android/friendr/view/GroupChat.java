package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.friendr.model.Message;
import android.friendr.model.User;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class GroupChat extends AppCompatActivity {

    private EditText userMessageInput;
    private LinearLayout messageView;
    Button btnSend, btnCreateEvent, btnFav, btnToLiveEvents;
    ScrollView mScrollView;
    String currentGroupName, currentUserID, currentUserName;
    String currentDate, currentTime;

    FirebaseAuth mAuth;
    DatabaseReference UsersRef, GroupNameRef, GroupMessageKeyRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);


        // todo pass in group name and edit title
        //currentGroupName = getIntent().getExtras().get(currentGroupName);
        currentGroupName = "Test Group Name";
        getSupportActionBar().setTitle(currentGroupName);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child("Chat");

        initFields();
        getUserInfo();



        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveMessageToDatabase();

                mScrollView.post(new Runnable() {
                    public void run() {
                        mScrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });

                userMessageInput.setText("");
                userMessageInput.requestFocus();
            }
        });

        btnToLiveEvents.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent nextWindow = new Intent(GroupChat.this, EventList.class);
                startActivity(nextWindow);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        getUserInfo();

        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()) {
                    displayAllMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()) {
                    displayAllMessages(dataSnapshot);
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


    private void getUserInfo() {
        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    //System.out.println(">>> Datasnap " + dataSnapshot.child("username").getValue());
                    currentUserName = dataSnapshot.child("username").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void initFields() {
        btnSend = findViewById(R.id.gr_send_button);
        btnCreateEvent = findViewById(R.id.gr_create_ev_button);
        btnFav = findViewById(R.id.gr_fav_button);
        btnToLiveEvents = findViewById(R.id.gr_to_live_events);
        messageView = findViewById(R.id.gr_message_view);
        userMessageInput = findViewById(R.id.gr_input_message);
        mScrollView = findViewById(R.id.gr_scroll_view);
    }



    private void SaveMessageToDatabase() {
        String txt_message = userMessageInput.getText().toString();
        String messageKey = GroupNameRef.push().getKey();

        if(TextUtils.isEmpty(txt_message)) {
            Toast.makeText(this, "Your message was empty", Toast.LENGTH_SHORT).show();
        } else {
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currDateFormat = new SimpleDateFormat("dd MMM, yyyy");
            currentDate = currDateFormat.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currTimeFormat = new SimpleDateFormat("HH:mm");
            currentTime = currTimeFormat.format(calForTime.getTime());


            HashMap<String, Object> groupMessageKey = new HashMap<>();
            GroupNameRef.updateChildren(groupMessageKey);

            GroupMessageKeyRef = GroupNameRef.child(messageKey);

            HashMap<String, Object> messageInfoMap = new HashMap<>();
                messageInfoMap.put("username", currentUserName);
                messageInfoMap.put("message", txt_message);
                messageInfoMap.put("date", currentDate);
                messageInfoMap.put("time", currentTime);
            GroupMessageKeyRef.updateChildren(messageInfoMap);
        }
    }



    public void toLiveEvents(View view) {
        Intent eventIntent = new Intent(this, EventChat.class);
        startActivity(eventIntent);
    }


    public void toCreateEvent(View view) {
        Intent createEventIntent = new Intent(this, CreateEvent.class);
        startActivity(createEventIntent);
    }



    private void displayAllMessages(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()) {
            String chatDate = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatUsername = (String) ((DataSnapshot) iterator.next()).getValue();

            displayMessage(chatDate, chatTime, chatUsername, chatMessage);

            mScrollView.post(new Runnable() {
                public void run() {
                    mScrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
    }

    public void displayMessage(String date, String Time, String username, String message) {
        if (username.equals(currentUserName)) {
            RelativeLayout my_message = (RelativeLayout) LayoutInflater.from(GroupChat.this).inflate(R.layout.my_message, null);
            TextView t = (TextView) my_message.getChildAt(0);
            t.setText(message);
            messageView.addView(my_message);
            //my_message.requestFocus();
        } else {
            RelativeLayout their_message = (RelativeLayout) LayoutInflater.from(GroupChat.this).inflate(R.layout.their_message, null);
            TextView name = (TextView) their_message.getChildAt(1);
            TextView msg = (TextView) their_message.getChildAt(2);
            name.setText(username);
            msg.setText(message);
            messageView.addView(their_message);
            //their_message.requestFocus();

        }


    }

}
