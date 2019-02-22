package android.friendr.view;

import android.content.Intent;
import android.friendr.R;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button buttonToIndex;
    EditText email, username, password, confPassword;
    RadioGroup gender;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        getSupportActionBar().setTitle("Register");

        //buttonToIndex = (Button)findViewById(R.id.reg_RegisterButtonID);
        btn_register = findViewById(R.id.reg_RegisterButtonID);
        email = findViewById(R.id.regEmail);
        username = findViewById(R.id.regUsernameID);
        password = findViewById(R.id.regPassword);
        //gender = findViewById(R.id.regRadioGroup);
        confPassword = findViewById(R.id.regConfPW);

        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_pw = password.getText().toString();

                // todo handle all fields
                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_pw) || TextUtils.isEmpty(txt_email)) {
                    Toast.makeText(RegisterActivity.this,"All fields must be filled out",Toast.LENGTH_SHORT).show();
                } else {
                    register(txt_email, txt_username, txt_pw);
                }
            }
        });

    }


    private void register(String email, final String username, String pw) {
        // todo nofity user if passwords don't match

        auth.createUserWithEmailAndPassword(email, pw).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(RegisterActivity.this, Index.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration was not successful.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
