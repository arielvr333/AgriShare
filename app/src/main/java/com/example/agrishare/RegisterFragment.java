package com.example.agrishare;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.agrishare.model.ModelFireBase;

public class RegisterFragment extends Fragment {

    private EditText fullName;
    private EditText username;
    private EditText password;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {}   // Required empty public constructor

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_register, container, false);

        fullName = view.findViewById(R.id.regfrag_fullName_txt);
        username=view.findViewById(R.id.regfrag_userName_txt);
        password=view.findViewById(R.id.regfrag_password);

        Button login = view.findViewById(R.id.regfrag_login_btn);
        login.setOnClickListener((v)->{
            Navigation.findNavController(v).navigateUp();
        });

        Button register = view.findViewById(R.id.regfrag_register_btn);
        /*
        register.setOnClickListener((v)->{
            if(Model.instance.register(username.getText().toString(), password.getText().toString()))
                Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_homeFragment);
                // Navigation.findNavController(v).navigate(RegisterFragmentDirections.actionRegisterFragmentToHomeFragment(username.getText().toString()));
            else
                Toast.makeText(getContext(),"please choose a different username",Toast.LENGTH_LONG).show();
        });
*/
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag", "clicked");
                if (TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(fullName.getText().toString())|| TextUtils.isEmpty(password.getText().toString())){
                    Log.d("tag", "empty");
                    Toast.makeText(getContext(), "please fill all fields!", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6){
                    Log.d("tag", "less than 6");
                    Toast.makeText(getContext(), "Password too short!", Toast.LENGTH_SHORT).show();
                } else {
                    if(ModelFireBase.registerUser(username.getText().toString() , fullName.getText().toString()  , password.getText().toString())) {
                        Log.d("tag", "firebase return an true answer");
                        Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_homeFragment);
                    }
                        // Navigation.findNavController(v).navigate(RegisterFragmentDirections.actionRegisterFragmentToHomeFragment(username.getText().toString()));
                    else {
                        Log.d("tag", "fire base return false answer");
                        Toast.makeText(getContext(), "please choose a different username", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return view;
    }


}