package com.example.foodie;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import com.example.foodie.model.User;
import com.example.foodie.service.UserService;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;



import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = SignupActivity.class.getSimpleName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final UserService userService = new UserService();

    private EditText email;
    private EditText username;
    private EditText password;
    private Button signUp;

    public void onClick(View view) {

        String emailTxt = email.getText().toString();
        String usernameTxt = username.getText().toString();
        String passwordTxt = password.getText().toString();

        if (usernameTxt.trim().length() == 0 ) {
            username.setError("Name can't be empty!");
            username.requestFocus();
            return;
        }

        if (!userService.emailValidator(emailTxt)) {
            email.setError("Invalid Email!");
            email.requestFocus();
            return;
        }

        if (passwordTxt.length() < 8 && !userService.passwordValidator(passwordTxt)) {
            password.setError("Password must contain minimum 8 characters at least 1 Alphabet, 1 Number and 1 Special Character!");
            password.requestFocus();
            return;
        }



        MutableLiveData<User> user = userService.getUserDocument(emailTxt);
        user.observe(this, data -> {
            if (null != data) {
                Log.i(TAG, "Email is registered! " + data.getEmail());
                email.setError("Email is registered! Please try another one !");
                email.requestFocus();
                return;
            } else {
                User u = new User(usernameTxt, emailTxt);
                try {
                    u.hashPassword(passwordTxt);
                } catch (Exception e) {
                    Log.e(TAG,"failed to hash password " + e.getMessage());
                    userService.Msg(SignupActivity.this,"Something wrong happened! Please try again!", true);
                    return;
                }

                MutableLiveData<Boolean> isSaved = userService.saveUser(u);
                isSaved.observe(this, ok -> {
                    if (ok) {
                        userService.Msg(SignupActivity.this,"User register sucessfully, redirecting to login page", false);
                        Intent login = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(login);
                        return;
                    }
                    Log.e(TAG,"Failed to save user: " + u.toString());
                    userService.Msg(SignupActivity.this, "Failed to register! Please try again later !", true);
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email = (EditText)findViewById(R.id.email);
        username = (EditText)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);

    }

    public void SignIn(View view) {
        Intent login = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(login);
    }
}