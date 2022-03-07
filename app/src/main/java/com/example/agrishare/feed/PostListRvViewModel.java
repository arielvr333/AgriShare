package com.example.agrishare.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrishare.model.Model;
import com.example.agrishare.model.Post;

import java.util.List;

public class PostListRvViewModel extends ViewModel {
    LiveData<List<Post>> data;

    public PostListRvViewModel(){
        data = Model.instance.getAll();
    }
    public LiveData<List<Post>> getData() {
        return data;
    }
}
