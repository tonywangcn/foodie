package com.example.foodie.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.foodie.model.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropSquareTransformation;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>  {
    private static final String TAG = MenuAdapter.class.getSimpleName();
    private List<Menu> menus;
    ImageButton add;
    ImageButton del;
    private Integer count = 0;
    TextView number;

    public MenuAdapter(Context context, List<Menu> data)
    {
        this.menus = data;
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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count >= 9) {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Failed!!")
                            .setPositiveButton("ok", null)
                            .setMessage( "Maximum number reached !!!"  )
                            .show();
                    return;
                }
                count++;
                if (count> 0) {
                    number.setText(count.toString());
                    del.setVisibility(View.VISIBLE);
                    number.setVisibility(View.VISIBLE);
                }
                Log.d(TAG,"+ count: " + count.toString());
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count > 0) {
                    count--;
                }
                Log.d(TAG, "- count: " + count.toString());
                number.setText(count.toString());
                if (count < 1) {
                    del.setVisibility(View.INVISIBLE);
                    number.setVisibility(View.INVISIBLE);
                }

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