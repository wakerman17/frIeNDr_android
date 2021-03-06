package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.friendr.integration.DatabaseReturner;
import android.friendr.integration.InterestAndGroupDAO;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    InterestAndGroupDAO interestAndGroupDAO = new InterestAndGroupDAO();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        Intent intent = getIntent();
        if (null != intent) {
            currentUserID = intent.getStringExtra("currentUserID");
            currentGroupName = intent.getExtras().get("groupNamesForUser").toString();
        }
        getSupportActionBar().setTitle(currentGroupName);

        mAuth = FirebaseAuth.getInstance();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child("Chat");


        initFields();
        getUserInfo();
        addChat();


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
                Intent intent = new Intent(GroupChat.this, EventList.class);
                intent.putExtra("groupName", currentGroupName);
                intent.putExtra("currentUserID", currentUserID);
                startActivity(intent);
            }
        });
    }


    protected void addChat() {

        interestAndGroupDAO.chatListener(new DatabaseReturner(){
            @Override
            public void returner(DataSnapshot dataSnapshot) {
                displayAllMessages(dataSnapshot);
            }
        }, currentGroupName);
        mScrollView.post(new Runnable() {
            public void run() {
                mScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }


    private void getUserInfo() {
        interestAndGroupDAO.userInfo(new DatabaseReturner(){

            @Override
            public void returner(DataSnapshot dataSnapshot) {
                currentUserName = dataSnapshot.child("username").getValue().toString();
            }
        }, currentUserID);
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

            interestAndGroupDAO.saveMessage(currentUserName, currentGroupName, txt_message, currentDate, currentTime);
        }
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
        } else {
            RelativeLayout their_message = (RelativeLayout) LayoutInflater.from(GroupChat.this).inflate(R.layout.their_message, null);
            TextView name = (TextView) their_message.getChildAt(1);
            TextView msg = (TextView) their_message.getChildAt(2);
            name.setText(username);
            msg.setText(message);
            messageView.addView(their_message);
        }
    }
}
