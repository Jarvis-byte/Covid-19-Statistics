package com.example.covid_19.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.covid_19.ModelClass.NewsItems;
import com.example.covid_19.R;
import com.example.covid_19.WebViewActivity;

import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {
    Context mContext;

    private List<NewsItems> items;

    public CardStackAdapter(Context mContext, List<NewsItems> items) {
        this.mContext = mContext;
        this.items = items;
    }
//    public CardStackAdapter(List<NewsItems> items) {
//        this.items = items;
//    }

    @NonNull
    @Override
    public CardStackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardStackAdapter.ViewHolder holder, final int position) {

        holder.setData(items.get(position));
        holder.parentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("EXTRA_SESSION_ID", items.get(position).getLink());
                mContext.startActivity(intent);


            }


        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<NewsItems> getItems() {
        return items;
    }

    public void setItems(List<NewsItems> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, content, source;
        LinearLayout parentCard;
        ImageView image_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            parentCard = itemView.findViewById(R.id.parentCard);
            image_view = itemView.findViewById(R.id.image_view);
            source = itemView.findViewById(R.id.source);
        }

        void setData(NewsItems data) {
            Log.i("ImageRV", data.getUrlToImage());

            Glide.with(mContext)
                    .load(data.getUrlToImage())
                    .placeholder(R.drawable.newspaper)
                    .dontAnimate()
                    .into(image_view);

            title.setText(data.getTitle());
            content.setText(data.getContent());
            source.setText(data.getReference());
        }

    }
}
