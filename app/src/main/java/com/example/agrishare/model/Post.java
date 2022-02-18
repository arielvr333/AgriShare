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
    // Long updateDate = new Long(0);
    // String avatarUrl;
    String Address = "";
    String Price = "";
    Long Id;

    public Post() {
    }

    public Post(String title, String post, String address, String price, Long id) {
        Title = title;
        Post = post;
        Address = address;
        Price = price;
        Id = id;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
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
        Map<String, Object> DbPost = new HashMap<>();
        DbPost.put("Title", Title);
        DbPost.put("Post", Post);
        DbPost.put("Address", Address);
        DbPost.put("Price", Price);
        DbPost.put("Id", Id);
        // DbPost.put("updateDate", FieldValue.serverTimestamp());
        // DbPost.put("avatarUrl",avatarUrl);
        return DbPost;
    }

    public static Post create(Map<String, Object> DBpost) {
        String Title = (String) DBpost.get("Title");
        String Post = (String) DBpost.get("Post");
        String Address = (String) DBpost.get("Address");
        String Price = (String) DBpost.get("Price");
        Long Id = (Long) DBpost.get("Id");
        //Timestamp ts = (Timestamp)DBpost.get("updateDate");
        //Long updateDate = ts.getSeconds();
        //  String avatarUrl = (String)DBpost.get("avatarUrl");

        Post nPost = new Post(Title, Post, Address, Price, Id);
        //student.setUpdateDate(updateDate);
        //student.setAvatarUrl(avatarUrl);
        return nPost;
    }

//        public Long getUpdateDate() {
//            return updateDate;
//        }
//
//        public void setAvatarUrl(String url) {
//            avatarUrl = url;
//        }
//
//        public String getAvatarUrl() {
//            return avatarUrl;
//        }
}
