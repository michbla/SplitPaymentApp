package com.example.splitpaymentapp.model;


import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.splitpaymentapp.view.Login;
import com.example.splitpaymentapp.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static Controller controller;
    private static CollectionReference _users = db.collection("Users");
    private static CollectionReference _groups = db.collection("Groups");
    public static User bufferUser;
    public static FirebaseAuth auth = FirebaseAuth.getInstance();

    private Controller() {
    }

    public Controller getInstance() {
        if (controller == null)
            controller = new Controller();
        return controller;
    }

    public static void loginUser (String _email, String _passwd, IDbActions.ILoginUser ILoginUser){
        auth.signInWithEmailAndPassword(_email, _passwd).addOnCompleteListener(new  OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    getUserFromDb(auth.getUid(),new IDbActions.IAddUser(){

                        @Override
                        public void onCompleted(User user) {
                            User user1 = user;
                            ILoginUser.onCompleted(user1);
                        }
                    });
                }
                else{
                    Log.e("loginUser", "failed to login");

                }

            }
        });
    }

    public static void createUser(String email, String passwd, String fullName, IDbActions.IRegisterUser IRegisterUser){
        auth.createUserWithEmailAndPassword(email, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    User user = new User(auth.getUid().toString(), fullName, email);
                    Controller.addUserToDb(user);
                    Log.w("createUser", "created user");
                    IRegisterUser.onCompleted(user);
                }
                else{
                    Log.w("createUser", "failed to create user");
                }

            }
        });
    }

    public static void addUserToDb(@NonNull User user) {
        Map<String, String> userMap = new HashMap<String, String>();
        userMap.put("email", user.getEmail());
        userMap.put("fullName", user.getFullName());

        _users.document(user.getUid()).set(userMap);
    }


    public static void getUserFromDb(@NonNull String Uid, IDbActions.IAddUser interFace) {
        _users.document(Uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    User user = ds.toObject(User.class);

                    interFace.onCompleted(user);
                }
            }
        });
    }

    public static void createGroup(@NonNull User user, String _groupName, IDbActions.ICreateGroup IcreateGroup){
        Map<String, Object> groupData = new HashMap<>();
        groupData.put("groupName", _groupName);
        groupData.put("groupOwner", auth.getUid());
        groupData.put("users", FieldValue.arrayUnion(auth.getUid()));
        Log.e("debugData", _groupName + " " + auth.getUid() + " " + Arrays.asList(auth.getUid()));
        _groups.add(groupData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.w("groupRegister", "grupa utworzona z id: " + _groups.document().getId());
                IcreateGroup.onCompleted(new Group(_groupName, user));

            }
        });
    }


}


