package com.example.agrishare.model;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Objects;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ModelFireBase {

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private  static FirebaseFirestore db = FirebaseFirestore.getInstance();
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