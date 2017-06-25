package com.example.ken.newsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ItemHolder>{

    private ArrayList<NewsItem> data;
    public ItemClickListener listener;

    public NewsAdapter(ArrayList<NewsItem> data, ItemClickListener listener){
        this.data = data;
        this.listener = listener;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item, parent, false);
        ItemHolder itemHolder = new ItemHolder(view);

        return itemHolder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface ItemClickListener{
        void onItemClick(int clickedItemIndex);
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        TextView description;
        TextView time;

        public ItemHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.articleTitle);
            description = (TextView) itemView.findViewById(R.id.articleDescription);
            time = (TextView) itemView.findViewById(R.id.articleTime);

            itemView.setOnClickListener(this);
        }

        public void bind(int position){
            NewsItem newsItem = data.get(position);

            title.setText(newsItem.getTitle());
            description.setText(newsItem.getDescription());
            time.setText(newsItem.getPublishedAt());
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            listener.onItemClick(position);

        }
    }
}
