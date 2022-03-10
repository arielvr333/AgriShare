package com.example.agrishare.feed;

import static android.app.Activity.RESULT_OK;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.agrishare.R;
import com.example.agrishare.model.Post;
import com.example.agrishare.model.Model;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class PostDetailsFragment extends Fragment {
    EditText Title;
    EditText Post;
    EditText Price;
    EditText Address;
    TextView Publisher;
    Button DeleteBtn;
    Button EditBtn;
    Long PId;
    ImageView avatar;
    Post post;
    ImageButton cameraBtn;
    ImageButton galleryBtn;
    Bitmap imageBitMap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);
        Title = view.findViewById(R.id.details_title);
        Post = view.findViewById(R.id.details_post);
        Price = view.findViewById(R.id.details_price);
        Address = view.findViewById(R.id.details_address);
        Publisher = view.findViewById(R.id.details_post_publisher);
        PId = PostDetailsFragmentArgs.fromBundle(getArguments()).getPos();
        avatar = view.findViewById(R.id.details_post_img);
        cameraBtn = view.findViewById(R.id.editpost_cam_btn);
        galleryBtn = view.findViewById(R.id.editpost_gallery_btn);
        cameraBtn.setOnClickListener(v -> openCamera());
        galleryBtn.setOnClickListener(v -> openGalley());

        Model.instance.getPostById(PId, post -> {
            displayPost(post);
            if (!post.getAvatarUrl().equals("")) {
                Picasso.get()
                        .load(post.getAvatarUrl())
                        .into(avatar);
            }
        });
        DeleteBtn = view.findViewById(R.id.details_delete_btn);
        DeleteBtn.setOnClickListener((v) -> {
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
        this.post=post;
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
            cameraBtn.setVisibility(View.VISIBLE);
            galleryBtn.setVisibility(View.VISIBLE);
            Title.setEnabled(true);
            Post.setEnabled(true);
            Price.setEnabled(true);
            Address.setEnabled(true);
            Publisher.setEnabled(true);
        }
        else{
            EditBtn.setVisibility(View.GONE);
            DeleteBtn.setVisibility(View.GONE);
            cameraBtn.setVisibility(View.GONE);
            galleryBtn.setVisibility(View.GONE);
            Title.setEnabled(false);
            Post.setEnabled(false);
            Price.setEnabled(false);
            Address.setEnabled(false);
            Publisher.setEnabled(false);
        }
    }

    static  final int REQUEST_IMAGE_CAPTURE=1;
    static final int REQUEST_IMAGE_PICK=2;
    private void openGalley() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_IMAGE_PICK);
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle extras=data.getExtras();
                imageBitMap = (Bitmap) extras.get("data");
                avatar.setImageBitmap(imageBitMap);
            }
        }else if(requestCode == REQUEST_IMAGE_PICK){
            if(resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    imageBitMap = BitmapFactory.decodeStream(imageStream);
                    avatar.setImageBitmap(imageBitMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),"Failed to select image from gallery",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void save(String type) {
        cameraBtn.setEnabled(false);
        galleryBtn.setEnabled(false);
        this.post.setTitle(Title.getText().toString());
        this.post.setPost(Post.getText().toString());
        this.post.setAddress(Address.getText().toString());
        this.post.setPrice(Price.getText().toString());
        this.post.setDisplayPost(type.equals("edit"));
        if (imageBitMap != null) {
            Model.instance.savePostImage(imageBitMap, this.post.getId().toString() + ".jpg", url -> {
                this.post.setAvatarUrl(url);
                Model.instance.editPost(this.post);
            });
        } else
            Model.instance.editPost(this.post);
    }
}