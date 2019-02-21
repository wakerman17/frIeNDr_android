package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {

    private Button buttonToIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        buttonToIndex = (Button)findViewById(R.id.reg_RegisterButtonID);

        buttonToIndex.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent regIntent = new Intent(RegisterActivity.this, Index.class);
                startActivity(regIntent);
            }
        });
    }
}
