package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.friendr.controller.Controller;
import android.friendr.integration.DatabaseReturner;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;

public class FindNewInterest extends AppCompatActivity {

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_new_interest);

        searchView = findViewById(R.id.search_interest);

        startClickListener(R.id.add_interests_button, CreateGroup.class);
        startClickListener(R.id.create_new_interest_button, CreateGroup.class);
    }

    private void startClickListener(final @IdRes int id, final java.lang.Class newWindowClass) {
        Button button = findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent nextWindow = new Intent(FindNewInterest.this, newWindowClass);
                if(id == R.id.add_interests_button) {
                    String searchText = searchView.getQuery().toString();
                    Controller controller = new Controller();
                    controller.getInterestSearchResult(new DatabaseReturner() {

                        @Override
                        public void returner(DataSnapshot dataSnapshot) {
                            //Show result using fragment_interest.xml and it's dynamic view,
                            // same for group search
                        }
                    }, 1, searchText);
                }

                startActivity(nextWindow);
            }
        });
    }
}
