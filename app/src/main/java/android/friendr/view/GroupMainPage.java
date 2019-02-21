package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GroupMainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main_page);

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
