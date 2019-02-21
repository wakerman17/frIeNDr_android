package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;

public class WelcomeActivity extends AppCompatActivity {

    private Button btn1;
    private Button btn2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        FirebaseApp.initializeApp(this);

        btn1 = findViewById(R.id.signUpID);
        btn2 = findViewById(R.id.WCloginBtnID);

        btn1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent regIntent = new Intent(WelcomeActivity.this, RegisterActivity.class);
                startActivity(regIntent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent loginIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }
}
