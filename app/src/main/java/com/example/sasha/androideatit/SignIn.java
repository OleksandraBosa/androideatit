package com.example.sasha.androideatit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sasha.androideatit.Common.Common;
import com.example.sasha.androideatit.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    EditText editPhone,editPassword;
    Button btnSignIn;

    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editPassword = (MaterialEditText) findViewById(R.id.editPassword);
        editPhone = (MaterialEditText) findViewById(R.id.editPhone);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        //Init Firebase
        db = FirebaseDatabase.getInstance("https://androideatit-dce7e.firebaseio.com/");
        users = db.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signInUser(editPhone.getText().toString(), editPassword.getText().toString());
            }
        });
    }

            private void signInUser(String phone, String password) {
                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                final String localPhone = phone;
                final String localPassword = password;

                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(localPhone).exists()){
                            mDialog.dismiss();
                            User user = dataSnapshot.child(localPhone).getValue(User.class);
                            user.setPhone(localPhone);
                                if(user.getPassword().equals(localPassword)){

                                    Intent login = new Intent(SignIn.this,FoodList.class);
                                    Common.currentUser = user;
                                    startActivity(login);
                                    finish();

                                }else{
                                    Toast.makeText(SignIn.this,"Wrong password",Toast.LENGTH_SHORT).show();
                                }

                        }else{
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this,"This user doesn't exist",Toast.LENGTH_SHORT).show();
                            
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
    }
