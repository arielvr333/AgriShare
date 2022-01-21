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
//        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                .setPersistenceEnabled(false)
//                .build();
//        db.setFirestoreSettings(settings);
    }

    public interface GetAllPostsListener{
        void onComplete(List<Post> list);
    }


    public interface GetAllUsersListener{
        void onComplete(List<User> list);
    }

    public static void getAllPosts(GetAllPostsListener listener) {
        db.collection(Post.COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    List<Post> list = new LinkedList<>();
                    if (task.isSuccessful()){
                        Log.d("tag","successful");
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            Post post = Post.create(doc.getData());
                            list.add(post);
                        }
                    }
                    Log.d("tag","finished getAlPosts");
                    listener.onComplete(list);
                });
    }


    public static void getAllUsers(GetAllUsersListener listener) {
        db.collection("Users")
                .get()
                .addOnCompleteListener(task -> {
                    List<User> list = new LinkedList<>();
                    if (task.isSuccessful()){
                        Log.d("tag","successful");
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            String Name = (String) doc.getData().get("name");
                            String Email = (String) doc.getData().get("email");
                            String Id = (String) doc.getData().get("id");
                            String Address = (String) doc.getData().get("address");
                            String Phonenumber = (String) doc.getData().get("phoneNumber");
                            User user = new User(Name,Email,Id,Address,Phonenumber);
                            list.add(user);
                        }
                    }
                    Log.d("tag","finished getAlPosts");
                    listener.onComplete(list);
                });
    }


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
            getAllUsers(new GetAllUsersListener() {
                @Override
                public void onComplete(List<User> list) {
                    Integer size=list.size()+1;
                    String id=Integer.toString(size);
                    User newuser = new User(name, Email, id, address, phoneNumber);
                    db.collection("Users").document(name).set(newuser);
                }
            });
         });
    }
}