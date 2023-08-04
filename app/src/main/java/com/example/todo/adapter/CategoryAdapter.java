package com.example.todo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;
import com.example.todo.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private List<Category> allCategories = new ArrayList<>();

    private OnItemClickListener listener;

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false);
        return new CategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Category currentCategory = allCategories.get(position);
        holder.categoryItem.setText(currentCategory.getCategory());
    }

    @Override
    public int getItemCount() {
        return allCategories.size();
    }

    public Category getCategoryAt(int position) {
        return allCategories.get(position);
    }

    public void setCategories(List<Category> categories) {
        this.allCategories = categories;
        notifyDataSetChanged();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        private TextView categoryItem;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);

            categoryItem = itemView.findViewById(R.id.category_item_layout_tv_category);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(allCategories.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Category category);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
