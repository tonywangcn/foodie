package com.example.foodie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.example.foodie.adapter.MenuAdapter;
import com.example.foodie.adapter.OrderAdapter;
import com.example.foodie.model.Global;
import com.example.foodie.model.Menu;
import com.example.foodie.model.Order;
import com.example.foodie.model.Restaurant;
import com.example.foodie.service.OrderService;
import com.example.foodie.service.RestaurantService;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    private static final String TAG = OrderActivity.class.getSimpleName();
    SharedPreferences settings;
    private OrderService orderService;
    private RestaurantService restaurantService;
    private String user;
    private Float subTotal;
    private Float total;
    private ArrayList<Order> orders;
    private RecyclerView orderView;
    private OrderAdapter orderAdapter;
    private Restaurant restaurant;
    private  Button checkout;
    private TextView subTotalView;
    private TextView deliveryFeeView;
    private Boolean hasDeliveryFee;
    private MutableLiveData<Boolean> hasFreeDeliveryLiveData;
    private TextView toPayView;
    private ImageButton back;

    private MutableLiveData<Float> subTotalLive;
    private MutableLiveData<ArrayList<Restaurant>> restaurantMutableLiveData;

    public void onClick(View view) {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        orderService = new OrderService(this);

       Intent intent = getIntent();
       restaurant = (Restaurant)intent.getSerializableExtra("restaurant");
       Log.d(TAG,"restaurant "+ restaurant.toString());
        settings = getSharedPreferences("UserInfo", 0);

        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Global global = (Global) getApplicationContext();
        ArrayList<Restaurant> restaurants = global.getRestaurants();
        Log.d(TAG,"restaurants from global" + restaurants.toString());

        deliveryFeeView = findViewById(R.id.deliveryFee);

        user = settings.getString("email","").toString();

        orderService = new OrderService(getApplicationContext());
        orders = orderService.getAllForUser(user);
        Log.d(TAG, "orders: " + orders.toString());

        hasDeliveryFee = orderService.hasDeliveryFee(restaurants);

        orderView = (RecyclerView)findViewById(R.id.orders);
        orderView.setLayoutManager(new LinearLayoutManager(OrderActivity.this));

        checkout = findViewById(R.id.checkout);
        subTotalView = findViewById(R.id.subtotal);
        toPayView = findViewById(R.id.toPay);
        subTotal = orderService.calculateTotalCost();

        subTotalView.setText(" $ " + String.format("%.2f", subTotal) );
        if (hasDeliveryFee) {
            deliveryFeeView.setText(" $ 10.00");
            total = subTotal + 10;
        } else {
            deliveryFeeView.setText(" $ 0.00");
            total = subTotal;


        }
        toPayView.setText(" $ " + String.format("%.2f", total));


        subTotalLive = new MutableLiveData<Float>();
        orderAdapter = new OrderAdapter(OrderActivity.this, orders, user, subTotalLive);
        orderView.setAdapter(orderAdapter);
        orderAdapter.notifyDataSetChanged();
        orderView.scheduleLayoutAnimation();

        subTotalLive.observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float subTotalLiveData) {
                Log.d(TAG," subTotalLiveData: " + subTotalLiveData.toString());
                subTotal = subTotalLiveData;
                subTotalView.setText(" $ " + String.format("%.2f",subTotal));

                total = subTotal + (hasDeliveryFee ? 10 : 0);
                toPayView.setText(" $ " + String.format("%.2f", total));

            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"checkout clicked!!! " + total.toString());
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Order Confirm!")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.d(TAG,"ok clicked!");
                                if (orderService.clear(user) ) {
                                    Log.d(TAG, "user order is cleared!");
                                } else {
                                    Log.d(TAG, "user order failed to be cleared!");
                                }
                                Intent explore = new Intent(view.getContext(), ExploreActivity.class);
                                view.getContext().startActivity(explore);

                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.d(TAG,"cancel clicked!");
                            }
                        })
                        .setMessage( "Are you sure going to pay?"  )
                        .show();
            }
        });
    }
}