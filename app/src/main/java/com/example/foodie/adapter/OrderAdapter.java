package com.example.foodie.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodie.R;
import com.example.foodie.model.Menu;
import com.example.foodie.model.Order;
import com.example.foodie.model.Restaurant;
import com.example.foodie.service.OrderService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jp.wasabeef.picasso.transformations.CropSquareTransformation;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>  {
    private static final String TAG = MenuAdapter.class.getSimpleName();
    private String user;
    private Restaurant restaurant;
    private ArrayList<Order> orders;
    private OrderService orderService;
    private ArrayList<Order> currentOrder;
    ImageButton add;
    ImageButton del;
    TextView number;

    public OrderAdapter(Context context, ArrayList<Order> orders, String user)
    {
        this.orderService = new OrderService(context);
        this.user = user;
        this.orders = orders;
        this.orders = orderService.getAllForUser(user);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView name;
        TextView price;
        TextView count;

        public ViewHolder(View itemView)
        {
            super(itemView);
        }
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        Order order = orders.get(position);
        Log.d(TAG, "onBindViewHolder: test position " + position  +", " + orders.get(position).toString());
//        holder.name.setText(order);
        holder.price.setText(order.getPrice().toString());
        holder.count.setText(order.getCount().toString());
        holder.name.setText(order.getName());

       ImageButton add = holder.itemView.findViewById(R.id.add);
       ImageButton del = holder.itemView.findViewById(R.id.del);

//
//        Log.d(TAG,"Orders + " + orders.toString());
//        Log.d(TAG, "user: " + user + " , restaurant id: "+ restaurant.getId().toString() + " , menu id: " + menu.getId().toString());
//        currentOrder = new ArrayList<Order>( orders.stream().filter(order -> order.getUser().equals(user) && order.getRestaurantId().equals(restaurant.getId()) && order.getMenuId().equals(menu.getId())).collect(Collectors.<Order>toList()));
//        Log.d(TAG,"currentOrder: " + currentOrder.toString());
//        if (currentOrder.size() != 0) {
//            Log.d(TAG,"current order count: "+ currentOrder.get(0).getCount().toString());
//            if (currentOrder.get(0).getCount()>0) {
//                number.setText(currentOrder.get(0).getCount().toString());
//                del.setVisibility(View.VISIBLE);
//                number.setVisibility(View.VISIBLE);
//            }
//
//        }


    }

    @Override
    public int getItemCount()
    {
        return orders.size();
    }
}