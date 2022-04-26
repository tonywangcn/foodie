package com.example.foodie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.example.foodie.adapter.MenuAdapter;
import com.example.foodie.adapter.OrderAdapter;
import com.example.foodie.model.Menu;
import com.example.foodie.model.Order;
import com.example.foodie.model.Restaurant;
import com.example.foodie.service.OrderService;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    private static final String TAG = OrderActivity.class.getSimpleName();
    SharedPreferences settings;
    private OrderService orderService;
    private String user;
    private ArrayList<Order> orders;
    private RecyclerView orderView;
    private OrderAdapter orderAdapter;
    private Restaurant restaurant;
    private  Button checkout;
    private TextView subTotalView;
    private TextView toPayView;
    private MutableLiveData<Float> subTotal;

    public void onClick(View view) {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

//        Intent intent = getIntent();
//        restaurant = (Restaurant)intent.getSerializableExtra("restaurant");
//        Log.d(TAG,"restaurant "+ restaurant.toString());

        settings = getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("email","hello@hello.com");
        editor.commit();


        user = settings.getString("email","").toString();

        orderService = new OrderService(getApplicationContext());
        orders = orderService.getAllForUser(user);
        Log.d(TAG, "orders: " + orders.toString());

        orderView = (RecyclerView)findViewById(R.id.orders);
        orderView.setLayoutManager(new LinearLayoutManager(OrderActivity.this));

        checkout = findViewById(R.id.checkout);

        subTotalView = findViewById(R.id.subtotal);
        subTotalView.setText(" $ " + String.format("%.2f",orderService.calculateTotalCost()) );
        toPayView = findViewById(R.id.toPay);


        subTotal = new MutableLiveData<Float>();
        orderAdapter = new OrderAdapter(OrderActivity.this, orders, user, subTotal);
        orderView.setAdapter(orderAdapter);
        orderAdapter.notifyDataSetChanged();
        orderView.scheduleLayoutAnimation();

        subTotal.observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float subTotalLiveData) {
                Log.d(TAG," subTotalLiveData: " + subTotalLiveData.toString());
                subTotalView.setText(" $ " + String.format("%.2f",subTotalLiveData));
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float sub = orderService.calculateTotalCost();
                Log.d(TAG,"checkout clicked!!! " + sub.toString());

            }
        });
    }
}