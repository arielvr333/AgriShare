package com.example.agrishare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.agrishare.model.Post;
import com.example.agrishare.model.Model;

public class PostDetailsFragment extends Fragment {
    EditText Title;
    EditText Post;
    EditText Price;
    EditText Address;
    TextView Publisher;
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
        Publisher = view.findViewById(R.id.details_post_publisher);
        PId= PostDetailsFragmentArgs.fromBundle(getArguments()).getPos();
        Post post = Model.instance.getPostById(PId);
        displayPost(post);

        DeleteBtn = view.findViewById(R.id.details_delete_btn);
        DeleteBtn.setOnClickListener((v) ->{
            save("delete");
            Navigation.findNavController(v).navigateUp();
        });
        EditBtn = view.findViewById(R.id.details_edit_btn);
        EditBtn.setOnClickListener(v -> {
            save("edit");
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
        Model.instance.getPublisherByPost(list -> {
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getId().equals(post.getWriterId()))
                   Publisher.setText("posted by : " + list.get(i).getName());
            }
        });
    }

    public void setButtonVisibility(Post post){
        if(Model.instance.userIsWriter(post)) {
            EditBtn.setVisibility(View.VISIBLE);
            DeleteBtn.setVisibility(View.VISIBLE);
            Title.setEnabled(true);
            Post.setEnabled(true);
            Price.setEnabled(true);
            Address.setEnabled(true);
            Publisher.setEnabled(true);
        }
        else{
            EditBtn.setVisibility(View.GONE);
            DeleteBtn.setVisibility(View.GONE);
            Title.setEnabled(false);
            Post.setEnabled(false);
            Price.setEnabled(false);
            Address.setEnabled(false);
            Publisher.setEnabled(false);
        }
    }

    private void save(String type) {
//        camBtn.setEnabled(false);
//        galleryBtn.setEnabled(false);
        String title = Title.getText().toString();
        String post = Post.getText().toString();
        String address = Address.getText().toString();
        String price = Price.getText().toString();
        Model.instance.editPost(title, post, address, price,PId, type.equals("edit"));

    }
}