package com.example.foodie.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodie.ExploreActivity;
import com.example.foodie.R;
import com.example.foodie.model.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropSquareTransformation;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder>  {
    private static final String TAG = RestaurantAdapter.class.getSimpleName();
    private List<Restaurant> restaurants;
    private Context context;

    public RestaurantAdapter(Context context, List<Restaurant> data)
    {
        this.restaurants = data;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView name;
        TextView rating;
        TextView type;


        public ViewHolder(View itemView)
        {
            super(itemView);
            this.image = itemView.findViewById(R.id.image);
            this.name = itemView.findViewById(R.id.name);
            this.rating = itemView.findViewById(R.id.rating);
            this.type = itemView.findViewById(R.id.type);
        }
    }

    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        Restaurant restaurant = restaurants.get(position);
        Log.d(TAG, "onBindViewHolder: test position " + position + restaurant.getName() + restaurant.toString());
        holder.name.setText(restaurant.getName());
        holder.rating.setText(restaurant.getRating().toString());
        if (restaurant.getHasFreeDelivery()) {
            holder.type.setText("Free delivery");
            holder.type.setVisibility(View.VISIBLE);
        }


        //Picasso
        Picasso.get()
                .load(Uri.parse(restaurants.get(position).getImage())) // internet path
                .transform(new CropSquareTransformation())
                .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "Item +" + position + " is clicked!");
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return restaurants.size();
    }
}