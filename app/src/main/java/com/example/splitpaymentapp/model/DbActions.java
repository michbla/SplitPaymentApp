package com.example.splitpaymentapp.model;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class DbActions {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static FirebaseStorage storage = FirebaseStorage.getInstance();
    public static StorageReference storageReference = storage.getReference();
    private static DbActions dbActions;
    private static CollectionReference _users = db.collection("Users");
    private static CollectionReference _groups = db.collection("Groups");
    private static CollectionReference _payments = db.collection("Payments");
    private static CollectionReference _receipts = db.collection("Receipts");
    public static User bufferUser;
    public static FirebaseAuth auth = FirebaseAuth.getInstance();

    private DbActions() {
    }

    public DbActions getInstance() {
        if (dbActions == null)
            dbActions = new DbActions();
        return dbActions;
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
                    DbActions.addUserToDb(user);
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

    public static void addUserToGroup(@NonNull String email, @NonNull String groupId, IDbActions.IAddUserToGroup IAddUserToGroup){
        _users.whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    QuerySnapshot snapshot = task.getResult();
                    List<User> x = snapshot.toObjects(User.class);
                    if (x.size()==0){
                        IAddUserToGroup.onFail();
                    }
                    else {
                        _groups.whereArrayContains("users", x.get(0).getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                boolean inThisGroup = false;
                                if (task.isSuccessful()) {
                                    QuerySnapshot snapshots = task.getResult();
                                    List<Group> groups = new ArrayList<>();
                                    groups.addAll(snapshots.toObjects(Group.class));
                                    for (Group z : groups) {
                                        if (z.getUid().equals(groupId)) {
                                            inThisGroup = true;
                                            IAddUserToGroup.onAlreadyInGroup();
                                        }
                                    }
                                    if (!inThisGroup) {
                                        _groups.document(groupId).update("users", FieldValue.arrayUnion((x.get(0).getUid()))).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                IAddUserToGroup.onSuccess();
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
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

    public static void createGroup(@NonNull User user, String _groupName, IDbActions.ICreateGroup ICreateGroup){
        Map<String, Object> groupData = new HashMap<>();
        String  id =  UUID.randomUUID().toString();
        groupData.put("Uid", id);
        groupData.put("groupName", _groupName);
        groupData.put("groupOwner", auth.getUid());
        groupData.put("isFinished", 0);
        groupData.put("users", Arrays.asList(auth.getUid()));
        //groupData.put("users", FieldValue.arrayUnion(auth.getUid()));
        Log.e("debugData", _groupName + " " + auth.getUid() + " " + Arrays.asList(auth.getUid()));
        _groups.document(id).set(groupData).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                ICreateGroup.onCompleted(new Group(id, _groupName, auth.getUid(), 0, user));
            }

        });
    }

    public static void addPayment(@NonNull Payment payment){
        _payments.document(payment.getPaymentId()).set(payment);
    }

    public static void browsePaymentsWithinRange(@NonNull List<String> list, IDbActions.IBrowsePaymentsWithinRange IBrowsePaymentsWithinRange){
        _payments.whereIn("receiptId", list).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Payment> payments = new ArrayList<Payment>();
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        Payment p = documentSnapshot.toObject(Payment.class);
                        payments.add(p);
                    }
                    IBrowsePaymentsWithinRange.onCompleted(payments);
                }
            }
        });
    }

    public static void addReceipt(@NonNull Receipt receipt) {_receipts.document(receipt.getId()).set(receipt); }

    public static void browseReceipts(String groupId, IDbActions.IBrowseReceipts IBrowseReceipts){
        _receipts.whereEqualTo("groupId", groupId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Receipt> receiptList = new ArrayList<Receipt>();
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        Receipt r = documentSnapshot.toObject(Receipt.class);
                        receiptList.add(r);
                    }
                    IBrowseReceipts.onCompleted(receiptList);
                }
            }
        });
    }

    public static void getPaymentsFromDb(@NonNull String receiptId, IDbActions.IBrowsePayments IBrowsePayments){
        _payments.whereEqualTo("receiptId", receiptId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Payment> paymentList = new ArrayList<Payment>();
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        Payment p = documentSnapshot.toObject(Payment.class);
                        paymentList.add(p);
                    }
                    IBrowsePayments.onCompleted(paymentList);
                }
            }
        });
    }

    public static void getUserGroups(String Uid, IDbActions.IBrowseGroup IBrowseGroup){
        _groups.whereArrayContains("users", Uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Group> groupList = new ArrayList<Group>();
                if (task.isSuccessful()){
                    Log.e("browseDB", "success");
                    for (QueryDocumentSnapshot document: task.getResult()){
                        Group g = document.toObject(Group.class);
                        g.setUid(document.getId());
                        groupList.add(g);
                    }
                    IBrowseGroup.onCompleted(groupList);
                }
                else{
                    Log.e("browseDB", "fail");
                }
            }
        });
    }

    public static void getUsersFromGroup(String groupUid, IDbActions.IBrowseUsers IBrowseUsers){
        List<User> userList = new ArrayList<>();
        CountDownLatch done = new CountDownLatch(1);
        _groups.document(groupUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                List<String> users = (List<String>) document.get("users");
                _users.whereIn(FieldPath.documentId(), users).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot document = task.getResult();
                        userList.addAll(document.toObjects(User.class));
                        IBrowseUsers.onCompleted(userList);
                    }
                });
            }
        });

    }

    public static void setGroupFinished(String gId){
        _groups.document(gId).update("isFinished", 1);
    }

}


