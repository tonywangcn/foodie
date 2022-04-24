package com.example.foodie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import com.example.foodie.service.UserService;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final UserService userService = new UserService();

    private EditText email;
    private EditText password;

    public void onClick(View view) {
        String emailTxt;
        String passwordTxt;

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);

        emailTxt = email.getText().toString();
        passwordTxt = password.getText().toString();

        if (!userService.emailValidator(emailTxt)) {
            email.setError("Invalid Email！");
            email.requestFocus();
            return;
        }

        if (passwordTxt.length() < 8 && !userService.passwordValidator(passwordTxt)) {
            password.setError("Password must contain minimum 8 characters at least 1 Alphabet, 1 Number and 1 Special Character!");
            password.requestFocus();
            return;
        }


        MutableLiveData<Boolean> isSigned = userService.SignIn(emailTxt, passwordTxt);
        isSigned.observe(this, ok -> {
            if (ok) {
                Log.i(TAG,"Signin sucessfully!");
                Intent explore = new Intent(LoginActivity.this, ExploreActivity.class);
                startActivity(explore);
            } else {
                Log.e(TAG,"Failed to login!!!");
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Failed to Login!!")
                        .setPositiveButton("ok", null)
                        .setMessage( "Email and password don't match !!\n Please Try Again!"  )
                        .show();
            }
        });


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public  void SignUp(View view) {
        Intent signup = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(signup);
    }
}