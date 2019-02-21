package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FindNewInterest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_new_interest);

        startClickListener(R.id.add_interests_button, CreateGroup.class);
        startClickListener(R.id.create_new_interest_button, CreateGroup.class);
    }

    private void startClickListener(@IdRes int id, final java.lang.Class newWindowClass) {
        Button button = findViewById(id);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent nextWindow = new Intent(FindNewInterest.this, newWindowClass);
                startActivity(nextWindow);
            }
        });
    }
}
