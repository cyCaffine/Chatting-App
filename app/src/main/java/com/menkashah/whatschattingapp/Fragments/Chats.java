package com.menkashah.whatschattingapp.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.menkashah.whatschattingapp.Adapters.UserAdapter;
import com.menkashah.whatschattingapp.Model.Users;

import com.menkashah.whatschattingapp.R;
import com.menkashah.whatschattingapp.databinding.FragmentChatsBinding;

import java.util.ArrayList;


public class Chats extends Fragment {



    public Chats() {

    }
    FragmentChatsBinding binding;
    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();

//        write your code here
        UserAdapter adapter = new UserAdapter(list, getContext());
        binding.chatrv.setAdapter(adapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatrv.setLayoutManager(layoutManager);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users user = dataSnapshot.getValue(Users.class);
                    assert user != null;
                    user.setUserId(dataSnapshot.getKey()); // Assuming setUserId is the setter method for user ID

//                    jo user login h vo list me show nhi hoga
                    if(!user.getUserId().equals(FirebaseAuth.getInstance().getUid()))
                    list.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Failed to read value.", error.toException());
            }

        });



        
        return  binding.getRoot();
    }
}