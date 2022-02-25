package com.example.agrishare.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.agrishare.MainActivity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    User user = null;

    MutableLiveData<List<Post>> postsList = new MutableLiveData<>();
    public static final Model instance = new Model();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    ModelFireBase modelFirebase = new ModelFireBase();

    public enum PostListLoadingState {
        loading,
        loaded
    }
    MutableLiveData<PostListLoadingState> postListLoadingState = new MutableLiveData<>();

    public LiveData<PostListLoadingState> getPostListLoadingState() {
        return postListLoadingState;
    }

    public boolean userIsWriter(Post post) {
        return this.user.Id.equals(post.getWriterId());
    }

    public interface AddPostListener {
        void onComplete();
    }

    private Model() {
        //  postListLoadingState.setValue(PostListLoadingState.loaded);
    }

    public void addPost(String title, String post, String address, String price) {
        Post newPost = new Post(title, post, address, price, System.currentTimeMillis(), user.getId());
        modelFirebase.addPost(newPost);
    }

    public void editPost(String title, String post, String address, String price, Long id) {
        Post tempPost = null;
        for (Post p : postsList.getValue())
            if (p.getId().equals(id)) {
                tempPost = p;
            }
        tempPost.setTitle(title);
        tempPost.setPost(post);
        tempPost.setAddress(address);
        tempPost.setPrice(price);
        modelFirebase.addPost(tempPost);
    }

    public void setLoggedUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public interface GetAllPostsListener {
        void onComplete(List<Post> list);
    }

    public LiveData<List<Post>> getAll() {
        return postsList;
    }

    public Post getPostById(Long id) {
        for (Post p : postsList.getValue())
            if (p.getId().equals(id))
                return p;
        return null;
    }

    public void getAllPosts(GetAllPostsListener listener) {
        executor.execute(() -> {
            List<Post> list = AppLocalDB.db.PostDao().getAll();
            mainThread.post(() -> listener.onComplete(list));
//        });
            modelFirebase.getAllPosts(pList -> {
                postsList.setValue(pList);
                listener.onComplete(pList);
            });
            Long lud = new Long(0);
            Log.d("TAG", "firebase returned " + list.size());
            for (Post post : list) {
                Log.d("tag", post.getTitle());
                Log.d("tag", "inserting post");
                AppLocalDB.db.PostDao().insertAll(post);
            }
            Log.d("tag", "inserted all posts");
            MainActivity.getContext()
                    .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                    .edit()
                    .putLong("PostsLastUpdateDate", lud)
                    .commit();
            List<Post> postList = AppLocalDB.db.PostDao().getAll();
        });

    }

//    public void refreshStudentList() {
//        postListLoadingState.setValue(postListLoadingState.loading);
//
//        // get last local update date
//        Long lastUpdateDate = MainActivity.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("PostsLastUpdateDate", 0);
//
//        executor.execute(() -> {
//            List<Post> stList = AppLocalDB.db.PostDao().getAll();
//            postsList.postValue(stList);
//        });
//
//        // firebase get all updates since lastLocalUpdateDate
//        modelFirebase.getAllPosts(list -> {
//            // add all records to the local db
//            executor.execute(() -> {
//                Long lud = new Long(0);
//                Log.d("TAG", "fb returned " + list.size());
//                for (Post post : list) {
//                    AppLocalDB.db.PostDao().insertAll(post);
//                }
//                // update last local update date
//                MainActivity.getContext()
//                        .getSharedPreferences("TAG", Context.MODE_PRIVATE)
//                        .edit()
//                        .putLong("StudentsLastUpdateDate", lud)
//                        .commit();
//
//                //return all data to caller
//                List<Post> stList = AppLocalDB.db.PostDao().getAll();
//                postsList.postValue(stList);
//                postListLoadingState.postValue(postListLoadingState.loaded);
//            });
//        });
//    }

    public void loginUser(String email, String password, ModelFireBase.loginListener listener){
        modelFirebase.loginUser(email,password,listener);
    }

    public void registerUser(String Email, String name, String password,String address,String phoneNumber, ModelFireBase.RegisterListener listener){
        modelFirebase.registerUser(Email, name, password, address, phoneNumber, listener);
    }
    public boolean isSignedIn() {
        return modelFirebase.isSignedIn();
    }
}
