package com.example.agrishare.model;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFireBase {

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            Post post = Post.create(doc.getData());
                            list.add(post);
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public static void getLoggedUser(String username) {
        DocumentReference docRef = db.collection("Users").document(username);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.isSuccessful()) {
                    String Name = (String) task.getResult().getData().get("name");
                    String Email = (String) task.getResult().getData().get("email");
                    String Id = (String) task.getResult().getData().get("id");
                    String Address = (String) task.getResult().getData().get("address");
                    String Phonenumber = (String) task.getResult().getData().get("phoneNumber");
                    User user = new User(Name, Email, Id, Address, Phonenumber);
                    Model.instance.setLoggedUser(user);
                }
            }
        });
    }

    public static void getAllUsers(GetAllUsersListener listener) {
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
                getLoggedUser(email);
                listener.onComplete(true);
            }
        }).addOnFailureListener(e -> listener.onComplete(false));
    }

    public static void registerUser(final String Email, final String name, String password,String address,String phoneNumber, RegisterListener listener) {
        mAuth.createUserWithEmailAndPassword(Email, password).addOnSuccessListener(authResult -> {
            listener.onComplete(true);
            getAllUsers(list -> {
                Integer size=list.size()+1;
                String id=Integer.toString(size);
                User newUser = new User(name, Email, id, address, phoneNumber);
                Model.instance.setLoggedUser(newUser);
                db.collection("Users").document(Email).set(newUser);
            });
         });
    }
}