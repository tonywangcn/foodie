package com.example.foodie.service;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.foodie.SignupActivity;
import com.example.foodie.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService extends Firebase {
    private static final String TAG = UserService.class.getSimpleName();
    private MutableLiveData<Boolean> isSaved;
    private MutableLiveData<Boolean> isValid;
    private MutableLiveData<User> user;
    
    public UserService() {
        super();
    }

    public MutableLiveData<Boolean> SignIn(String email, String password) {
        isValid = new MutableLiveData<Boolean>();

        db.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {

                    QuerySnapshot documents = (QuerySnapshot) task.getResult();
                    if (!documents.isEmpty()) {
                        for (DocumentSnapshot doc: documents) {
                            User u = doc.toObject(User.class);
                            try {
                                u.validatePassword(password);
                            } catch (Exception e) {
                                Log.e(TAG,e.getMessage());
                                isValid.postValue(false);
                                return;
                            }

                            isValid.postValue(true);
                            Log.d(TAG, "Found a User: " + u.toString());
                            return;
                        }
                    }
                }
                isValid.setValue(false);
                Log.d(TAG, "Failed to find user: "+ email);

            }
        });
        return isValid;
    }

    public MutableLiveData<Boolean> SignUp(String username, String email, String password) {
        User user = new User(username, email);
        try {
            user.setPassword(password);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            isSaved.postValue(false);
            return isSaved;
        }
        return saveUser(user);
    }

    public MutableLiveData<Boolean> saveUser(User user) {

        isSaved = new MutableLiveData<Boolean>();

        db.collection("users")
            .add(user)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(TAG, "User is saved into db: " + documentReference.getId());
                    isSaved.postValue(true);
                }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Failed to save user", e);
                        isSaved.postValue(false);
                    }
                });
        Log.d(TAG, "saveUser: " + isSaved.toString());
        return isSaved;
    }

    public MutableLiveData<User> getUserDocument(String email) {
        user = new MutableLiveData<User>();

        db.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {

                    QuerySnapshot documents = (QuerySnapshot) task.getResult();
                    if (!documents.isEmpty()) {
                        for (DocumentSnapshot doc: documents) {
                            User u = doc.toObject(User.class);
                            user.postValue(u);
                            Log.d(TAG, "Found a User: " + u.toString());
                            return;
                        }
                    }
                }
                user.setValue(null);
                Log.d(TAG, "Failed to find user: "+ email);

            }
        });
        return user;

    }
    public void Msg(Context c, String msg, Boolean isErr) {
        Toast toast;

        if (isErr) {
            toast = Toast.makeText(c, Html.fromHtml("<font color='#FF0000' ><b>" + msg + "</b></font>"), Toast.LENGTH_SHORT);
        } else {
            toast = Toast.makeText(c, msg, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static boolean passwordValidator(String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();

    }

    public static boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
