package com.selclientapp.selapp.views;

import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.selclientapp.selapp.App;
import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.Message;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscussionLeftViewHolder extends RecyclerView.ViewHolder {

    @BindView((R.id.image_view_profile))
    ImageView imageViewprofile;


    @BindView(R.id.message_item_id)
    TextView textMessageItem;


    public DiscussionLeftViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithMessage(Message message) {
        if (message.getAvatarUrl() == null) {
            imageViewprofile.setImageResource(R.drawable.sel);
        } else {
            Glide.with(App.context).load(App.URL_SERVER + "imageavatar/" + message.getAvatarUrl()).into(imageViewprofile);
        }


        this.textMessageItem.setText(message.getMessage());
    }


}