package com.example.ken.newsapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ken.newsapp.data.Contract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ItemHolder>{

    //private ArrayList<NewsItem> data;

    private Cursor cursor;
    public ItemClickListener listener;
    private Context context;

    public NewsAdapter(Cursor cursor, ItemClickListener listener){
        this.cursor = cursor;
        this.listener = listener;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
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
        return cursor.getCount();
    }

    public interface ItemClickListener{
        void onItemClick(Cursor cursor, int clickedItemIndex);
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        TextView description;
        TextView time;
        ImageView img;

        public ItemHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.articleTitle);
            description = (TextView) itemView.findViewById(R.id.articleDescription);
            time = (TextView) itemView.findViewById(R.id.articleTime);
            img = (ImageView) itemView.findViewById(R.id.articleImg);

            itemView.setOnClickListener(this);
        }

        public void bind(int position){
            //NewsItem newsItem = data.get(position);
            cursor.moveToPosition(position);

            //title.setText(newsItem.getTitle());
            title.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_TITLE)));
            //description.setText(newsItem.getDescription());
            description.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_DESCRIPTION)));

            //time.setText(newsItem.getPublishedAt());
            time.setText(cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_PUBLISHDATE)));

            String url = cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_URLTOIMAGE));
            Picasso.with(context).load(url).into(img);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            listener.onItemClick(cursor, position);

        }
    }
}
