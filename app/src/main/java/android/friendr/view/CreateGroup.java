package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        Button find_new_group = findViewById(R.id.find_new_group);
        find_new_group.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent nextWindow = new Intent(CreateGroup.this, Index.class);
                startActivity(nextWindow);
            }
        });
    }
}
