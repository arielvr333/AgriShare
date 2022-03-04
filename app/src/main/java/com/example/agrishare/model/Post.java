package com.example.agrishare.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

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
    String Address = "";
    String Price = "";
    Long Id;
    String writerId;
    Boolean DisplayPost;
    private String avatarUrl;

    public Post() {}

    public Post(String title, String post, String address, String price, Long id, String postWriterId, Boolean display) {
        Title = title;
        Post = post;
        Address = address;
        Price = price;
        Id = id;
        writerId = postWriterId;
        DisplayPost = display;
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

    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    public Boolean getDisplayPost() {
        return DisplayPost;
    }

    public void setDisplayPost(Boolean displayPost) {
        DisplayPost = displayPost;
    }

    public Map<String, Object> toDB() {
        Map<String, Object> DbPost = new HashMap<>();
        DbPost.put("Title", Title);
        DbPost.put("Post", Post);
        DbPost.put("Address", Address);
        DbPost.put("Price", Price);
        DbPost.put("Id", Id);
        DbPost.put("writerId", writerId);
        DbPost.put("updateDate", FieldValue.serverTimestamp());
        DbPost.put("avatarUrl",avatarUrl);
        DbPost.put("displayPost", DisplayPost);
        return DbPost;
    }

    public static Post create(Map<String, Object> DbPost) {
        String Title = (String) DbPost.get("Title");
        String Post = (String) DbPost.get("Post");
        String Address = (String) DbPost.get("Address");
        String Price = (String) DbPost.get("Price");
        Long Id = (Long) DbPost.get("Id");
        String writerId = (String) DbPost.get("writerId");
        Timestamp ts = (Timestamp)DbPost.get("updateDate");
        Long updateDate = ts.getSeconds();
        String avatarUrl = (String)DbPost.get("avatarUrl");
        Boolean displayPost = (Boolean)DbPost.get("displayPost");
        Post post = new Post(Title, Post, Address, Price, Id ,writerId, displayPost);
        post.setUpdateDate(updateDate);
        post.setAvatarUrl(avatarUrl);
        return post;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public void setAvatarUrl(String url) {
            this.avatarUrl = url;
        }

    public String getAvatarUrl() {
        return avatarUrl;
    }

}
