package com.example.lostandfound;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final Context            context;
    private final List<LostFoundItem> itemList;

    // Constructor receives the context and data list
    public ItemAdapter(Context context, List<LostFoundItem> itemList) {
        this.context  = context;
        this.itemList = itemList;
    }

    // ── ViewHolder: holds references to one row's views ────────────────
    // Created ONCE per visible row — reused as you scroll
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;
        TextView  tvItemName, tvType, tvCategory, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvItemName  = itemView.findViewById(R.id.tvItemName);
            tvType      = itemView.findViewById(R.id.tvType);
            tvCategory  = itemView.findViewById(R.id.tvCategory);
            tvDate      = itemView.findViewById(R.id.tvDate);
        }
    }

    // ── onCreateViewHolder: inflate the row layout ────────────────────
    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    // ── onBindViewHolder: fill data into the row ──────────────────────
    // Called every time a row becomes visible on screen
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LostFoundItem item = itemList.get(position);

        holder.tvItemName.setText(item.getName());
        holder.tvType.setText(item.getType());
        holder.tvCategory.setText(item.getCategory());
        holder.tvDate.setText(item.getTimestamp());

        // Load image using Glide (handles empty URI gracefully)
        if (item.getImageUri() != null && !item.getImageUri().isEmpty()) {
            Glide.with(context)
                    .load(android.net.Uri.parse(item.getImageUri()))
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(holder.ivThumbnail);
        } else {
            holder.ivThumbnail.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // When a row is tapped, open DetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            // Pass item data using Intent extras
            intent.putExtra("id",          item.getId());
            intent.putExtra("type",        item.getType());
            intent.putExtra("name",        item.getName());
            intent.putExtra("phone",       item.getPhone());
            intent.putExtra("description", item.getDescription());
            intent.putExtra("date",        item.getDate());
            intent.putExtra("location",    item.getLocation());
            intent.putExtra("category",    item.getCategory());
            intent.putExtra("imageUri",    item.getImageUri());
            intent.putExtra("timestamp",   item.getTimestamp());

            // ── NEW for Task 9.1P: pass latitude & longitude ──────────
            // DetailActivity / MapActivity can use these to open the map
            intent.putExtra("latitude",  item.getLatitude());
            intent.putExtra("longitude", item.getLongitude());

            context.startActivity(intent);
        });
    }

    // Required: tells RecyclerView how many items exist
    @Override
    public int getItemCount() { return itemList.size(); }
}