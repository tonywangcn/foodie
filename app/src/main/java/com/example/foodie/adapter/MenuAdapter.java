package com.example.foodie.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodie.DetailActivity;
import com.example.foodie.ExploreActivity;
import com.example.foodie.LoginActivity;
import com.example.foodie.R;
import com.example.foodie.SignupActivity;
import com.example.foodie.model.Menu;
import com.example.foodie.model.Order;
import com.example.foodie.model.Restaurant;
import com.example.foodie.service.OrderService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jp.wasabeef.picasso.transformations.CropSquareTransformation;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>  {
    private static final String TAG = MenuAdapter.class.getSimpleName();
    private String user;
    private List<Menu> menus;
    private Restaurant restaurant;
    private ArrayList<Order> orders;
    private OrderService orderService;
    private ArrayList<Order> currentOrder;
    ImageButton add;
    ImageButton del;
    private Integer count = 0;
    TextView number;

    public MenuAdapter(Context context, List<Menu> data, Restaurant restaurant, String user)
    {
        this.orderService = new OrderService(context);
        this.menus = data;
        this.restaurant = restaurant;
        this.user = user;
        this.orders = orderService.getAll(user, restaurant.getId().toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView name;
        TextView price;


        public ViewHolder(View itemView)
        {
            super(itemView);
            this.image = itemView.findViewById(R.id.image);
            this.name = itemView.findViewById(R.id.name);
            this.price = itemView.findViewById(R.id.price);
        }
    }

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        Menu menu = menus.get(position);
        Log.d(TAG, "onBindViewHolder: test position " + position  + menu.toString());
        holder.name.setText(menu.getName());
        holder.price.setText("$ "+menu.getPrice().toString());
        ImageButton add = holder.itemView.findViewById(R.id.add);
        ImageButton del = holder.itemView.findViewById(R.id.del);

        TextView number = holder.itemView.findViewById(R.id.number);
        number.getBackground().setAlpha(150);;

        Log.d(TAG,"Orders + " + orders.toString());
        Log.d(TAG, "user: " + user + " , restaurant id: "+ restaurant.getId().toString() + " , menu id: " + menu.getId().toString());
        currentOrder = new ArrayList<Order>( orders.stream().filter(order -> order.getUser().equals(user) && order.getRestaurantId().equals(restaurant.getId()) && order.getMenuId().equals(menu.getId())).collect(Collectors.<Order>toList()));
        Log.d(TAG,"currentOrder: " + currentOrder.toString());
        if (currentOrder.size() != 0) {
            Log.d(TAG,"current order count: "+ currentOrder.get(0).getCount().toString());
            if (currentOrder.get(0).getCount()>0) {
                number.setText(currentOrder.get(0).getCount().toString());
                del.setVisibility(View.VISIBLE);
                number.setVisibility(View.VISIBLE);
            }

        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentOrder = new ArrayList<Order>( orders.stream().filter(order -> order.getUser().equals(user) && order.getRestaurantId().equals(restaurant.getId()) && order.getMenuId().equals(menu.getId())).collect(Collectors.<Order>toList()));

                if (currentOrder.size() == 0) {
                    Boolean ok = orderService.newOrder(user, restaurant.getId().toString(), menu.getId().toString(), menu.getName(),menu.getPrice());
                    number.setText("1");
                    del.setVisibility(View.VISIBLE);
                    number.setVisibility(View.VISIBLE);
                    Log.d(TAG,"new order created: " + ok);

                }  else {
                    if (currentOrder.get(0).getCount()>= 9) {
                        new AlertDialog.Builder(view.getContext())
                                .setTitle("Failed!!")
                                .setPositiveButton("ok", null)
                                .setMessage( "Maximum number reached !!!"  )
                                .show();
                        return;
                    }

                    orderService.incrCount(user, restaurant.getId().toString(), menu.getId().toString());
                    number.setText(String.valueOf( currentOrder.get(0).getCount() + 1));
                    del.setVisibility(View.VISIBLE);
                    number.setVisibility(View.VISIBLE);
                }
                orders = orderService.getAll(user, restaurant.getId().toString());

                Log.d(TAG,"incr | order size: " + orders.size() + " restaurant id: " + restaurant.getId().toString() + ", menu id:" +menu.getId().toString() + ", count: " + ( currentOrder.size() > 0 ? currentOrder.get(0).getCount() + 1 : 1));
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orders = orderService.getAll(user, restaurant.getId().toString());
                Log.d(TAG,"Orders + " + orders.toString());
                currentOrder = new ArrayList<Order>( orders.stream().filter(order -> order.getUser().equals(user) && order.getRestaurantId().equals( restaurant.getId()) && order.getMenuId().equals( menu.getId()) ).collect(Collectors.<Order>toList()));
                if (currentOrder.get(0).getCount() <= 1) {
                    del.setVisibility(View.INVISIBLE);
                    number.setVisibility(View.INVISIBLE);
                }
                number.setText(String.valueOf(currentOrder.get(0).getCount() - 1));
                orderService.decrCount(user, restaurant.getId().toString(), menu.getId().toString());
                orders = orderService.getAll(user, restaurant.getId().toString());
                Log.d(TAG,"decr | order size: " + orders.size() + " restaurant id: " + restaurant.getId().toString() + ", menu id:" +menu.getId().toString() + ", count: " + ( currentOrder.get(0).getCount() + 1));

            }
        });

        //Picasso
        Picasso.get()
                .load(Uri.parse(menus.get(position).getImage())) // internet path
                .transform(new CropSquareTransformation())
                .resize(600, 600)
                .centerCrop()
                .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Item +" + position + " is clicked! id: " + menu.getId().toString());
//                Intent detail = new Intent(v.getContext(), DetailActivity.class);
//                v.getContext().startActivity(detail);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return menus.size();
    }
}