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

import com.example.splitpaymentapp.R;
import com.example.splitpaymentapp.model.DbActions;
import com.example.splitpaymentapp.model.IDbActions;
import com.example.splitpaymentapp.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText fullName, email, passwd, passwd2;
    Button registerButton;
    TextView alternateLogin;
    public FirebaseAuth auth;
    ProgressBar pbar;
    DbActions dbActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeElements();

        alternateLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }

    private void initializeElements(){
        fullName = findViewById(R.id.fullNameFieldRegister);
        email = findViewById(R.id.emailFieldRegister);
        passwd = findViewById(R.id.passwdFieldRegister);
        passwd2 = findViewById(R.id.passwd2FieldRegister);
        registerButton = findViewById(R.id.registerButton);
        alternateLogin = findViewById(R.id.alternateLoginButton);
        pbar = findViewById(R.id.registerProgressBar);
        auth = FirebaseAuth.getInstance();
        //dbActions.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //String email, password;
            //Pair<String, String> pair = parseFormData();
            //email = pair.first;
            //password = pair.second;

                String _name = fullName.getText().toString().trim();
                String _email = email.getText().toString().trim();
                String _passwd = passwd.getText().toString().trim();
                //System.out.printf(_name + " " + _email + " " + passwd);
                String _passwd2 = passwd2.getText().toString().trim();



                if (TextUtils.isEmpty(_name)) {
                    fullName.setError("full name field cannot be empty!");
                    return;
                }

                if (TextUtils.isEmpty(_email)){
                    email.setError("email field cannot be empty!");
                    return;
                }

                if (TextUtils.isEmpty(_passwd)){
                    passwd.setError("password field cannot be empty!");
                    return;
                }

                if (passwd.length()<8){
                    passwd.setError("password must be 8 or more characters");
                    return;
                }

                if (TextUtils.isEmpty(_passwd2)){
                    passwd2.setError("password field cannot be empty!");
                    return;
                }

                if (!TextUtils.equals(_passwd, _passwd2)){
                    passwd2.setError("password mismatch");
                    return;
                }


            pbar.setVisibility(View.VISIBLE);
            try {
                //sendDataToFirebase(_email, _passwd);
                DbActions.createUser(_email, _passwd, _name, new IDbActions.IRegisterUser() {
                    @Override
                    public void onCompleted(User user) {
                        if (auth.getCurrentUser() !=null){
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }
                });
            }
            catch (NullPointerException e){
                System.err.println(e);
            }



            }
        });
    }

//    private Pair<String, String> parseFormData(){
//        String _name = fullName.getText().toString().trim();
//        String _email = email.getText().toString().trim();
//        String _passwd = passwd.getText().toString().trim();
//        System.out.printf(_name + " " + _email + " " + passwd);
//        String _passwd2 = passwd2.getText().toString().trim();
//
//
//
//        if (TextUtils.isEmpty(_name)) {
//            fullName.setError("full name field cannot be empty!");
//            return new Pair(null, null);
//        }
//
//        if (TextUtils.isEmpty(_email)){
//            email.setError("email field cannot be empty!");
//            return new Pair(null, null);
//        }
//
//        if (TextUtils.isEmpty(_passwd)){
//            passwd.setError("password field cannot be empty!");
//            return new Pair(null, null);
//        }
//
//        if (passwd.length()<8){
//            passwd.setError("password must be 8 or more characters");
//            return new Pair(null, null);
//        }
//
//        if (TextUtils.isEmpty(_passwd2)){
//            passwd2.setError("password field cannot be empty!");
//            return new Pair(null, null);
//        }
//
//        if (!TextUtils.equals(_passwd, _passwd2)){
//            passwd2.setError("password mismatch");
//            return new Pair(null, null);
//        }
//        return new Pair(_email, _passwd);
//    }

//    private void sendDataToFirebase(String email, String passwd){
//        auth.createUserWithEmailAndPassword(email, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()){
//
//                    User user = new User(auth.getUid().toString(), fullName.getText().toString(), email);
//                    DbActions.addUserToDb(user);
//                    Toast.makeText(RegisterActivity.this, "you have been registered", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(RegisterActivity.this, " xDError!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//
//    }

}