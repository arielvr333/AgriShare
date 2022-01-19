package com.example.agrishare.model;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ModelFireBase {

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private  static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ModelFireBase() {}

    public interface loginListener{
        void onComplete(boolean bool);
    }

    public interface RegisterListener{
        void onComplete(boolean bool);
    }

    public static void loginUser(String email, String password, loginListener listener) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.onComplete(true);
            }
        }).addOnFailureListener(e -> listener.onComplete(false));
    }

    public static void registerUser(final String Email, final String name, String password,String address,String phoneNumber, RegisterListener listener) {
        mAuth.createUserWithEmailAndPassword(Email, password).addOnSuccessListener(authResult -> {
            listener.onComplete(true);
            User newuser = new User(name, Email, 2, address, phoneNumber);
            db.collection("Users").document(name).set(newuser);
         });
    }
}