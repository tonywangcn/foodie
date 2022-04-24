package com.example.foodie.service;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.example.foodie.model.Menu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class MenuService extends Firebase {
    private static final String TAG = MenuService.class.getSimpleName();
    private MutableLiveData<ArrayList<Menu>> menus;

    public MenuService() {
        super();
    }

    public MutableLiveData<ArrayList<Menu>> getMenus(Integer id) {
        menus = new MutableLiveData<ArrayList<Menu>>();
        ArrayList<Menu> results = new ArrayList<Menu>();

        db.collection("menus").whereEqualTo("restaurantId", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    QuerySnapshot documents = (QuerySnapshot) task.getResult();
                    if (!documents.isEmpty()) {
                        for (DocumentSnapshot doc : documents) {
                            Menu r = doc.toObject(Menu.class);
                            results.add(r);
                        }
                        menus.postValue(results);
                    }
                }
                Log.d(TAG, "Failed to find restaurants");

            }
        });
        return menus;
    }
}
