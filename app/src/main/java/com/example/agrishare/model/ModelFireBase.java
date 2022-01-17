package com.example.agrishare.model;

import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ModelFireBase {

    private static FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ModelFireBase() {
        mAuth = FirebaseAuth.getInstance();
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
        final boolean[] flag = {false};
        mAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                   flag[0] = true;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                flag[0] = false;
            }
        });
        return flag[0];
    }

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
}