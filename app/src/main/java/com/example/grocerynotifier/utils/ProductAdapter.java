package com.example.grocerynotifier.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerynotifier.R;

import java.util.List;
import com.example.grocerynotifier.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> products;
    private OnDeleteClickListener listener;

    public interface OnDeleteClickListener { void onDelete(Product product); }

    public ProductAdapter(List<Product> products, OnDeleteClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product p = products.get(position);
        holder.name.setText(p.getName());

        // Calculate Days Left
        long diff = p.getExpirationDate().getTime() - System.currentTimeMillis();
        long days = diff / (24 * 60 * 60 * 1000);
        holder.daysLeft.setText(days + " days left");

        holder.deleteBtn.setOnClickListener(v -> listener.onDelete(p));
    }

    @Override
    public int getItemCount() { return products.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, daysLeft;
        Button deleteBtn;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvProductName);
            daysLeft = itemView.findViewById(R.id.tvDaysLeft);
            deleteBtn = itemView.findViewById(R.id.btnDelete);
        }
    }
}
