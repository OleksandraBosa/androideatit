package com.example.sasha.androideatit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.sasha.androideatit.Interface.ItemClickListener;
import com.example.sasha.androideatit.Model.Category;
import com.example.sasha.androideatit.Model.Food;
import com.example.sasha.androideatit.Model.Request;
import com.example.sasha.androideatit.Model.User;
import com.example.sasha.androideatit.ViewHolder.FoodViewHolder;
import com.example.sasha.androideatit.ViewHolder.MenuViewHolder;
import com.example.sasha.androideatit.ViewHolder.UsersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Users extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference users;

    String status;

    FirebaseRecyclerAdapter<User,UsersViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance("https://androideatit-dce7e.firebaseio.com");
        users = firebaseDatabase.getReference("User");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_users);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


            loadUsers();
    }


    private void loadUsers() {

            adapter = new FirebaseRecyclerAdapter<User, UsersViewHolder>(User.class,
                    R.layout.users_item,
                    UsersViewHolder.class,
                    users
            ) {
                @Override
                protected void populateViewHolder(UsersViewHolder viewHolder, User model, int position) {
                    viewHolder.txtUserName.setText(model.getName());
                    viewHolder.txtUserPhone.setText(model.getPhone());

                    if(model.getIsStaff().equals("true")){
                        status = "Staff";
                    }
                    else if(model.getIsStaff().equals("false")){
                        status = "Customer";
                    }else{
                        status = "Undefined";
                    }


                    viewHolder.txtUserStatus.setText(status);



                    final User local = model;
                    viewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            //Start new Activity
                            Intent userDetail = new Intent(Users.this,UserDetail.class);
                            userDetail.putExtra("phone",adapter.getRef(position).getKey());
                            startActivity(userDetail);
                        }
                    });
                }
            };

            recyclerView.setAdapter(adapter);
        }
}
