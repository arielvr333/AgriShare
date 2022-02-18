package com.example.agrishare;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.agrishare.model.Model;
import com.example.agrishare.model.ModelFireBase;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private EditText username;
    private EditText password;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private com.example.agrishare.model.ModelFireBase ModelFireBase;

    public LoginFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        username = view.findViewById(R.id.loginfrag_userName_txt);
        password = view.findViewById(R.id.loginfrag_pass_txt);
        view.findViewById(R.id.loginfrag_register_btn).setOnClickListener
                (Navigation.createNavigateOnClickListener
                        (R.id.action_loginFragment_to_registerFragment));

        view.findViewById(R.id.loginfrag_login_btn).setOnClickListener(v -> {
            String txt_email = username.getText().toString();
            String txt_password = password.getText().toString();

            if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                Toast.makeText(getContext(), "please enter email and password!", Toast.LENGTH_LONG).show();
            } else {
                Model.instance.loginUser(txt_email, txt_password, bool -> {
                    if (bool)
                        Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_postListRvFragment);
                    else
                        Toast.makeText(getContext(), "The password or username is incorrect", Toast.LENGTH_LONG).show();
                });
            }
        });
        return view;
    }
}

