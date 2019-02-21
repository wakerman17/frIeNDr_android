package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn2 = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.loginButton);

        btn2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent regIntent = new Intent(LoginActivity.this, Index.class);
                startActivity(regIntent);
            }
        });
    }
}
