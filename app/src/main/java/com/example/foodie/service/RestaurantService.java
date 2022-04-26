package com.example.foodie.service;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;


import com.example.foodie.model.Restaurant;
import com.example.foodie.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;


public class RestaurantService extends Firebase {
    private static final String TAG = RestaurantService.class.getSimpleName();
    private ArrayList<Restaurant> restaurants;

    private MutableLiveData<ArrayList<Restaurant>> results;

    public RestaurantService() {
        super();
    }

    public MutableLiveData<ArrayList<Restaurant>> getRestaurants() {

        results = new MutableLiveData<ArrayList<Restaurant>>();
        restaurants = new ArrayList<Restaurant>();

        db.collection("restaurants").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()) {

                    QuerySnapshot documents = (QuerySnapshot) task.getResult();
                    if (!documents.isEmpty()) {
                        for (DocumentSnapshot doc: documents) {
                            Restaurant r = doc.toObject(Restaurant.class);
                            restaurants.add(r);
                        }
                        results.postValue(restaurants);
                    }
                }
                Log.d(TAG, "Failed to find restaurants" );

            }
        });
        return results;
    }

}
