package com.example.agrishare.model;

import java.util.HashMap;
import java.util.Map;

public class Model {


    ModelFireBase modelFirebase = new ModelFireBase();
    public interface AddPostListener {
        void onComplete();
    }

    public void addPost(Post post) {
        modelFirebase.addPost(post);
    }

    public static final Model instance = new Model();

    Map<String, String> map = new HashMap<String,String>();     //Temporary instead of out DB


    private Model(){}


    public boolean logIn(String userName,String password){      // log in function in ModelFireBase
        String pas = map.get(userName);
        if (pas!=null){
            return pas.equals(password);
        }
        return false;
    }

    public boolean register(String userName,String password){       // register function in ModelFireBase
        String pas = map.get(userName);
        if (pas!=null){
            return false;
        }
        map.put(userName, password);
        return true;
    }
}
