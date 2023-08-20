package com.menkashah.whatschattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.menkashah.whatschattingapp.Model.user;
import com.menkashah.whatschattingapp.databinding.ActivitySignUpBinding;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    private ActivitySignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog  = new ProgressDialog(SignUp.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                auth.createUserWithEmailAndPassword(
                        binding.editTTextEmailAddress.getText().toString(),
                                binding.editTextNumberPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();

                         if(task.isSuccessful()){
                             user user = new user(binding.editTextTextPersonName.getText().toString(),
                                     binding.editTTextEmailAddress.getText().toString(),
                                     binding.editTextNumberPassword.getText().toString());

//                             find id with firebase
                             String id = task.getResult().getUser().getUid();
                             database.getReference().child("users").child(id).setValue(user);

                             Toast.makeText(SignUp.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                             
                         }else{
                             Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                         }
                                
                                
                            }
                        });

            }
        });
        binding.alredayhaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUp.this,SignIn.class);
                startActivity(i);
            }
        });


    }
}