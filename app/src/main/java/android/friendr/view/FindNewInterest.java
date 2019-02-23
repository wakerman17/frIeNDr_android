package android.friendr.view;

import android.content.Context;
import android.content.Intent;
import android.friendr.R;
import android.friendr.controller.Controller;
import android.friendr.integration.DatabaseReturner;
import android.friendr.view.viewObject.InterestProfile;
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

import com.google.firebase.database.DataSnapshot;

public class FindNewInterest extends AppCompatActivity {

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_new_interest);

        searchView = findViewById(R.id.search_interest);

        startClickListener(R.id.add_interests_button);

        Button button = findViewById(R.id.create_new_interest_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent nextWindow = new Intent(FindNewInterest.this, CreateGroup.class);
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
                    Controller controller = new Controller();
                    controller.getInterestSearchResult(new DatabaseReturner() {

                        @Override
                        public void returner(DataSnapshot dataSnapshot) {
                            //Show result using fragment_interest.xml and it's dynamic view,
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
                                    View rowView = inflater.inflate(R.layout.fragment_interests, null, false);
                                    Interests interestProfile = postSnapshot.getValue(Interests.class);

                                    spinner = findViewById(R.id.progressBar3);
                                    parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
                                    TextView interest_text = parentLinearLayout.findViewById(R.id.interest);
                                    interest_text.setText(interestProfile.getName());
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
                    }, 1, searchText);
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
