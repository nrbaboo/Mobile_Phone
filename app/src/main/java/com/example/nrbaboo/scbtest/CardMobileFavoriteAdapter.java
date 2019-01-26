package com.example.nrbaboo.scbtest;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.lang.ref.WeakReference;
import java.util.List;

public class CardMobileFavoriteAdapter extends RecyclerView.Adapter<CardMobileFavoriteAdapter.MyViewHolder>{

    private Context mCtx;
    private List<CardMobileFavorite> cardList;
    private FavoriteTabActivity fm;
    private final ClickListener listener;

    public CardMobileFavoriteAdapter(FavoriteTabActivity fm,Context mCtx, List<CardMobileFavorite> cardList, ClickListener listener) {
        this.mCtx = mCtx;
        this.cardList = cardList;
        this.listener = listener;
        this.fm = fm;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardview_favorite_mobile,null);
        return  new MyViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CardMobileFavorite card = cardList.get(position);
        holder.name.setText(card.getName());
        holder.price.setText("Price: $"+card.getPrice());
        holder.rate.setText("Rating: "+card.getRate());

        Glide.with(fm).load(card.getImageUrl())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher)).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView imageView;
        TextView name,price,rate;
        CardView cardView;

        private WeakReference<ClickListener> listenerRef;

        public MyViewHolder(View v,ClickListener listener) {
            super(v);
            imageView = itemView.findViewById(R.id.mobile_photo);
            cardView = itemView.findViewById(R.id.cardview_mobile_favorite);
            name = itemView.findViewById(R.id.mobile_name);
            price = itemView.findViewById(R.id.mobile_price);
            rate = itemView.findViewById(R.id.mobile_rating);

            listenerRef = new WeakReference<>(listener);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            listenerRef.get().onPositionClicked(getAdapterPosition());
        }
    }

}
