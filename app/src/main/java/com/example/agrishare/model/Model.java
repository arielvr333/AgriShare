package com.example.agrishare.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.RequiresApi;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.agrishare.MYApplication;
import java.util.Collections;
import java.util.Comparator;
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
    MutableLiveData<PostListLoadingState> postListLoadingState = new MutableLiveData<PostListLoadingState>();
    private Model() {
        postListLoadingState.setValue(PostListLoadingState.loaded);
    }
    public LiveData<PostListLoadingState> getPostListLoadingState() {
        return postListLoadingState;
    }

    public interface SaveImageListener {
        void onComplete(String url);
    }
    public interface PostAvatar {
        void onComplete(Post post);
    }
    public interface UserAvatar {
        void onComplete(User user);
    }

    public void savePostImage(Bitmap imageBitMap, String imageName, SaveImageListener listener) {
        ModelFireBase.savePostImage(imageBitMap, imageName, listener);
    }
    public void saveUserImage(Bitmap imageBitMap, String imageName, SaveImageListener listener) {
        ModelFireBase.saveUserImage(imageBitMap, imageName, listener);
    }

    public enum PostListLoadingState {
        loading,
        loaded
    }

    public void getUser(UserAvatar listener) {
        listener.onComplete(this.user); }


    public boolean userIsWriter(Post post) {
        return this.user.Id.equals(post.getWriterId());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addPost(Post newPost) {
        newPost.setWriterId(user.getId());
        modelFirebase.addPost(newPost, this::refreshPostList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void editPost(Post post){ modelFirebase.addPost(post, this::refreshPostList); }

    public void editUser(User user){
        modelFirebase.editUser(user);
    }

    public void getPublisherByPost(ModelFireBase.GetAllUsersListener listener) {
        modelFirebase.getAllUsers(listener);
    }

    public void setLoggedUser(User user) {
        this.user = user;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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

    @RequiresApi(api = Build.VERSION_CODES.N)
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
            Collections.sort(PostList, Comparator.comparing(Post::getUpdateDate));
            Collections.reverse(PostList);
            postsList.postValue(PostList);
            postListLoadingState.postValue(PostListLoadingState.loaded);
        }));
    }

    public void loginUser(String email, String password, ModelFireBase.loginListener listener) {
        modelFirebase.loginUser(email, password, listener);
    }

    public void registerUser(String Email, String name, String password, String address, String phoneNumber, ModelFireBase.RegisterListener listener) {
        modelFirebase.registerUser(Email, name, password, address, phoneNumber, listener,"");
    }

    public boolean isSignedIn() {
        return modelFirebase.isSignedIn();
    }

    public void logout(ModelFireBase.LogoutListener listener) {
        modelFirebase.logout(listener);
    }
}
