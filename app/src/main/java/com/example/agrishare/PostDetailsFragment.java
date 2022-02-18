package com.example.agrishare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.agrishare.model.Post;
import com.example.agrishare.model.Model;


public class PostDetailsFragment extends Fragment {
    EditText Title;
    EditText Post;
    EditText Price;
    EditText Address;
    Button DeleteBtn;
    Button EditBtn;
    Long PId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);
        Title = view.findViewById(R.id.details_title);
        Post = view.findViewById(R.id.details_post);
        Price = view.findViewById(R.id.details_price);
        Address = view.findViewById(R.id.details_address);
        PId= PostDetailsFragmentArgs.fromBundle(getArguments()).getPos();
        Post post = Model.instance.getPostById(PId);
        displayPost(post);

        DeleteBtn = view.findViewById(R.id.details_delete_btn);
        DeleteBtn.setOnClickListener((v) -> Navigation.findNavController(v).navigateUp());
        EditBtn = view.findViewById(R.id.details_edit_btn);
        EditBtn.setOnClickListener(v -> {
            save();
            Navigation.findNavController(v).navigateUp();
        });
        setButtonVisibility(post);
        return view;
    }

    public void displayPost(Post post){
        Title.setText(post.getTitle());
        Post.setText(post.getPost());
        Price.setText(post.getPrice());
        Address.setText(post.getAddress());
    }

    public void setButtonVisibility(Post post){
        boolean flag = false;
        for (int i = 0; i < Model.instance.getUser().getPosts().size() ; i++) {
            if (Model.instance.getUser().getPosts().get(i).getId().equals(post.getId())) {
                flag = true;
                break;
            }
        }
        if(flag) {
            EditBtn.setVisibility(View.VISIBLE);
            DeleteBtn.setVisibility(View.VISIBLE);
        }
        else{
            EditBtn.setVisibility(View.GONE);
            DeleteBtn.setVisibility(View.GONE);
        }
    }

    private void save() {
//        camBtn.setEnabled(false);
//        galleryBtn.setEnabled(false);
        String title = Title.getText().toString();
        String post = Post.getText().toString();
        String address = Address.getText().toString();
        String price = Price.getText().toString();
        Model.instance.editPost(title, post, address, price,PId);
    }
}