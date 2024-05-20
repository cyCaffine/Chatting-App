package com.menkashah.whatschattingapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.menkashah.whatschattingapp.Model.MessageModel;
import com.menkashah.whatschattingapp.R;

import java.util.ArrayList;

public class ChatAdapter extends  RecyclerView.Adapter {

    ArrayList<MessageModel> messageModels;
    Context context;
    String recId;

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String recId) {
        this.messageModels = messageModels;
        this.context = context;
        this.recId = recId;
    }

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return  new senderViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
        return  new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = messageModels.get(position);


//        Alert dialog for delete message
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete ")
                        .setMessage("Are you sure want to delete this message")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid()+ recId;
                                database.getReference().child("chats").child(senderRoom)
                                        .child(messageModel.getMessageId())
                                        .setValue(null);

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                   dialog.dismiss();
                            }
                        }).show();
                return false;
            }
        });
        if(holder.getClass() == senderViewHolder.class){
            ((senderViewHolder)holder).sendMsg.setText(messageModel.getMessage());
        }
        else{
            ((RecyclerViewHolder)holder).receiverMsg.setText(messageModel.getMessage());
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(messageModels.get(position).getUid().equals(FirebaseAuth.getInstance().getUid())) {
            return SENDER_VIEW_TYPE;
        }
        else{
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class RecyclerViewHolder extends  RecyclerView.ViewHolder {
        TextView receiverMsg, receiverTime;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMsg = (itemView).findViewById(R.id.receiverMsg);
            receiverTime = (itemView).findViewById(R.id.receiverTime);
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {
        TextView sendMsg, sentTime;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);

            sendMsg = (itemView).findViewById(R.id.sendMsg);
            sentTime = (itemView).findViewById(R.id.sendTime);
        }
    }
}
