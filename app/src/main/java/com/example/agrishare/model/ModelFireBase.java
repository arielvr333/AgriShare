package com.example.agrishare.model;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFireBase {

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private  static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ModelFireBase(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }
/*
    public interface GetAllPostsListener{
        void onComplete(List<Post> list);
    }
    //TODO: fix since...
    public void getAllPosts(Long lastUpdateDate, GetAllPostsListener listener) {
        db.collection(Post.COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("updateDate",new Timestamp(lastUpdateDate,0))
                .get()
                .addOnCompleteListener(task -> {
                    List<Post> list = new LinkedList<Post>();
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            Post post = Post.create(doc.getData());
                            if (post != null){
                                list.add(post);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }

 */





    public void addPost(Post post) {
        Map<String, Object> nPost = post.toDB();
        db.collection(post.COLLECTION_NAME)
                .document(post.getId())
                .set(nPost);
               // .addOnSuccessListener(unused -> listener.onComplete())
                //.addOnFailureListener(e -> listener.onComplete());
    }


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