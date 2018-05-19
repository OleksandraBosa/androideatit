package com.example.sasha.androideatit.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sasha.androideatit.Interface.ItemClickListener;
import com.example.sasha.androideatit.R;

/**
 * Created by sasha on 06.04.18.
 */

public class UsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtUserPhone;
    public TextView txtUserName;
    public TextView txtUserStatus;
    public ImageView imageView;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public UsersViewHolder(View itemView){
        super(itemView);

        txtUserPhone= (TextView)itemView.findViewById(R.id.user_phone);
        txtUserName = (TextView)itemView.findViewById(R.id.user_name);
        txtUserStatus = (TextView)itemView.findViewById(R.id.user_status);
        imageView = (ImageView)itemView.findViewById(R.id.user_image);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}
