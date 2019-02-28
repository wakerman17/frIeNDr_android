package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.friendr.integration.DatabaseReturner;
import android.friendr.integration.InterestAndGroupDAO;
import android.friendr.view.viewObject.InterestProfile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashSet;

public class CreateGroup extends AppCompatActivity {

    EditText groupNameET, groupDescriptionET;
    String currentUserID;
    InterestAndGroupDAO interestAndGroupDAO = new InterestAndGroupDAO();
    HashSet<String> interestNamesForUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Intent intent = getIntent();
        if (null != intent) {
            currentUserID = (String) intent.getSerializableExtra("currentUserID");
            interestNamesForUser = (HashSet<String>) intent.getSerializableExtra("interestNamesForUser");
        }
        groupNameET = findViewById(R.id.group_name);
        groupDescriptionET = findViewById(R.id.group_description);

        final Spinner spinner = findViewById(R.id.interest_spinner);
        interestAndGroupDAO.getUserInterest(new DatabaseReturner(){

            @Override
            public void returner(DataSnapshot dataSnapshot) {
                final ArrayList<String> interestList = new ArrayList<>();
                interestList.add("");
                interestList.addAll(interestNamesForUser);
                final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(CreateGroup.this, R.layout.spinner_item, interestList);

                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                spinner.setAdapter(spinnerArrayAdapter);
            }
        }, currentUserID);

        Button find_new_group = findViewById(R.id.find_new_group);
        find_new_group.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String groupName = groupNameET.getText().toString();
                String groupDescription = groupDescriptionET.getText().toString();
                final String interest = spinner.getSelectedItem().toString();
                interestAndGroupDAO.addGroup(groupName, groupDescription, interest, currentUserID);
                Intent nextWindow = new Intent(CreateGroup.this, Index.class);
                startActivity(nextWindow);
            }
        });
    }
}
