package com.example.agrishare.model;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

class ModelFireBase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void logIn(String userName,String password) {
        // TODO: pull data from DB and compare the username and password.
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