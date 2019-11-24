package com.park.smet_k.bauman_gis.M_news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.park.smet_k.bauman_gis.R;
import com.park.smet_k.bauman_gis.model.News;
import com.park.smet_k.bauman_gis.utils.OnItemClickListner;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsRecyclerViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<News> data;
    private final OnItemClickListner<News> onItemClickListener;


    public NewsAdapter(Context context, OnItemClickListner<News> onItemClickListener) {
        layoutInflater = LayoutInflater.from(context);

        this.data = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public NewsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsRecyclerViewHolder(layoutInflater.inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsRecyclerViewHolder holder, int position) {
        holder.bind(data.get(position), this.onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(News newData) {
        data.add(0, newData);
        notifyItemInserted(0);
    }

    public void setNews(List<News> newsList) {
        data = newsList;
        notifyDataSetChanged();
    }

    final static class NewsRecyclerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mBackground;
        private final TextView mTitle;
        private final TextView mTime;
        private final TextView mPayload;

        NewsRecyclerViewHolder(View itemView) {
            super(itemView);
            mBackground = itemView.findViewById(R.id.card_background);
            mTitle = itemView.findViewById(R.id.title);
            mTime = itemView.findViewById(R.id.time);
            mPayload = itemView.findViewById(R.id.payload);
        }

        void bind(final News i, OnItemClickListner onItemClickListener) {
            mBackground.setImageResource(R.drawable.avatar);
            mTitle.setText(i.getTitle());
//            mTime.setText(i.getTime().toString());
            mPayload.setText(i.getPayload());

            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(i));
        }

    }
}
