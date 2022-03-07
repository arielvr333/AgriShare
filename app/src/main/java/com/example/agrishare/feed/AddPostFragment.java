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
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.agrishare.R;
import com.example.agrishare.model.Model;
import com.example.agrishare.model.Post;
import java.io.InputStream;


public class AddPostFragment extends Fragment {
    EditText Title;
    EditText Post;
    EditText Address;
    EditText Price;
    Button saveBtn;
    Button cancelBtn;
    View v;
    ImageView avatar;
    ImageButton cameraBtn;
    ImageButton galleryBtn;
    Bitmap imageBitMap;
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
        cameraBtn= view.findViewById(R.id.addpost_cam_btn);
        galleryBtn= view.findViewById(R.id.addpost_gallery_btn);
        avatar = view.findViewById(R.id.addpost_imageView);
        cameraBtn.setOnClickListener(v -> openCamera());
        galleryBtn.setOnClickListener(v -> openGalley());
        return view;
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
            if (resultCode == RESULT_OK)
            {
                Bundle extras=data.getExtras();
                imageBitMap = (Bitmap) extras.get("data");
                avatar.setImageBitmap(imageBitMap);
            }
        }else if(requestCode == REQUEST_IMAGE_PICK){
            if(resultCode == RESULT_OK)
            {
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

    private void save() {
        saveBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
        cameraBtn.setEnabled(false);
        galleryBtn.setEnabled(false);

        String title = Title.getText().toString();
        String post = Post.getText().toString();
        String address = Address.getText().toString();
        String price = Price.getText().toString();
        Long postId=  System.currentTimeMillis();
        if (imageBitMap != null) {
            Model.instance.saveImage(imageBitMap, postId + ".jpg", url -> Model.instance.addPost(new Post(title, post, address, price, postId, "", true, url)));
        }
        else {
            Model.instance.addPost(new Post(title, post, address, price, postId, "", true, ""));

        }
    }
}


