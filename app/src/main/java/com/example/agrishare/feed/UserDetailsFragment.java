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
import com.example.agrishare.model.User;
import com.squareup.picasso.Picasso;
import java.io.InputStream;

public class UserDetailsFragment extends Fragment {
    EditText Name;
    EditText Email;
    EditText Address;
    EditText Phonenumber;
    Button EditBtn;
    ImageView avatar;
    User user;
    ImageButton cameraBtn;
    ImageButton galleryBtn;
    Bitmap imageBitMap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        Name = view.findViewById(R.id.edituser_name);
        Email = view.findViewById(R.id.edituser_email);
        Address = view.findViewById(R.id.edituser_address);
        Phonenumber = view.findViewById(R.id.edituser_phonenumber);
        avatar = view.findViewById(R.id.details_user_img);
        cameraBtn = view.findViewById(R.id.edituser_cam_btn);
        galleryBtn = view.findViewById(R.id.edituser_gallery_btn);
        cameraBtn.setOnClickListener(v -> openCamera());
        galleryBtn.setOnClickListener(v -> openGalley());
        Email.setEnabled(false);
        Model.instance.getUser(user1 -> {
            displayUser(user1);
            if (!user1.getAvatarUrl().equals("")) {
                Picasso.get().load(user1.getAvatarUrl()).into(avatar);
            }
        });
        EditBtn = view.findViewById(R.id.edituser_edit_btn);
        EditBtn.setOnClickListener(v -> {
            save();
            Model.instance.setLoggedUser(this.user);
            Navigation.findNavController(v).navigateUp();
        });
        return view;
    }

    public void displayUser(User user){
        Name.setText(user.getName());
        Email.setText(user.getEmail());
        Address.setText(user.getAddress());
        Phonenumber.setText(user.getPhoneNumber());
        this.user=user;
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
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitMap = (Bitmap) extras.get("data");
                avatar.setImageBitmap(imageBitMap);
            }
        } else if (requestCode == REQUEST_IMAGE_PICK) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    imageBitMap = BitmapFactory.decodeStream(imageStream);
                    avatar.setImageBitmap(imageBitMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed to select image from gallery", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void save() {
        cameraBtn.setEnabled(false);
        galleryBtn.setEnabled(false);
        this.user.setName(Name.getText().toString());
        this.user.setAddress(Address.getText().toString());
        this.user.setPhoneNumber(Phonenumber.getText().toString());
        if (imageBitMap != null)
            Model.instance.saveUserImage(imageBitMap, this.user.getId() + ".jpg", url ->{
                this.user.setAvatarUrl(url);
                Model.instance.editUser(this.user);
            });
        else
            Model.instance.editUser(this.user);
    }
}