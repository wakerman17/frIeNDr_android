package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.friendr.controller.Controller;
import android.friendr.integration.DatabaseReturner;
import android.friendr.view.viewObject.Group;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CreateGroup extends AppCompatActivity {

    EditText groupNameET, groupDescriptionET;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Controller controller = new Controller();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Intent intent = getIntent();
        if (null != intent) {
            currentUserID = (String) intent.getSerializableExtra("currentUserID");
        }

        final Spinner interestSpinner =  findViewById(R.id.interest);
        groupNameET = findViewById(R.id.group_name);
        groupDescriptionET = findViewById(R.id.group_description);

        final Spinner spinner = findViewById(R.id.interest);
        controller.getAllInterests(new DatabaseReturner(){

            @Override
            public void returner(DataSnapshot dataSnapshot) {
                int i = 0;
                final ArrayList<String> plantsList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final Group group = postSnapshot.getValue(Group.class);
                    plantsList.add(group.getName());
                }
                //final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>();
                final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(CreateGroup.this, R.layout.spinner_item, plantsList);

                spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                spinner.setAdapter(spinnerArrayAdapter);
            }
        });

        Button find_new_group = findViewById(R.id.find_new_group);
        find_new_group.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String groupName = groupNameET.getText().toString();
                String groupDescription = groupDescriptionET.getText().toString();
                final String interest = interestSpinner.getSelectedItem().toString();
                controller.addGroup(groupName, groupDescription, interest, currentUserID);
                Intent nextWindow = new Intent(CreateGroup.this, Index.class);
                startActivity(nextWindow);
            }
        });
    }
}
