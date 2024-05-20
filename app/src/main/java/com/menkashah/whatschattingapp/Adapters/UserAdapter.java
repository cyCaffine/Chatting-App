package com.menkashah.whatschattingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.menkashah.whatschattingapp.Model.Users;
import com.menkashah.whatschattingapp.R;
import com.menkashah.whatschattingapp.chatDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends  RecyclerView.Adapter<UserAdapter.viewHolder> {


    ArrayList<Users> list;
    Context context;

    public UserAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.sample_shows_user,parent,false);
        return new viewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Users usermod = list.get(position);
        if (usermod != null) {
            Picasso.get().load(usermod.getProfilePic())
                    .placeholder(R.drawable.puma)

                    .into(holder.image);
            holder.userName.setText(usermod.getUserName());

            FirebaseDatabase.getInstance().getReference().child("chats")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid() + usermod.getUserId())
                    .orderByChild("timestamps")
                    .limitToLast(1)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Handle the retrieved data here
                            if(snapshot.hasChildren()){
                                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                    holder.lastMessage.setText(snapshot1.child("message").getValue().toString());

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle any errors here
                        }
                    });


//send data from one activity to another activity
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, chatDetailActivity.class);
                    i.putExtra("userId", usermod.getUserId());
                    i.putExtra("profilePic", usermod.getProfilePic());
                    i.putExtra("userName", usermod.getUserName());
                    context.startActivity(i);
                }
            });

        }
    }


    @Override
    public int getItemCount() {

        return  list.size();
    }


    class viewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView userName, lastMessage;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            image = (itemView).findViewById(R.id.profile_image);
            userName = (itemView).findViewById(R.id.user_name);
            lastMessage = (itemView).findViewById(R.id.last_message);
        }
    }
}
