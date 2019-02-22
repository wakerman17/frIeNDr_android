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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main_page);

        TextView groupName = findViewById(R.id.group_name);
        TextView groupDescription = findViewById(R.id.group_description);

        Intent intent = getIntent();
        if (null != intent) {
            Group group = (Group) intent.getSerializableExtra("group");
            groupName.setText(group.getName());
            groupDescription.setText(group.getDescription());
        }

        Button createEventButton = findViewById(R.id.create_event_button);
        createEventButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent nextWindow = new Intent(GroupMainPage.this, CreateEvent.class);
                startActivity(nextWindow);
            }
        });
    }

    public void toGroup(View view) {
        Intent eventIntent = new Intent(this, GroupChat.class);
        startActivity(eventIntent);
    }

    public void toEvent(View view) {
        Intent eventIntent = new Intent(this, EventChat.class);
        startActivity(eventIntent);
    }

}
