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

import com.example.foodie.OrderActivity;
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
    private Context context;
    private Order order;
    ImageButton add;
    ImageButton del;
    TextView number;

    public OrderAdapter(Context context, ArrayList<Order> orders, String user)
    {
        this.orderService = new OrderService(context);
        this.user = user;
        this.orders = orders;
        this.orders = orderService.getAllForUser(user);
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView name;
        TextView price;
        TextView count;

        public ViewHolder(View itemView)
        {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.price = itemView.findViewById(R.id.price);
            this.count = itemView.findViewById(R.id.number);
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
        order = orders.get(position);
        Log.d(TAG, "onBindViewHolder: test position " + position  +", " + orders.get(position).toString());
        holder.price.setText(" $ "+ String. format("%.2f",order.getPrice()));
        holder.count.setText(order.getCount().toString());
        holder.name.setText(order.getName());
        TextView number = holder.itemView.findViewById(R.id.number);

        ImageButton add = holder.itemView.findViewById(R.id.add);
        ImageButton del = holder.itemView.findViewById(R.id.del);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order = orders.get(position);

                Log.d(TAG,"incr clicked " + order.getName());

                if (order.getCount() >= 9) {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Failed!!")
                            .setPositiveButton("ok", null)
                            .setMessage( "Menu "+ order.getName() +" maximum number reached !!!"  )
                            .show();
                    return;
                }
                orderService.incrCount(user, order.getRestaurantId().toString(), order.getMenuId().toString());
                number.setText(String.valueOf(order.getCount() + 1));
                orders = orderService.getAllForUser(user);

                Log.d(TAG,"incr | order size: " + orders.size() + " restaurant id: " + order.getRestaurantId().toString() + ", menu id:" +order.getMenuId().toString() + ", count: " + ( order.getCount() + 1 ));
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order = orders.get(position);

                Log.d(TAG,"decr clicked " + order.getName());
                if (order.getCount() < 1) {
                    Log.d(TAG,order.getName()+" zero reached ");
                    return;
                }

                orderService.decrCount(user, order.getRestaurantId().toString(), order.getMenuId().toString());
                number.setText(String.valueOf(order.getCount() - 1));
                orders = orderService.getAllForUser(user);
                Log.d(TAG,"decr | order size: " + orders.size() + " restaurant id: " + order.getRestaurantId().toString() + ", menu id:" +order.getMenuId().toString() + ", count: " + (order.getCount() - 1));
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return orders.size();
    }
}