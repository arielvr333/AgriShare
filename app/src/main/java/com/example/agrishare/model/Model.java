package com.example.agrishare.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.agrishare.MYApplication;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    User user=null;
    MutableLiveData<List<Post>> postsList = new MutableLiveData<>();
    public static final Model instance = new Model();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    ModelFireBase modelFirebase = new ModelFireBase();

    public interface AddPostListener { void onComplete();}

    private Model() {
        //  postListLoadingState.setValue(PostListLoadingState.loaded);
    }

    public void addPost(String title,String post,String address,String price) {
        Post newPost = new Post(title, post, address , price, System.currentTimeMillis());
        modelFirebase.addPost(newPost);
        this.user.posts.add(newPost);
        modelFirebase.updateUserPostList(this.user);
    }

    public void editPost(String title,String post,String address,String price,Long id) {
        Post tempPost=null;
        for (Post p:postsList.getValue())
            if(p.getId().equals(id)) {
                tempPost = p;
                this.user.posts.remove(p);
            }
        tempPost.setTitle(title);
        tempPost.setPost(post);
        tempPost.setAddress(address);
        tempPost.setPrice(price);
        modelFirebase.addPost(tempPost);
        this.user.posts.add(tempPost);
        modelFirebase.updateUserPostList(this.user);
    }

    public void setLoggedUser(User user){ this.user=user; }

    public User getUser() { return user; }

    public interface GetAllPostsListener { void onComplete(List<Post> list);}

    public LiveData<List<Post>> getAll() { return postsList; }

    public Post getPostById(Long id){
        for (Post p:postsList.getValue())
            if(p.getId().equals(id))
                return p;
            return null;
    }
    public void getAllPosts(GetAllPostsListener listener) {
//        executor.execute(() -> {
//            List<Post> list = AppLocalDB.db.PostDao().getAll();
//            mainThread.post(() -> listener.onComplete(list));
//        });
        modelFirebase.getAllPosts(list -> {
            postsList.setValue(list);
            listener.onComplete(list);
        });
            //     Long lud = new Long(0);
//            Log.d("TAG", "firebase returned " + list.size());
//            for (Post post : list) {
//                Log.d("tag", post.getTitle());
//                Log.d("tag", "inserting post");
//                AppLocalDB.db.PostDao().insertAll(post);
//            }
//            Log.d("tag", "inserted all posts");
//            MYApplication.getContext()
//                    .getSharedPreferences("TAG", Context.MODE_PRIVATE)
//                    .edit()
//                   // .putLong("PostsLastUpdateDate", lud)
//                    .commit();
//            List<Post> postList = AppLocalDB.db.PostDao().getAll();
//        });

//    public enum PostListLoadingState {
//        loading,
//        loaded
//    }
//
//    MutableLiveData<PostListLoadingState> postListLoadingState = new MutableLiveData<PostListLoadingState>();
//
//    public LiveData<PostListLoadingState> getPostListLoadingState() {
//        return postListLoadingState;
//    }
    }

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
