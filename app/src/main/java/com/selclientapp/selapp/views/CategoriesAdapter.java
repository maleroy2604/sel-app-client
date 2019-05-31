package com.selclientapp.selapp.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.recyclerview.widget.RecyclerView;

import com.selclientapp.selapp.R;
import com.selclientapp.selapp.model.Category;


import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesViewHolder> {

    public interface Listener {
        void deleteCategory(int position);
    }


    private List<Category> categories;
    private final Listener callback;

    public CategoriesAdapter(List<Category> categories, Listener callback) {
        this.callback = callback;
        this.categories = categories;
    }

    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_my_categories_item, parent, false);

        return new CategoriesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(CategoriesViewHolder holder, int position) {
        holder.updateWithcategory(this.categories.get(position), this.callback);
    }

    @Override
    public int getItemCount() {
        return this.categories.size();
    }

    public Category getCategory (int position) {
        return this.categories.get(position);
    }

}
