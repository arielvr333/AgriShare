package com.example.agrishare.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.agrishare.MYApplication;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {

    User user=null;
    ModelFireBase modelFirebase = new ModelFireBase();
    MutableLiveData<List<Post>> postsList = new MutableLiveData<>();
    public interface AddPostListener {
        void onComplete();
    }

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
            if(p.getId().equals(id))
                tempPost=p;
        tempPost.setTitle(title);
        tempPost.setPost(post);
        tempPost.setAddress(address);
        tempPost.setPrice(price);
        modelFirebase.addPost(tempPost);
        for (Post p:this.user.posts)
            if(p.getId().equals(id))
            {
                this.user.posts.remove(p);
                this.user.posts.add(tempPost);
                break;
            }
        modelFirebase.updateUserPostList(this.user);
    }

    public void setLoggedUser(User user){ this.user=user; }
    public static final Model instance = new Model();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    public interface GetAllPostsListener { void onComplete(List<Post> list);}
    public LiveData<List<Post>> getAll() { return postsList; }
    public Post getPostById(Long id)
    {
        for (Post p:postsList.getValue())
            if(p.getId().equals(id))
                return p;

            return null;

    }
    public void getAllPosts(GetAllPostsListener listener) {
        executor.execute(() -> {
            List<Post> list = AppLocalDB.db.PostDao().getAll();
            mainThread.post(() -> listener.onComplete(list));
        });
        ModelFireBase.getAllPosts(list -> executor.execute(() -> {
            Long lud = new Long(0);
            for (Post post : list)
                AppLocalDB.db.PostDao().insertAll(post);
            MYApplication.getContext()
                    .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                    .edit()
                    .putLong("PostsLastUpdateDate", lud)
                    .apply();
            List<Post> postList = AppLocalDB.db.PostDao().getAll();
            postsList.setValue(postList);
        }));

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

}
