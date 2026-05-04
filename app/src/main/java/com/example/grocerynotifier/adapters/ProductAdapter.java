package com.example.grocerynotifier.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerynotifier.R;
import com.example.grocerynotifier.model.Product;

import java.util.Calendar;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> products;
    private OnDeleteClickListener deleteListener;
    private OnProductClickListener clickListener;

    // Interfaces
    public interface OnDeleteClickListener {
        void onDelete(Product product);
    }

    public interface OnProductClickListener {
        void onClick(Product product);
    }

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

        // Expiration logic
        if (p.getExpirationDate() != null) {

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            Calendar expiry = Calendar.getInstance();
            expiry.setTime(p.getExpirationDate());
            expiry.set(Calendar.HOUR_OF_DAY, 0);
            expiry.set(Calendar.MINUTE, 0);
            expiry.set(Calendar.SECOND, 0);
            expiry.set(Calendar.MILLISECOND, 0);

            long diff = expiry.getTimeInMillis() - today.getTimeInMillis();
            long days = diff / (24 * 60 * 60 * 1000);

            if (days < 0) {
                holder.daysLeft.setText("Expired ❌");
                holder.daysLeft.setTextColor(0xFFE53935); // red
            } else if (days == 0) {
                holder.daysLeft.setText("Expires today ⚠️");
                holder.daysLeft.setTextColor(0xFFFF9800); // orange
            } else if (days == 1) {
                holder.daysLeft.setText("1 day left");
                holder.daysLeft.setTextColor(0xFFFF9800); // orange
            } else {
                holder.daysLeft.setText(days + " days left");
                holder.daysLeft.setTextColor(0xFF4CAF50); // green
            }
        } else {
            holder.daysLeft.setText("No expiry");
            holder.daysLeft.setTextColor(0xFF757575);
        }

        // DELETE button
        holder.deleteBtn.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(p);
            }
        });

        // CLICK whole item
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

    // ViewHolder
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