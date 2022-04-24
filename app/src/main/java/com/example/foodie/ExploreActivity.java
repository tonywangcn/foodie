package com.example.foodie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

import com.example.foodie.adapter.RestaurantAdapter;
import com.example.foodie.model.Restaurant;
import com.example.foodie.service.RestaurantService;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;

public class ExploreActivity extends AppCompatActivity {
    private static final String TAG = ExploreActivity.class.getSimpleName();
    private RecyclerView restaurantView;

    private ArrayList<Restaurant> restaurants;
    private final RestaurantService restaurantService = new RestaurantService();
    private RestaurantAdapter restaurantAdapter;
    private EditText search;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;

    public void onClick(View view) {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        //Initialize the RecyclerView
        restaurantView = (RecyclerView)findViewById(R.id.recyclerView);
        //Set the Layout Manager
        restaurantView.setLayoutManager(new GridLayoutManager(ExploreActivity.this, 2));

//        search = (EditText)findViewById(R.id.search);
//        String searchTxt = search.getText().toString();
//        if (searchTxt.trim().length() == 0) {
//            search.setError("Search can't be empty!");
//            search.requestFocus();
//            return;
//        }

        restaurants = new ArrayList<>();
        MutableLiveData<ArrayList<Restaurant>> restaurantsLiveData = restaurantService.getRestaurants();
        restaurantsLiveData.observe(this, new Observer<ArrayList<Restaurant>>() {
            @Override
            public void onChanged(ArrayList<Restaurant> restaurants) {
                Log.d(TAG, "onChanged: " + restaurants.size());
                restaurantAdapter = new RestaurantAdapter(ExploreActivity.this, restaurants);
                restaurantView.setAdapter(restaurantAdapter);
                restaurantAdapter.notifyDataSetChanged();
                restaurantView.scheduleLayoutAnimation();
            }
        });


        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d(TAG,"verticalOffset " + Integer.valueOf(verticalOffset).toString());
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
//                    toolbar.setVisibility(View.INVISIBLE);
                } else if (isShow) {
                    isShow = false;
//                    toolbar.setVisibility(View.VISIBLE);
                }
            }
        });

        restaurantView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i(TAG, " dx dy");
                if (dy > 0) {
                    Log.i(TAG,"Scrolling up");
                } else {
                    Log.i(TAG,"Scrolling down");
                }
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                Log.i(TAG,"onScrollStateChanged");
                Boolean ok = recyclerView.canScrollVertically(1);
                Log.i(TAG, " ok " + ok.toString());
                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
//                    toolbar.setVisibility(View.INVISIBLE);
                    Log.i(TAG,"end");
                } else {
//                    toolbar.setVisibility(View.VISIBLE);
                }
            }
        });

    }

}