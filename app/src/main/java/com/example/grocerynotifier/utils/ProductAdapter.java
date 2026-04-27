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
    private OnDeleteClickListener deleteListener;
    private OnProductClickListener clickListener;

    public ProductAdapter(List<Product> products,
                          OnDeleteClickListener deleteListener,
                          OnProductClickListener clickListener) {

        this.products = products;
        this.deleteListener = deleteListener;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Product p = products.get(position);

        // Name
        holder.name.setText(p.getName());

        // Days left calculation
        if (p.getExpirationDate() != null) {
            long diff = p.getExpirationDate().getTime() - System.currentTimeMillis();
            long days = diff / (24 * 60 * 60 * 1000);

            if (days < 0) {
                holder.daysLeft.setText("Expired");
                holder.daysLeft.setTextColor(0xFFE53935); // red
            } else {
                holder.daysLeft.setText(days + " days left");
            }
        }

        // DELETE button
        holder.deleteBtn.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(p);
            }
        });

        // CLICK on whole card
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onClick(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

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
