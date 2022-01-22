package com.example.agrishare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.agrishare.model.Post;
import com.example.agrishare.model.Model;


public class PostDetailsFragment extends Fragment {
    TextView Title;
    TextView Post;
    TextView Price;
    TextView Address;
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
        Title.setText(post.getTitle());
        Post.setText(post.getPost());
        Price.setText(post.getPrice());
        Address.setText(post.getAddress());
//        nameTv.setTextSize(20);
//        idTv.setTextSize(20);
//        phoneTv.setTextSize(20);
//        addressTv.setTextSize(20);
        DeleteBtn = view.findViewById(R.id.details_delete_btn);
        DeleteBtn.setOnClickListener((v) -> Navigation.findNavController(v).navigateUp());
        EditBtn = view.findViewById(R.id.details_edit_btn);
        EditBtn.setOnClickListener(v -> {
            save();
            Navigation.findNavController(v).navigateUp();
        });
        return view;
    }


    private void save() {
//        EditBtn.setEnabled(false);
//        DeleteBtn.setEnabled(false);
//        progressBar.setVisibility(View.VISIBLE);
//        camBtn.setEnabled(false);
//        galleryBtn.setEnabled(false);
        String title = Title.getText().toString();
        String post = Post.getText().toString();
        String address = Address.getText().toString();
        String price = Price.getText().toString();
        Model.instance.editPost(title, post, address, price,PId);
    }
}