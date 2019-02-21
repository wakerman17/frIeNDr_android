package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Index extends AppCompatActivity {

    LinearLayout groupGoto1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        startClickListener(R.id.find_new_group, FindNewGroup.class);
        startClickListener(R.id.to_general_page, GroupMainPage.class);
        startClickListener(R.id.add_interests, FindNewInterest.class);

        groupGoto1 = findViewById(R.id.group_goto_1);
    }

    private void startClickListener(@IdRes int id, final java.lang.Class newWindowClass) {
        Button button = findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent nextWindow = new Intent(Index.this, newWindowClass);
                startActivity(nextWindow);
            }
        });
    }

    public void toEvent(View view) {
        Intent eventIntent = new Intent(this, EventChat.class);
        startActivity(eventIntent);
    }


    public void toMyGroup(View view) {
        Intent groupChatIntent = new Intent(this, GroupChat.class);
        startActivity(groupChatIntent);
    }


    public void toggle_group_goto_1(View v) {
        groupGoto1.setVisibility( groupGoto1.isShown()
                ? View.GONE
                : View.VISIBLE );
    }
}
