package com.selclientapp.selapp.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.Message;
import com.selclientapp.selapp.repositories.ManagementTokenAndUSer;

import java.util.List;

public class DiscussionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MSG_RIGHT_TYPE = 0;
    private static final int MSG_LEFT_TYPE = 1;

    public List<Message> messages;
    ManagementTokenAndUSer managementTokenAndUSer = new ManagementTokenAndUSer();

    public DiscussionAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType == MSG_LEFT_TYPE){
            View view = inflater.inflate(R.layout.chat_item_left, parent, false);
            return new DiscussionLeftViewHolder(view);
        }else{
            View view = inflater.inflate(R.layout.chat_item_right, parent, false);
            return new DiscussionRightViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == MSG_LEFT_TYPE){
            DiscussionLeftViewHolder viewHolder = (DiscussionLeftViewHolder)holder;
            viewHolder.updateWithMessage(this.messages.get(position));
        }else{
            DiscussionRightViewHolder viewHolder = (DiscussionRightViewHolder)holder;
            viewHolder.updateWithMessage(this.messages.get(position));
        }



    }

    @Override
    public int getItemCount() {
        return this.messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = this.messages.get(position);
        int user_id = managementTokenAndUSer.getCurrentUser().getId();
        if (user_id == message.getSenderId()) {
            return MSG_RIGHT_TYPE;
        } else {
            return MSG_LEFT_TYPE;
        }
    }
}
