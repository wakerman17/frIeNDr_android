package android.friendr.view;

import android.content.Context;
import android.content.Intent;
import android.friendr.R;
import android.friendr.controller.Controller;
import android.friendr.integration.DatabaseReturner;
import android.friendr.view.viewObject.Group;
import android.friendr.view.viewObject.Interests;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashSet;

public class FindNewGroup extends AppCompatActivity {

    LinearLayout filterView, groupInfo1, groupInfo2, groupResult;
    TextView min_members_txt, max_members_txt;
    EditText min_members, max_members;
    Button searchGroup, createNewGroup;
    Controller controller = new Controller();
    Spinner spinner;
    SearchView searchView;
    ArrayList<LinearLayout> linearLayoutArrayList = new ArrayList<>();
    String currentUserID;
    HashSet<String> groupNamesForUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_new_group);

        Intent intent = getIntent();
        if (null != intent) {
            currentUserID = (String) intent.getSerializableExtra("currentUserID");
            groupNamesForUser = (HashSet<String>) intent.getSerializableExtra("groupNamesForUser");
        }

        groupResult = findViewById(R.id.group_result);
        filterView = findViewById(R.id.filter_view);
        createNewGroup = findViewById(R.id.create_new_group);
        searchGroup = findViewById(R.id.search_group_button);
        searchView = findViewById(R.id.search_group);

        createNewGroup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent nextWindow = new Intent(FindNewGroup.this, CreateGroup.class);
                nextWindow.putExtra("currentUserID", currentUserID);
                startActivity(nextWindow);
            }
        });
        searchGroup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String searchText = searchView.getQuery().toString();
                if (!searchText.equals("")) {
                    controller.getGroupSearchResult(new DatabaseReturner() {

                        @Override
                        public void returner(DataSnapshot dataSnapshot) {
                            //controller.getInterestSearchResult();
                            if (dataSnapshot.getValue() != null) {
                                LinearLayout parentLinearLayout = findViewById(R.id.group_result);
                                //parentLinearLayout.removeAllViews();
                                final ProgressBar spinner = findViewById(R.id.progress_bar);
                                parentLinearLayout.setVisibility(View.VISIBLE);
                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                int i = 0;
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    View rowView = inflater.inflate(R.layout.fragment_new_group, null, false);
                                    final Group group = postSnapshot.getValue(Group.class);
                                    LinearLayout result = parentLinearLayout.findViewById(R.id.result);
                                    if (result != null) {
                                        result.removeAllViews();
                                    }
                                    if(groupNamesForUser.contains(group.getName())) {
                                        continue;
                                    }
                                    parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);

                                    TextView groupName = parentLinearLayout.findViewById(R.id.group_name_fragment);

                                    groupName.setText(group.getName());
                                    TextView description = parentLinearLayout.findViewById(R.id.group_description_fragment);
                                    description.setText(group.getDescription());
                                    final Switch switch_view = parentLinearLayout.findViewById(R.id.group_switch_fragment);
                                    final LinearLayout linearLayout = parentLinearLayout.findViewById(R.id.group_info);
                                    Button joinGroup = parentLinearLayout.findViewById(R.id.join_group);
                                    joinGroup.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            controller.addUserGroup(group.getName(), group.getDescription(), group.getInterest_base(), currentUserID);
                                            Intent nextWindow = new Intent(FindNewGroup.this, Index.class);
                                            startActivity(nextWindow);
                                        }
                                    });
                                    switch_view.setTag(Integer.valueOf(i));
                                    linearLayoutArrayList.add(linearLayout);
                                    i++;
                                }
                                spinner.setVisibility(View.GONE);
                            } else {
                                Log.d("d::::", "else");
                                LinearLayout parentLinearLayout = findViewById(R.id.group_result);
                                LinearLayout result = parentLinearLayout.findViewById(R.id.result);
                                if (result != null) {
                                    parentLinearLayout.removeView(result);
                                }
                            }
                        }
                    }, 1, searchText);
                } else {
                    LinearLayout parentLinearLayout = findViewById(R.id.group_result);
                    LinearLayout result = parentLinearLayout.findViewById(R.id.result);
                    int i = 0;
                    if (result != null) {
                        parentLinearLayout.removeView(result);
                    }

                    result = parentLinearLayout.findViewById(R.id.result);
                    if (result != null) {
                        parentLinearLayout.removeView(result);
                    }
                }
            }
        });
    }
    /**
     * onClick handler
     */
    public void toggle_filter(View v){
        toggleContent(filterView);
    }

    public void toggle_group_info_1(View v){
        toggleContent(groupInfo1);
    }

    private void toggleContent (LinearLayout linearLayout) {
        linearLayout.setVisibility( linearLayout.isShown()
                ? View.GONE
                : View.VISIBLE );
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
