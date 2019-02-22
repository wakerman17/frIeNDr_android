package android.friendr.view;

import android.content.Context;
import android.content.Intent;
import android.friendr.controller.Controller;
import android.friendr.R;
import android.friendr.integration.DatabaseReturner;
import android.friendr.view.viewObject.Group;
import android.friendr.view.viewObject.InterestProfile;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class Index extends AppCompatActivity {

    int userID = 1;
    ArrayList<LinearLayout> linearLayoutArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Controller controller = new Controller();
        controller.getUserInterest(new DatabaseReturner() {

            @Override
            public void returner(DataSnapshot dataSnapshot) {
                final ProgressBar spinner = findViewById(R.id.progressBar1);
                LinearLayout parentLinearLayout = findViewById(R.id.parent_linear_layout_interest);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    View rowView = inflater.inflate(R.layout.fragment_interests, null, false);
                    InterestProfile interestProfile = postSnapshot.getValue(InterestProfile.class);
                    parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
                    TextView interest_text = parentLinearLayout.findViewById(R.id.interest);
                    interest_text.setText(interestProfile.getInterest());
                    interest_text.setId((int)(Math.random() * 1000 + 1));
                }
                spinner.setVisibility(View.GONE);
            }
        }, userID);

        startClickListener(R.id.add_interests, FindNewInterest.class);
        controller.getUserGroup(new DatabaseReturner() {

            @Override
            public void returner(DataSnapshot dataSnapshot) {
                final ProgressBar spinner = findViewById(R.id.progressBar2);
                LinearLayout parentLinearLayout = findViewById(R.id.parent_linear_layout_group);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                int i = 0;

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    View rowView = inflater.inflate(R.layout.fragment_group, null, false);
                    final Group group = postSnapshot.getValue(Group.class);
                    parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
                    TextView group_text = parentLinearLayout.findViewById(R.id.group);
                    group_text.setText(group.getName());
                    final Switch switch_view = parentLinearLayout.findViewById(R.id.group_switch);
                    final LinearLayout linearLayout = parentLinearLayout.findViewById(R.id.group_goto);
                    switch_view.setTag(Integer.valueOf(i));
                    linearLayoutArrayList.add(linearLayout);
                    Button buttonGeneralPage = parentLinearLayout.findViewById(R.id.to_general_page);
                    Button buttonGeneralChat = parentLinearLayout.findViewById(R.id.to_general_chat);

                    buttonGeneralPage.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            Intent nextWindow = new Intent(Index.this, GroupMainPage.class);
                            nextWindow.putExtra("group", group);
                            startActivity(nextWindow);
                        }
                    });

                    buttonGeneralChat.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            Intent nextWindow = new Intent(Index.this, GroupChat.class);
                            startActivity(nextWindow);
                        }
                    });
                    i++;
                }
                spinner.setVisibility(View.GONE);
            }
        }, userID);
        startClickListener(R.id.find_new_group, FindNewGroup.class);
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


    public void toggle_group_goto(View view) {
        Integer tag = (Integer) view.getTag();
        int tagi = tag.intValue();
        LinearLayout linearLayoutToShow = linearLayoutArrayList.get(tagi);
        linearLayoutToShow.setVisibility( linearLayoutToShow.isShown()
                ? View.GONE
                : View.VISIBLE );
    }
}
