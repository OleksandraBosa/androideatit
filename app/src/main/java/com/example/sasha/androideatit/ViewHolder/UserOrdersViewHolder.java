package com.example.sasha.androideatit.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sasha.androideatit.Interface.ItemClickListener;
import com.example.sasha.androideatit.R;
import com.example.sasha.androideatit.UserDetail;

/**
 * Created by sasha on 06.04.18.
 */

public class UserOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

public TextView txtOrderId, txtOrderStatus, txtOrderAddress, txtOrderTotal,txtStatus;

public Spinner statusSpinner;


private ItemClickListener itemClickListener;

public UserOrdersViewHolder(View itemView) {
        super(itemView);

        txtOrderAddress = (TextView)itemView.findViewById(R.id.order_address);
        txtOrderId = (TextView)itemView.findViewById(R.id.order_id);
        txtOrderTotal = (TextView)itemView.findViewById(R.id.order_total);
        //txtStatus = (TextView) itemView.findViewById(R.id.orderStatus);

        statusSpinner = (Spinner)itemView.findViewById(R.id.order_status);


        itemView.setOnClickListener(this);
        }

public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        }

@Override
public void onClick(View view){
        itemClickListener.onClick(view,getAdapterPosition(),false);
        }

}
