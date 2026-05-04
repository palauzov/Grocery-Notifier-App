package com.example.grocerynotifier.adapters;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerynotifier.R;
import com.example.grocerynotifier.daos.ShopDao;
import com.example.grocerynotifier.model.ShopItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ViewHolder> {

    private List<ShopItem> items;
    private ShopDao shopDao;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ShopItemAdapter(List<ShopItem> items, ShopDao shopDao) {
        this.items = items;
        this.shopDao = shopDao;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ShopItem item = items.get(position);

        holder.tvName.setText(item.name);

        holder.etQuantity.setText(String.valueOf(item.quantity));

        // avoid recycling bugs
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(item.isChecked);

        // strike-through
        if (item.isChecked) {
            holder.tvName.setPaintFlags(
                    holder.tvName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
        } else {
            holder.tvName.setPaintFlags(
                    holder.tvName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)
            );
        }

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            item.isChecked = isChecked;

            if (isChecked) {
                holder.tvName.setPaintFlags(
                        holder.tvName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
                );
            } else {
                holder.tvName.setPaintFlags(
                        holder.tvName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)
                );
            }

            executorService.execute(() -> {

                shopDao.updateItem(item);

                // check if all items are checked
                List<ShopItem> allItems = shopDao.getItems(item.listId);

                boolean allChecked = true;
                for (ShopItem i : allItems) {
                    if (!i.isChecked) {
                        allChecked = false;
                        break;
                    }
                }

                // delete list if complete
                if (allChecked) {
                    shopDao.deleteList(item.listId);

                    ((Activity) holder.itemView.getContext()).runOnUiThread(() -> {
                        Toast.makeText(holder.itemView.getContext(),
                                "Shopping list completed 🎉",
                                Toast.LENGTH_SHORT).show();

                        items.clear();
                        notifyDataSetChanged();
                    });
                }
            });
        });

        holder.etQuantity.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {

                String text = holder.etQuantity.getText().toString().trim();

                int qty = 1;
                try {
                    qty = Integer.parseInt(text);
                } catch (Exception ignored) {}

                item.quantity = qty;

                executorService.execute(() -> {
                    shopDao.updateItem(item);
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView tvName;
        EditText etQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkItem);
            tvName = itemView.findViewById(R.id.tvItemName);
            etQuantity = itemView.findViewById(R.id.etQuantity);
        }
    }
}