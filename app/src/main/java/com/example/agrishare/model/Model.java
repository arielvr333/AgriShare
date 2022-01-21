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


    ModelFireBase modelFirebase = new ModelFireBase();
    public interface AddPostListener {
        void onComplete();
    }

    public void addPost(Post post) {
        modelFirebase.addPost(post);
    }

    public static final Model instance = new Model();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    public interface GetAllPostsListener {
        void onComplete(List<Post> list);
    }

    MutableLiveData<List<Post>> studentsList = new MutableLiveData<List<Post>>();

    public LiveData<List<Post>> getAll() {
        return studentsList;
    }


    public void getAllPosts(GetAllPostsListener listener) {
        executor.execute(() -> {
            List<Post> list = AppLocalDB.db.PostDao().getAll();
            mainThread.post(() -> {
                listener.onComplete(list);
            });
        });


        ModelFireBase.getAllPosts(list -> {
            // add all records to the local db
            executor.execute(() -> {
                Long lud = new Long(0);
                Log.d("TAG", "firebase returned " + list.size());
                for (Post post : list)
                    AppLocalDB.db.PostDao().insertAll(post);
                MYApplication.getContext()
                        .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                        .edit()
                        .putLong("PostsLastUpdateDate", lud)
                        .apply();

                List<Post> postList = AppLocalDB.db.PostDao().getAll();
            });
        });

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
