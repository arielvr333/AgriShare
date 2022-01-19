package com.example.agrishare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.agrishare.model.Post;
import com.example.agrishare.model.Model;


public class AddPostFragment extends Fragment {
    EditText Title;
    EditText Post;
    EditText Address;
    EditText UploadPhoto;
    EditText Price;
    Button saveBtn;
    Button cancelBtn;
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_add, container, false);
        v = view;
        Title = view.findViewById(R.id.addpost_title);
        Post = view.findViewById(R.id.addpost_post);
        Address = view.findViewById(R.id.addpost_address);
        Price = view.findViewById(R.id.addpost_price);
        saveBtn = view.findViewById(R.id.addspost_save_btn);
        cancelBtn = view.findViewById(R.id.addpost_cancel_btn);
        saveBtn.setOnClickListener(v -> {
            save();
            Navigation.findNavController(v).navigateUp();
        });
        cancelBtn.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        return view;
    }

    private void save() {
        saveBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
        //progressBar.setVisibility(View.VISIBLE);

        // camBtn.setEnabled(false);
        //galleryBtn.setEnabled(false);

        String title = Title.getText().toString();
        String post = Post.getText().toString();
        String address = Address.getText().toString();
        String price = Price.getText().toString();
        Post nPost = new Post(title, post, address, price);
        Model.instance.addPost(nPost);
/*
        if (imageBitmap == null) {
            Model.instance.addStudent(student, () -> {
                Navigation.findNavController(nameEt).navigateUp();
            });
        } else {
            Model.instance.saveImage(imageBitmap, id + ".jpg", url -> {
                student.setAvatarUrl(url);
                Model.instance.addStudent(student, () -> {
                    Navigation.findNavController(nameEt).navigateUp();
                });
            });
        }

 */
    }
}


