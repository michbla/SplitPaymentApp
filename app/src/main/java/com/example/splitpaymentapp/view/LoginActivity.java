package com.example.splitpaymentapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.DbActions;
import com.example.splitpaymentapp.model.IDbActions;
import com.example.splitpaymentapp.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginButton;
    TextView alternateRegisterButton;
    ProgressBar pbar;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeElements();

        parseLoginData();

        pbar.setVisibility(View.GONE);


        alternateRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    private void initializeElements(){
        email = findViewById(R.id.emailFieldLogin);
        password = findViewById(R.id.passwordFieldLogin);
        alternateRegisterButton = findViewById(R.id.alternateSignUpButton);
        pbar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.loginButton);




    }

    private void parseLoginData()
    {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _email = "", _passwd = "";
                _email = email.getText().toString().trim();
                _passwd = password.getText().toString().trim();

                if (TextUtils.isEmpty(_email)){
                    email.setError("enter email");
                    return;
                }

                if (TextUtils.isEmpty((_passwd))){
                    password.setError("enter password");
                    return;
                }

                if (_passwd.length() < 8){
                    password.setError("password must be 8 or more letters long");
                    return;
                }

//                auth.signInWithEmailAndPassword(_email, _passwd).addOnCompleteListener(LoginActivity.this,new  OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                        else
//                            Toast.makeText(LoginActivity.this, "Error Logging in", Toast.LENGTH_SHORT).show();
//                    }
//                });
                pbar.setVisibility(View.VISIBLE);
                DbActions.loginUser(_email, _passwd, new IDbActions.ILoginUser() {
                    @Override
                    public void onCompleted(User user) {
                        Toast.makeText(LoginActivity.this, "zalogowano!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("id", auth.getUid());
                        startActivity(intent);
                        finish();
                    }
                });

        }
        });
    }


}