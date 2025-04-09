package com.jaixlabs.pricestracker.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


import com.jaixlabs.pricestracker.R;
import com.jaixlabs.pricestracker.model.Price;

import java.util.ArrayList;
import java.util.List;

public class PriceAdapter extends ListAdapter<Price, PriceAdapter.ViewHolder> {

    public PriceAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Price> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Price>() {
                @Override
                public boolean areItemsTheSame(@NonNull Price oldItem, @NonNull Price newItem) {
                    return oldItem.getId() == newItem.getId(); // int comparison
                }

                @Override
                public boolean areContentsTheSame(@NonNull Price oldItem, @NonNull Price newItem) {
                    return oldItem.getName().equals(newItem.getName()) &&
                            oldItem.getPrice() == newItem.getPrice() &&
                            oldItem.getChange() == newItem.getChange();
                }
            };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtChange;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtChange = itemView.findViewById(R.id.txtChange);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_price, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Price t = getItem(position);
        holder.txtName.setText(t.getName());
        holder.txtPrice.setText("₹" + String.format("%.2f", t.getPrice()));
        holder.txtChange.setText(String.format("%.2f%%", t.getChange()));
    }

    public void setPriceList(List<Price> newList) {
        submitList(new ArrayList<>(newList)); // defensive copy
    }
}
