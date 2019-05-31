package com.selclientapp.selapp.views;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.Category;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_my_categories_category)
    TextView textCategory;
    @BindView(R.id.fragment_my_categories_delete)
    ImageButton imageButtonDelete;


    private WeakReference<CategoriesAdapter.Listener> callbackWeakRef;

    public CategoriesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithcategory(Category category, CategoriesAdapter.Listener callback) {
        this.textCategory.setText(category.getCategory());
        this.callbackWeakRef = new WeakReference<>(callback);
        configImgDelete();
    }


    private void configImgDelete() {
        imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoriesAdapter.Listener callback = callbackWeakRef.get();
                if (callback != null) {
                    callback.deleteCategory(getAdapterPosition());
                }
            }
        });
    }

}

