package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class FindNewGroup extends AppCompatActivity {

    LinearLayout filterView, groupInfo1, groupInfo2;
    TextView min_members_txt, max_members_txt;
    EditText min_members, max_members;
    Button createNewGroup;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_new_group);
        filterView = findViewById(R.id.filter_view);
        groupInfo1 = findViewById(R.id.group_info_1);
        groupInfo2 = findViewById(R.id.group_info_2);
        createNewGroup = findViewById(R.id.create_new_group);
        createNewGroup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent nextWindow = new Intent(FindNewGroup.this, CreateGroup.class);
                startActivity(nextWindow);
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

    public void toggle_group_info_2(View v){
        toggleContent(groupInfo2);
    }

    private void toggleContent (LinearLayout linearLayout) {
        linearLayout.setVisibility( linearLayout.isShown()
                ? View.GONE
                : View.VISIBLE );
    }
}
