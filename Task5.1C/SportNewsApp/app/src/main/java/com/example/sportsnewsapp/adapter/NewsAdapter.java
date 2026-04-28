package com.example.sportsnewsapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsnewsapp.R;
import com.example.sportsnewsapp.model.NewsItem;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.VH> {

    public interface OnItemClickListener {
        void onItemClick(NewsItem item);
    }

    private List<NewsItem>            items;
    private final OnItemClickListener listener;

    public NewsAdapter(List<NewsItem> items, OnItemClickListener listener) {
        this.items    = items;
        this.listener = listener;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        NewsItem item = items.get(pos);
        h.image.setImageResource(item.getImageResId());
        h.title.setText(item.getTitle());
        h.category.setText(item.getCategory());
        h.desc.setText(item.getDescription());
        h.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() { return items.size(); }

    public void updateList(List<NewsItem> newList) {
        this.items = newList;
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView image;
        TextView  title, category, desc;

        VH(View v) {
            super(v);
            image    = v.findViewById(R.id.imgNews);
            title    = v.findViewById(R.id.tvNewsTitle);
            category = v.findViewById(R.id.tvNewsCategory);
            desc     = v.findViewById(R.id.tvNewsDesc);
        }
    }
}