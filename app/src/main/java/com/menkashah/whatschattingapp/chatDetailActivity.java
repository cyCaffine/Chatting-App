package com.menkashah.whatschattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.menkashah.whatschattingapp.Adapters.ChatAdapter;
import com.menkashah.whatschattingapp.Model.MessageModel;
import com.menkashah.whatschattingapp.databinding.ActivityChatDetailBinding;
import com.menkashah.whatschattingapp.databinding.FragmentChatsBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class chatDetailActivity extends AppCompatActivity {


    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

//        get sender id and receiver id from forebase
        String senderId =  auth.getUid();
        String receiverId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String userProfilePic = getIntent().getStringExtra("profilePic");


        binding.chatTitle.setText(userName);
        Picasso.get().load(userProfilePic).placeholder(R.drawable.puma).into(binding.chatProfileImage);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chatDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messageModels,this, receiverId);
        binding.chatDetailRv.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatDetailRv.setLayoutManager(layoutManager);


        final  String senderRoom =  senderId+receiverId;
        final  String receiverRoom = receiverId+senderId;


        database.getReference().child("chats")
                        .child(senderRoom)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        messageModels.clear();
                                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                            MessageModel model = snapshot1.getValue(MessageModel.class);
                                            model.setMessageId(snapshot1.getKey());
                                            messageModels.add(model);
                                        }
                                        chatAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

   binding.sendImage.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {

           String message =  binding.editText.getText().toString();
           final MessageModel model = new MessageModel(senderId, message);
           model.setTimestamp(new Date().getTime());
           binding.editText.setText("");

           database.getReference().child("chats")
                   .child(senderRoom).push()
                   .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void unused) {
            database.getReference().child("chats").child(receiverRoom).push()
                    .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                       }
                   });

       }
   });

       





    }
}