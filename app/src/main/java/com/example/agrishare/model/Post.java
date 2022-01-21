package com.example.agrishare.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;


@Entity
public class Post {
        final public static String COLLECTION_NAME = "Posts";
        @PrimaryKey
        @NonNull
        String Title = "";
        String Post = "";
        Long updateDate = new Long(0);
        String avatarUrl;
        String Address = "";
        String Price = "";
        String Id;


        public void setUpdateDate(Long updateDate) {
            this.updateDate = updateDate;
        }

        public Post(){}

    public Post(String title, String post, String address, String price,String id) {
        Title = title;
        Post = post;
        Address = address;
        Price = price;
        Id=id;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPost() {
        return Post;
    }

    public void setPost(String post) {
        Post = post;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public Map<String, Object> toDB() {
            Map<String, Object> DBpost = new HashMap<String, Object>();
        DBpost.put("Title",Title);
        DBpost.put("Post",Post);
        DBpost.put("Address",Address);
        DBpost.put("Price",Price);
        DBpost.put("Id",Id);
           // json.put("updateDate", FieldValue.serverTimestamp());
           // json.put("avatarUrl",avatarUrl);
            return DBpost;
        }

        public static Post create(Map<String, Object> DBpost) {
            String Title = (String) DBpost.get("Title");
            String Post = (String) DBpost.get("Post");
            String Address = (String) DBpost.get("Address");
            String Price = (String) DBpost.get("Price");
            String Id = (String) DBpost.get("Id");
            //Timestamp ts = (Timestamp)DBpost.get("updateDate");
            //Long updateDate = ts.getSeconds();
          //  String avatarUrl = (String)DBpost.get("avatarUrl");

            Post nPost = new Post(Title,Post,Address,Price,Id);
            //student.setUpdateDate(updateDate);
            //student.setAvatarUrl(avatarUrl);
            return nPost;
        }

        public Long getUpdateDate() {
            return updateDate;
        }

        public void setAvatarUrl(String url) {
            avatarUrl = url;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }
    }
