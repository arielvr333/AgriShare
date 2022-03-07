package com.example.agrishare.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.agrishare.MYApplication;
import com.example.agrishare.feed.MainActivity;
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

    public interface SaveImageListener {
        void onComplete(String url);
    }
    public interface PostAvatar {
        void onComplete(Post post);
    }

    public void saveImage(Bitmap imageBitMap, String imageName, SaveImageListener listener) {
        ModelFireBase.saveImage(imageBitMap, imageName, listener);
    }

    public enum PostListLoadingState {
        loading,
        loaded
    }

    MutableLiveData<PostListLoadingState> postListLoadingState = new MutableLiveData<PostListLoadingState>();

    public LiveData<PostListLoadingState> getPostListLoadingState() {
        return postListLoadingState;
    }

    public boolean userIsWriter(Post post) {
        return this.user.Id.equals(post.getWriterId());
    }

    private Model() {
        postListLoadingState.setValue(PostListLoadingState.loaded);
    }

    public void addPost(Post newPost) {
        newPost.setWriterId(user.getId());
        modelFirebase.addPost(newPost, this::refreshPostList);
    }

    public void editPost(Post post){
        modelFirebase.addPost(post, this::refreshPostList);
    }

    public void getPublisherByPost(ModelFireBase.GetAllUsersListener listener) {
        modelFirebase.getAllUsers(listener);
    }

    public void setLoggedUser(User user) {
        this.user = user;
    }

    public LiveData<List<Post>> getAll() {
        if (postsList.getValue() == null)
            refreshPostList();
        return postsList;
    }

    public void getPostById(Long id,PostAvatar listener) {
        for (Post p : postsList.getValue())
            if (p.getId().equals(id))
                listener.onComplete(p);
    }

    public void refreshPostList() {
        postListLoadingState.setValue(PostListLoadingState.loading);
        Long lastUpdateDate = MYApplication.getAppContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("lastUpdateDate", 0);
        modelFirebase.getAllPosts(lastUpdateDate, list -> executor.execute(() -> {
            Long lud = new Long(0);
            for (Post post : list) {
                if (!post.getDisplayPost())
                    AppLocalDB.db.PostDao().delete(post);
                else
                    AppLocalDB.db.PostDao().insertAll(post);
                if (lud < post.getUpdateDate())
                    lud = post.getUpdateDate();
            }
            MYApplication.getAppContext()
                    .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                    .edit()
                    .putLong("PostsLastUpdateDate", lud)
                    .commit();
            List<Post> PostList = AppLocalDB.db.PostDao().getAll();
            postsList.postValue(PostList);
            postListLoadingState.postValue(PostListLoadingState.loaded);
        }));
    }

    public void loginUser(String email, String password, ModelFireBase.loginListener listener) {
        modelFirebase.loginUser(email, password, listener);
    }

    public void registerUser(String Email, String name, String password, String address, String phoneNumber, ModelFireBase.RegisterListener listener) {
        modelFirebase.registerUser(Email, name, password, address, phoneNumber, listener);
    }

    public boolean isSignedIn() {
        return modelFirebase.isSignedIn();
    }

    public void logout(ModelFireBase.LogoutListener listener) {
        modelFirebase.logout(listener);
    }
}
