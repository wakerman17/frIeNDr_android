package android.friendr.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.friendr.R;
import android.friendr.integration.DatabaseReturner;
import android.friendr.integration.InterestAndGroupDAO;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashSet;

public class Index extends AppCompatActivity {

    int userID = 1;
    ArrayList<LinearLayout> linearLayoutArrayList = new ArrayList<>();
    String currentUserID;
    HashSet<String> interestNamesForUser = new HashSet<>();
    InterestAndGroupDAO interestAndGroupDAO = new InterestAndGroupDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        interestAndGroupDAO.getUserInterest(new DatabaseReturner() {

            @Override
            public void returner(final DataSnapshot dataSnapshot) {
                final ProgressBar spinner = findViewById(R.id.progressBar1);
                LinearLayout parentLinearLayout = findViewById(R.id.parent_linear_layout_interest);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    View rowView = inflater.inflate(R.layout.fragment_interests, null, false);
                    InterestProfile interestProfile = postSnapshot.getValue(InterestProfile.class);
                    parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
                    TextView interest_text = parentLinearLayout.findViewById(R.id.interest);
                    String interestName = interestProfile.getInterest();
                    interest_text.setText(interestName);
                    interest_text.setId((int)(Math.random() * 1000 + 1));
                    interestNamesForUser.add(interestName);
                }
                spinner.setVisibility(View.GONE);
                Button button = findViewById(R.id.add_interests);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Intent nextWindow = new Intent(Index.this, FindNewInterest.class);
                        nextWindow.putExtra("interestNamesForUser", interestNamesForUser);
                        startActivity(nextWindow);
                    }
                });
            }
        }, currentUserID);

        startClickListener(R.id.add_interests, FindNewInterest.class);
        interestAndGroupDAO.getUserGroup(new DatabaseReturner() {

            @Override
            public void returner(DataSnapshot dataSnapshot) {
                final ProgressBar spinner = findViewById(R.id.progressBar2);
                LinearLayout parentLinearLayout = findViewById(R.id.parent_linear_layout_group);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                int i = 0;
                final HashSet<String> groupNamesForUser = new HashSet<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    View rowView = inflater.inflate(R.layout.fragment_group, null, false);
                    final Group group = postSnapshot.getValue(Group.class);
                    parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
                    TextView group_text = parentLinearLayout.findViewById(R.id.group);
                    final String groupName = group.getName();
                    group_text.setText(groupName);
                    groupNamesForUser.add(groupName);
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
                            nextWindow.putExtra("currentUserID", currentUserID);
                            nextWindow.putExtra("group", group);

                            startActivity(nextWindow);
                        }
                    });

                    buttonGeneralChat.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            Intent nextWindow = new Intent(Index.this, GroupChat.class);
                            nextWindow.putExtra("currentUserID", currentUserID);
                            nextWindow.putExtra("groupNamesForUser", groupName);
                            startActivity(nextWindow);
                        }
                    });

                    group_text.setId((int) Math.random() * 1000 + 1);
                    switch_view.setId((int) Math.random() * 1000 + 1);
                    linearLayout.setId((int) Math.random() * 1000 + 1);
                    buttonGeneralPage.setId((int) Math.random() * 1000 + 1);
                    buttonGeneralChat.setId((int) Math.random() * 1000 + 1);

                    i++;
                }
                Button button = findViewById(R.id.find_new_group);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Intent nextWindow = new Intent(Index.this, FindNewGroup.class);
                        nextWindow.putExtra("currentUserID", currentUserID);
                        nextWindow.putExtra("groupNamesForUser", groupNamesForUser);
                        nextWindow.putExtra("interestNamesForUser", interestNamesForUser);
                        startActivity(nextWindow);
                    }
                });
                spinner.setVisibility(View.GONE);
            }
        }, currentUserID);
        startClickListener(R.id.find_new_group, FindNewGroup.class);
    }

    private void startClickListener(@IdRes int id, final java.lang.Class newWindowClass) {
        Button button = findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent nextWindow = new Intent(Index.this, newWindowClass);
                nextWindow.putExtra("currentUserID", currentUserID);
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
