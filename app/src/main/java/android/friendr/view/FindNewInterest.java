package android.friendr.view;

import android.content.Context;
import android.content.Intent;
import android.friendr.R;
import android.friendr.controller.Controller;
import android.friendr.integration.DatabaseReturner;
import android.friendr.view.viewObject.Interests;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashSet;

public class FindNewInterest extends AppCompatActivity {

    SearchView searchView;
    Controller controller = new Controller();
    String currentUserID;
    HashSet<String> interestNamesForUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_new_interest);

        Intent intent = getIntent();
        if (null != intent) {
            currentUserID = (String) intent.getSerializableExtra("currentUserID");
            interestNamesForUser = (HashSet<String>) intent.getSerializableExtra("interestNamesForUser");
        }

        searchView = findViewById(R.id.search_interest);

        startClickListener(R.id.add_interests_button);

        Button button = findViewById(R.id.create_new_interest_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String searchText = searchView.getQuery().toString();
                controller.addInterest(searchText, currentUserID);
                Intent nextWindow = new Intent(FindNewInterest.this, Index.class);
                startActivity(nextWindow);
            }
        });

        //startClickListener(, CreateGroup.class);
    }

    private void startClickListener(final @IdRes int id) {
        Button button = findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String searchText = searchView.getQuery().toString();
                if(!searchText.isEmpty()) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    final String currentUserID = mAuth.getCurrentUser().getUid();
                    controller.getInterestSearchResult(new DatabaseReturner() {

                        @Override
                        public void returner(DataSnapshot dataSnapshot) {
                            //Show result using fragment_interests.xmll and it's dynamic view,
                            // same for group search
                            if (dataSnapshot != null) {
                                ProgressBar spinner = findViewById(R.id.progressBar3);
                                LinearLayout parentLinearLayout = findViewById(R.id.result_view);
                                parentLinearLayout.setVisibility(View.VISIBLE);
                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                LinearLayout result = parentLinearLayout.findViewById(R.id.result_interest);
                                if (result != null) {
                                    parentLinearLayout.removeView(result);
                                }

                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    View rowView = inflater.inflate(R.layout.fragment_new_interests, null, false);
                                    Interests interestProfile = postSnapshot.getValue(Interests.class);
                                    if(interestNamesForUser.contains(interestProfile.getName())) {
                                        continue;
                                    }
                                    spinner = findViewById(R.id.progressBar3);
                                    parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
                                    TextView interest_text = parentLinearLayout.findViewById(R.id.interest);
                                    final String interestName = interestProfile.getName();
                                    interest_text.setText(interestName);
                                    Button addInterest = parentLinearLayout.findViewById(R.id.add_interests_button);
                                    addInterest.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            controller.addUserInterest(interestName, currentUserID);
                                            Intent nextWindow = new Intent(FindNewInterest.this, Index.class);
                                            startActivity(nextWindow);
                                        }
                                    });
                                    addInterest.setId((int) (Math.random() * 1000 + 1));
                                    interest_text.setId((int) (Math.random() * 1000 + 1));
                                }
                                spinner.setVisibility(View.GONE);
                            } else {
                                LinearLayout parentLinearLayout = findViewById(R.id.result_view);
                                LinearLayout result = parentLinearLayout.findViewById(R.id.result_interest);
                                if (result != null) {
                                    parentLinearLayout.removeView(result);
                                }
                            }
                        }
                    }, currentUserID, searchText);
                } else {
                    LinearLayout parentLinearLayout = findViewById(R.id.result_view);
                    LinearLayout result = parentLinearLayout.findViewById(R.id.result_interest);
                    if (result != null) {
                        parentLinearLayout.removeView(result);
                    }

                }
            }
        });
    }


}
