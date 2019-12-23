package com.park.smet_k.bauman_gis.history;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.park.smet_k.bauman_gis.R;
import com.park.smet_k.bauman_gis.model.History;
import com.park.smet_k.bauman_gis.utils.OnItemClickListner;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryRecyclerViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<History> data;
    private final OnItemClickListner<History> onItemClickListener;

    public HistoryAdapter(Context context, OnItemClickListner<History> onItemClickListener) {
        layoutInflater = LayoutInflater.from(context);

        this.data = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public HistoryRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryRecyclerViewHolder(layoutInflater.inflate(R.layout.item_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryRecyclerViewHolder holder, int position) {
        holder.bind(data.get(position), this.onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(History newData) {
        data.add(0, newData);
        notifyItemInserted(0);
    }

    public void setHistories(List<History> historyList) {
        data = historyList;
        notifyDataSetChanged();
    }


    final static class HistoryRecyclerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mBackground;
        private final TextView mTitle;
        private final TextView mPayload;

        HistoryRecyclerViewHolder(View itemView) {
            super(itemView);
            mBackground = itemView.findViewById(R.id.card_background);
            mTitle = itemView.findViewById(R.id.title);
            mPayload = itemView.findViewById(R.id.payload);
        }

        @SuppressLint("SetTextI18n")
        void bind(final History i, OnItemClickListner onItemClickListener) {
            int hash = (i.getFrom().length() + i.getTo().length()) % 8;
            getBackground(hash);

            mTitle.setText("From: " + i.getFrom() + ", To: " + i.getTo());

            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(i));
        }

        private void getBackground(int hash) {
            switch (hash) {
                case 0:
                    mBackground.setImageResource(R.drawable.bg_1);
                    break;
                case 1:
                    mBackground.setImageResource(R.drawable.bg_2);
                    break;
                case 2:
                    mBackground.setImageResource(R.drawable.bg_3);
                    break;
                case 3:
                    mBackground.setImageResource(R.drawable.bg_4);
                    break;
                case 4:
                    mBackground.setImageResource(R.drawable.bg_5);
                    break;
                case 5:
                    mBackground.setImageResource(R.drawable.bg_6);
                    break;
                case 6:
                    mBackground.setImageResource(R.drawable.bg_7);
                    break;
                case 7:
                    mBackground.setImageResource(R.drawable.bg_8);
                    break;
            }
        }
    }
}
