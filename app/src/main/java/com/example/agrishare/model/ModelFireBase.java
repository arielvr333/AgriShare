package com.example.agrishare.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFireBase {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ModelFireBase(){}

    public interface GetAllPostsListener{
        void onComplete(List<Post> list);
    }

    public interface GetAllUsersListener{
        void onComplete(List<User> list);
    }

    public void getAllPosts(GetAllPostsListener listener) {
        db.collection(Post.COLLECTION_NAME).orderBy("Id",Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    List<Post> list = new LinkedList<>();
                    if (task.isSuccessful())
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            Post post = Post.create(doc.getData());
                            list.add(post);
                        }
                    listener.onComplete(list);
                });
    }

    public void getLoggedUser(String username) {
        db.collection("Users").document(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String Name = (String) task.getResult().getData().get("name");
                String Email = (String) task.getResult().getData().get("email");
                String Id = (String) task.getResult().getData().get("id");
                String Address = (String) task.getResult().getData().get("address");
                String Phonenumber = (String) task.getResult().getData().get("phoneNumber");
                User user = new User(Name, Email, Id, Address, Phonenumber);
                user.setPosts((ArrayList<Post>) task.getResult().getData().get("posts"));
                System.out.println(user.posts);
                Model.instance.setLoggedUser(user);
            }
        });
    }

    public void getAllUsers(GetAllUsersListener listener) {
        db.collection("Users")
                .get()
                .addOnCompleteListener(task -> {
                    List<User> list = new LinkedList<>();
                    if (task.isSuccessful()){
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
                    listener.onComplete(list);
                });
    }


    public void addPost(Post post) {
        Map<String, Object> nPost = post.toDB();
        db.collection(post.COLLECTION_NAME)
                .document(Long.toString(post.getId()))
                .set(nPost);
               // .addOnSuccessListener(unused -> listener.onComplete())
                //.addOnFailureListener(e -> listener.onComplete());
    }

    public void updateUserPostList(User user) {
        db.collection("Users").document(user.getEmail()).update("posts",user.getPosts());
    }

    public void deletePost(User user, String postId){
        db.collection("post.COLLECTION_NAME").document(postId).delete();
        updateUserPostList(user);
    }

    public interface loginListener{
        void onComplete(boolean bool);
    }

    public interface RegisterListener{
        void onComplete(boolean bool);
    }

    public void loginUser(String email, String password, loginListener listener) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                getLoggedUser(email);
                listener.onComplete(true);
            }
        }).addOnFailureListener(e -> listener.onComplete(false));
    }

    public void registerUser(final String Email, final String name, String password,String address,String phoneNumber, RegisterListener listener) {
        mAuth.createUserWithEmailAndPassword(Email, password).addOnSuccessListener(authResult -> {
            listener.onComplete(true);
            getAllUsers(list -> {
                int size=list.size()+1;
                String id=Integer.toString(size);
                User newUser = new User(name, Email, id, address, phoneNumber);
                Model.instance.setLoggedUser(newUser);
                db.collection("Users").document(Email).set(newUser);
            });
         });
    }

    public boolean isSignedIn(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return (currentUser != null);
    }
}