package com.selclientapp.selapp.views;

import android.view.View;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.Message;


import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscussionRightViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.message_item_id)
    TextView textMessageItem;


    public DiscussionRightViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithMessage(Message message) {
        this.textMessageItem.setText(message.getMessage());
    }


}
