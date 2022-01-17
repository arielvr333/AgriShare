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
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    public ModelFireBase() {}


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

    public static void registerUser(final String username, final String name, String password, RegisterListener listener) {
        mAuth.createUserWithEmailAndPassword(username, password).addOnSuccessListener(authResult -> {
            listener.onComplete(true);
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("username", username);
            map.put("id", Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
            mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Log.d("tag", "completed");
                else
                    Log.d("tag", "task failed");
            }).addOnFailureListener(e -> listener.onComplete(false));
        });
    }
}