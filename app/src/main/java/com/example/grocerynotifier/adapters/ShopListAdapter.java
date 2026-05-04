package com.example.grocerynotifier.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerynotifier.R;
import com.example.grocerynotifier.model.ShopList;

import java.util.List;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.VH> {

    public interface OnClick {
        void onClick(ShopList list);
    }

    private List<ShopList> list;
    private OnClick listener;

    public ShopListAdapter(List<ShopList> list, OnClick listener) {
        this.list = list;
        this.listener = listener;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView name;

        public VH(View v) {
            super(v);
            name = v.findViewById(R.id.tvListName);
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH h, int pos) {
        ShopList l = list.get(pos);
        h.name.setText("List: " + l.id);

        h.itemView.setOnClickListener(v -> listener.onClick(l));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
