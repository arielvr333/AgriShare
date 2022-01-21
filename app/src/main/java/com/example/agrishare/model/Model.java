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
    ModelFireBase modelFirebase = new ModelFireBase();
    public interface AddPostListener {
        void onComplete();
    }

    public void addPost(String title,String post,String address,String price) {
        String userId = user.getId();
        String postsNum =Integer.toString(user.getPosts().size());
        Post newPost = new Post(title, post, address , price, userId + "."+ postsNum);
        modelFirebase.addPost(newPost);
        user.posts.add(newPost);
    }

    public void setLoggedUser(User user)
    {
        this.user=user;
    }

    public static final Model instance = new Model();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    public interface GetAllPostsListener {
        void onComplete(List<Post> list);
    }

    MutableLiveData<List<Post>> postsList = new MutableLiveData<List<Post>>();

    public LiveData<List<Post>> getAll() {
        return postsList;
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
        }));

//    public enum StudentListLoadingState {
//        loading,
//        loaded
//    }
//
//    MutableLiveData<StudentListLoadingState> studentListLoadingState = new MutableLiveData<StudentListLoadingState>();
//
//    public LiveData<StudentListLoadingState> getStudentListLoadingState() {
//        return studentListLoadingState;
//    }
    }
    private Model() {
            //  studentListLoadingState.setValue(StudentListLoadingState.loaded);
        }

}
