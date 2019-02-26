package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.friendr.view.viewObject.Group;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GroupMainPage extends AppCompatActivity {
    String groupNameString;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main_page);

        final TextView groupName = findViewById(R.id.group_name);
        TextView groupDescription = findViewById(R.id.group_description);


        Intent intent = getIntent();
        if (null != intent) {
            currentUserID = intent.getStringExtra("currentUserID");
            Group group = (Group) intent.getSerializableExtra("group");
            groupNameString = group.getName();
            groupName.setText(groupNameString);
            groupDescription.setText(group.getDescription());
        }

        Button createEventButton = findViewById(R.id.create_event_button);
        createEventButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent nextWindow = new Intent(GroupMainPage.this, CreateEvent.class);
                nextWindow.putExtra("groupName", groupNameString);
                startActivity(nextWindow);
            }
        });

        Button buttonGeneralChat = findViewById(R.id.to_general_chat);
        buttonGeneralChat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent nextWindow = new Intent(GroupMainPage.this, GroupChat.class);
                nextWindow.putExtra("currentUserID", currentUserID);
                nextWindow.putExtra("groupNamesForUser", groupNameString);
                startActivity(nextWindow);
            }
        });
        Button btnToLiveEvents = findViewById(R.id.gr_to_live_events);
        btnToLiveEvents.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(GroupMainPage.this, EventList.class);
                intent.putExtra("groupName", groupNameString);
                startActivity(intent);
            }
        });
    }

    public void toGroup(View view) {
        Intent eventIntent = new Intent(this, GroupChat.class);
        startActivity(eventIntent);
    }
}
