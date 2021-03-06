package android.friendr.view;

import android.content.Context;
import android.content.Intent;
import android.friendr.R;
import android.friendr.integration.DatabaseReturner;
import android.friendr.integration.InterestAndGroupDAO;
import android.friendr.view.viewObject.Group;
import android.friendr.view.viewObject.InterestProfile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashSet;

public class FindNewGroup extends AppCompatActivity {

    LinearLayout filterView, groupInfo1, groupInfo2, groupResult;
    TextView min_members_txt, max_members_txt;
    EditText min_members, max_members;
    Button searchGroup, createNewGroup;
    InterestAndGroupDAO interestAndGroupDAO = new InterestAndGroupDAO();
    Spinner spinner;
    SearchView searchView;
    ArrayList<LinearLayout> linearLayoutArrayList = new ArrayList<>();
    String currentUserID;
    HashSet<String> groupNamesForUser, interestNamesForUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_new_group);

        Intent intent = getIntent();
        if (null != intent) {
            currentUserID = intent.getStringExtra("currentUserID");
            groupNamesForUser = (HashSet<String>) intent.getSerializableExtra("groupNamesForUser");
            interestNamesForUser = (HashSet<String>) intent.getSerializableExtra("interestNamesForUser");
        }

        groupResult = findViewById(R.id.group_result);
        filterView = findViewById(R.id.filter_view);
        createNewGroup = findViewById(R.id.create_new_group);
        searchGroup = findViewById(R.id.search_group_button);
        searchView = findViewById(R.id.search_group);

        spinner = findViewById(R.id.interest_spinner);
        interestAndGroupDAO.getUserInterest(new DatabaseReturner(){

            @Override
            public void returner(DataSnapshot dataSnapshot) {
                final ArrayList<String> interestList = new ArrayList<>();
                interestList.add("");
                interestList.addAll(interestNamesForUser);
                final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(FindNewGroup.this, R.layout.spinner_item, interestList);

                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                spinner.setAdapter(spinnerArrayAdapter);
            }
        }, currentUserID);

        createNewGroup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent nextWindow = new Intent(FindNewGroup.this, CreateGroup.class);
                nextWindow.putExtra("currentUserID", currentUserID);
                nextWindow.putExtra("interestNamesForUser", interestNamesForUser);
                startActivity(nextWindow);
            }
        });
        searchGroup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String searchText = searchView.getQuery().toString();
                if (!searchText.equals("")) {
                    interestAndGroupDAO.getGroupSearchResult(new DatabaseReturner() {

                        @Override
                        public void returner(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                LinearLayout parentLinearLayout = findViewById(R.id.group_result);
                                //final ProgressBar progressBar = findViewById(R.id.progress_bar);
                                parentLinearLayout.setVisibility(View.VISIBLE);
                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                int i = 0;
                                String choosenInterest = spinner.getSelectedItem().toString();
                                boolean noSpecificInterest = false;
                                if(choosenInterest.equals("")) {
                                    noSpecificInterest = true;
                                }
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    final Group group = postSnapshot.getValue(Group.class);
                                    if(groupNamesForUser.contains(group.getName())) {
                                        continue;
                                    } else if ((noSpecificInterest && interestNamesForUser.contains(group.getInterest_base())) || interestNamesForUser.contains(choosenInterest)) {
                                    } else if (!group.getInterest_base().equals(choosenInterest)) {
                                        LinearLayout result = parentLinearLayout.findViewById(R.id.result);
                                        if (result != null) {
                                            parentLinearLayout.removeView(result);
                                        }
                                        continue;
                                    }
                                    View rowView = inflater.inflate(R.layout.fragment_new_group, null, false);
                                    LinearLayout result = parentLinearLayout.findViewById(R.id.result);

                                    if (result != null) {
                                        result.removeAllViews();
                                    }
                                    parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());

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
                                            interestAndGroupDAO.addUserGroup(group.getName(), group.getDescription(), group.getInterest_base(), currentUserID);
                                            Intent nextWindow = new Intent(FindNewGroup.this, Index.class);
                                            startActivity(nextWindow);
                                        }
                                    });
                                    switch_view.setTag(Integer.valueOf(i));
                                    linearLayoutArrayList.add(linearLayout);
                                    i++;
                                }
                                //progressBar.setVisibility(View.GONE);
                            } else {
                                Log.d("d::::", "else");
                                LinearLayout parentLinearLayout = findViewById(R.id.group_result);
                                LinearLayout result = parentLinearLayout.findViewById(R.id.result);
                                if (result != null) {
                                    parentLinearLayout.removeView(result);
                                }
                                parentLinearLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }, currentUserID, searchText);
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
