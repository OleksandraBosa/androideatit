package com.example.sasha.androideatit;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sasha.androideatit.Common.Common;
import com.example.sasha.androideatit.Database.Database;
import com.example.sasha.androideatit.Interface.ItemClickListener;
import com.example.sasha.androideatit.Model.Order;
import com.example.sasha.androideatit.Model.Request;
import com.example.sasha.androideatit.Model.User;
import com.example.sasha.androideatit.ViewHolder.OrderViewHolder;
import com.example.sasha.androideatit.ViewHolder.UserOrdersViewHolder;
import com.example.sasha.androideatit.ViewHolder.UsersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetail extends AppCompatActivity  {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request,UserOrdersViewHolder> adapter;

    TextView txtUserName, txtUserPhone, txtUserStatus;

    Button homeBtn, saveInfo;

    FirebaseDatabase database;
    DatabaseReference requests,users;

    Switch switchButton;

    String phone="";

    User currentUser;

    boolean hasBeenChanged = false;
    boolean statusChanged = false;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        //Firebase
        database = FirebaseDatabase.getInstance("https://androideatit-dce7e.firebaseio.com");
        requests = database.getReference("Request");
        users = database.getReference("User");

        txtUserName = (TextView)findViewById(R.id.user_name);
        txtUserPhone = (TextView)findViewById(R.id.user_phone);
        txtUserStatus = (TextView)findViewById(R.id.user_status);

        switchButton = (Switch) findViewById(R.id.switchButton);


        homeBtn = (Button)findViewById(R.id.returnHome);
        saveInfo = (Button)findViewById(R.id.saveInfo);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(UserDetail.this,Home.class);
                startActivity(homeIntent);
            }
        });




        recyclerView = (RecyclerView)findViewById(R.id.userOrders);
        recyclerView.setNestedScrollingEnabled(true);
       // recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference detailedUser = database.getReference("User").child(phone);

                if(statusChanged){
                    statusChanged = false;
                }else {
                    if (switchButton.isChecked()) {
                        detailedUser.child("IsStaff").setValue("true");
                        txtUserStatus.setText("Staff");

                    } else {
                        detailedUser.child("IsStaff").setValue("false");
                        txtUserStatus.setText("Customer");
                    }

                }
                Intent saveIntent = new Intent(UserDetail.this, Users.class);
                saveIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(saveIntent);
            }
        });

/*
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference detailedUser = database.getReference("User").child(phone);
                if(isChecked){
                    txtUserStatus.setText("Staff");
                }
                else{
                    txtUserStatus.setText("Customer");
                }
                }
        });


*/

        //Get Intent here
        if(getIntent() != null)
            phone = getIntent().getStringExtra("phone");
        if(!phone.isEmpty() &&phone !=null)
        {
            loadUserById(phone);
            loadOrders(phone);
        }

    }



    private void loadUserById(String id){
        users.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);

                txtUserName.setText(currentUser.getName());
                txtUserPhone.setText(currentUser.getPhone());


                if(currentUser.getIsStaff().equals("true")){
                    switchButton.setChecked(true);
                    txtUserStatus.setText("Staff");
                }else {
                    switchButton.setChecked(false);
                    txtUserStatus.setText("Customer");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadOrders(String phone) {

        adapter = new FirebaseRecyclerAdapter<Request, UserOrdersViewHolder>(
                Request.class,
                R.layout.user_orders,
                UserOrdersViewHolder.class,
                requests.orderByChild("phone")
                        .equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(final UserOrdersViewHolder viewHolder, final Request model, int position) {

                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderTotal.setText(model.getTotal());
                viewHolder.txtOrderAddress.setText(model.getAddress());
                ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getBaseContext(),R.array.status_array,android.R.layout.simple_spinner_item);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                viewHolder.statusSpinner.setAdapter(arrayAdapter);
                viewHolder.statusSpinner.setSelection(getPosition(model.getStatus()));

                    viewHolder.statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (hasBeenChanged) {
                                hasBeenChanged = false;
                            } else {
                                String text = parent.getItemAtPosition(position).toString();
                                hasBeenChanged = true;
                                if (text.equals("Placed")) {
                                    adapter.getItem(position).setStatus("0");
                                    Toast.makeText(getBaseContext(),text,Toast.LENGTH_SHORT).show();
                                } else if (text.equals("On the way")) {
                                    adapter.getItem(position).setStatus("1");
                                } else {
                                    adapter.getItem(position).setStatus("2");
                                }

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }


        };

        recyclerView.setAdapter(adapter);
    }

    private int getPosition(String status){
        if(status.equals("0")){
            return 0;
        }else if(status.equals("1")){
            return 1;
        }else if(status.equals("2")){
            return 2;
        }
        return 0;
    }
}
