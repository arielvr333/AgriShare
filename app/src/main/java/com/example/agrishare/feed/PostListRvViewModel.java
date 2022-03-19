package com.example.agrishare.feed;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrishare.model.Model;
import com.example.agrishare.model.Post;

import java.util.List;

public class PostListRvViewModel extends ViewModel {
    LiveData<List<Post>> data;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public PostListRvViewModel(){
        data = Model.instance.getAll();
    }
    public LiveData<List<Post>> getData() {
        return data;
    }
}
