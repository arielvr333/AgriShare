package com.example.agrishare.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ModelFireBase {

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    public ModelFireBase() {
       // mAuth = FirebaseAuth.getInstance();
    //    mRootRef = FirebaseDatabase.getInstance().getReference();
    }


    /*              // check if user that opened the app still logged in (need to be in the first activity)
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();       //go to home page
        }
    }
     */


    public static boolean loginUser(String email, String password) {
        AtomicBoolean flag = new AtomicBoolean(false);
        Log.d("tag", String.valueOf(flag.get()));
        mAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("tag", "onComplete");
                    flag.set(true);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("tag", "onFailure");
                flag.set(false);
            }
        });
        Log.d("tag", String.valueOf(flag.get()));
        return flag.get();
    }
/*
    public void register(String fullName,String userName,String password) {
        Map<String, Object> user = new HashMap<>();
        user.put("full name", fullName);
        user.put("user name", userName);
        user.put("password", password);

        db.collection("users")
                .document(userName)
                .set(user)
                .addOnSuccessListener(unused -> Log.d("TAG", "DocumentSnapshot added with ID: " + unused))
                .addOnFailureListener(e -> Log.w("TAG", "Error adding document", e));

    }
*/


    public static boolean registerUser(final String username, final String name, String password) {
        final boolean[] flag = {false};
        Log.d("tag", "in registerUser");
        Log.d("tag", username);
        Log.d("tag", password);
        mAuth.createUserWithEmailAndPassword(username, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d("tag", "onSuccess");
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("username", username);
                map.put("id", Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

                mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("tag", "onComplete");
                        if (task.isSuccessful()) {
                            flag[0] = true;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("tag", "onFailure");
                        flag[0] = false;
                    }
                });

            }
        });
        Log.d("tag", "before return");
        return flag[0];
    }
}