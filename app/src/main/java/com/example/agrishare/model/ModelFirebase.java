package com.example.agrishare.model;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

class ModelFireBase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    ModelFireBase() {
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


    public void logIn(String userName,String password) {

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