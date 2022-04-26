package com.example.foodie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.example.foodie.adapter.MenuAdapter;
import com.example.foodie.model.Menu;
import com.example.foodie.model.Restaurant;
import com.example.foodie.service.MenuService;
import com.example.foodie.service.RestaurantService;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import jp.wasabeef.picasso.transformations.CropSquareTransformation;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    SharedPreferences settings;
    private String user;
    private Restaurant restaurant;
    private TextView name;
    private TextView address;
    private TextView openTime;
    private TextView openStatus;
    private TextView description;
    private ImageView image;
    private ImageView like;
    private ImageView shadow;
    private FloatingActionButton checkout;
    private ImageButton back;

    private RecyclerView menuView;
    private MenuAdapter menuAdapter;
    private ArrayList<Menu> menus;
    private MenuService menuService = new MenuService();
    private CollapsingToolbarLayout collapsingToolbarLayout;

    public void onClick(View view) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        restaurant = (Restaurant)intent.getSerializableExtra("restaurant");
        Log.d(TAG,"restaurant "+ restaurant.toString());

        settings = getSharedPreferences("UserInfo", 0);
        user = settings.getString("email","");

        setRestaurantView(restaurant);

        menuView = (RecyclerView)findViewById(R.id.menus);
        menuView.setLayoutManager(new GridLayoutManager(DetailActivity.this, 2));


        menus = new ArrayList<>();
        MutableLiveData<ArrayList<Menu>> menusLiveData =  menuService.getMenus(restaurant.getId());
        menusLiveData.observe(this, new Observer<ArrayList<Menu>>() {
            @Override
            public void onChanged(ArrayList<Menu> liveMenus) {
                Log.d(TAG, "onChanged: " + liveMenus.size());
                menus = liveMenus;
                menuAdapter = new MenuAdapter(DetailActivity.this, liveMenus, restaurant, user);
                menuView.setAdapter(menuAdapter);
                menuAdapter.notifyDataSetChanged();
                menuView.scheduleLayoutAnimation();

                
            }
        });

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
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

        checkout = findViewById(R.id.checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"checkout clicked!!!");
                Intent checkout = new Intent(view.getContext(), OrderActivity.class);
                checkout.putExtra("restaurant", restaurant);
                view.getContext().startActivity(checkout);
            }
        });


    }


    private void setRestaurantView(Restaurant restaurant) {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        name = findViewById(R.id.name);
        name.setText(restaurant.getName());

        address = findViewById(R.id.address);
        address.setText(restaurant.getAddress());

        openTime = findViewById(R.id.openTime);
        openTime.setText(restaurant.getOpenTime().toString() + "AM" + " to " + restaurant.getCloseTime().toString()+ "PM" + " - ");

        openStatus = findViewById(R.id.openStatus);
        if(hour >= restaurant.getOpenTime() && hour <= restaurant.getCloseTime()) {
            openStatus.setText("Open now");
            openStatus.setTextColor(Color.BLUE);
        } else {
            openStatus.setText("Closed!");
            openStatus.setTextColor(Color.RED);
        }

        description = findViewById(R.id.description);
        description.setText(restaurant.getDescription());

        shadow = findViewById(R.id.shadow);
        shadow.setBackgroundColor(Color.GRAY);
        shadow.getBackground().setAlpha(100);

        image = findViewById(R.id.image);
        Picasso.get()
                .load(Uri.parse(restaurant.getImage()))
                .fit()
                .into(image);
    }
}