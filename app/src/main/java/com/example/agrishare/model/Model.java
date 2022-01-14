package com.example.agrishare.model;

import java.util.HashMap;
import java.util.Map;

public class Model {

    public static final Model instance = new Model();

    Map<String, String> map = new HashMap<String,String>();


    private Model(){

    }


    public boolean logIn(String userName,String password){
        String pas = map.get(userName);
        if (pas!=null){
            if(pas.equals(password))
                return true;
        }
        return false;
    }

    public boolean register(String userName,String password){
        String pas = map.get(userName);
        if (pas!=null){
            return false;
        }
        map.put(userName, password);
        return true;
    }
}
