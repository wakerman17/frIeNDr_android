package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GroupChat extends AppCompatActivity {

    private EditText editText;
    private LinearLayout messageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        messageView = findViewById(R.id.message_view);
        editText = findViewById(R.id.editText);
    }

    public void toLiveEvents(View view) {
        Intent eventIntent = new Intent(this, EventChat.class);
        startActivity(eventIntent);
    }


    public void toCreateEvent(View view) {
        Intent createEventIntent = new Intent(this, CreateEvent.class);
        startActivity(createEventIntent);
    }


    public void sendMessage(View view) {
        Editable message = editText.getText();
        System.out.println(message);

        if (message.length() > 0) {
            RelativeLayout my_message = (RelativeLayout) LayoutInflater.from(GroupChat.this).inflate(R.layout.my_message, null);
            TextView t = (TextView) my_message.getChildAt(0);
            t.setText(message);
            messageView.addView(my_message);
            my_message.requestFocus();
            editText.getText().clear();
            editText.requestFocus();
        }

    }

}
