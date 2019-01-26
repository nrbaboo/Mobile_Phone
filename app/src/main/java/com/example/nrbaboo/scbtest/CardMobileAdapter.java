package com.example.nrbaboo.scbtest;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.lang.ref.WeakReference;
import java.util.List;

public class CardMobileAdapter extends RecyclerView.Adapter<CardMobileAdapter.MyViewHolder>{

    private Context mCtx;
    private List<CardMobile> cardList;
    private ListTabActivity listTabActivity;
    private final ClickListener listener;

    public CardMobileAdapter(ListTabActivity listTabActivity,Context mCtx, List<CardMobile> cardList, ClickListener listener) {
        this.mCtx = mCtx;
        this.cardList = cardList;
        this.listTabActivity = listTabActivity;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardview_mobile,null);
        return  new MyViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CardMobile card = cardList.get(position);
        holder.name.setText(card.getName());
        holder.detail.setText(card.getDetail());
        if( card.isFavorite())
            holder.favorite.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.baseline_favorite_black_18dp));
        else
            holder.favorite.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.baseline_favorite_border_black_18dp));

        Glide.with(listTabActivity).load(card.getImageUrl())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher)).into(holder.imageView);


        holder.price.setText("Price: $"+card.getPrice());
        holder.rate.setText("Rating: "+card.getRate());

    }

    public void setFavorite(int position)
    {
        CardMobile card = cardList.get(position);
        card.setFavorite(!card.isFavorite());
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView imageView,favorite;
        TextView name,detail,price,rate;
        CardView cardView;
        private WeakReference<ClickListener> listenerRef;

        public MyViewHolder(View v,ClickListener listener) {
            super(v);
            imageView = itemView.findViewById(R.id.mobile_photo);
            cardView = itemView.findViewById(R.id.cardview_mobile);
            name = itemView.findViewById(R.id.mobile_name);
            detail = itemView.findViewById(R.id.mobile_description);
            price = itemView.findViewById(R.id.mobile_price);
            rate = itemView.findViewById(R.id.mobile_rating);
            favorite = itemView.findViewById(R.id.mobile_favorite);

            listenerRef = new WeakReference<>(listener);
            favorite.setOnClickListener(this);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (v.getId() == favorite.getId()) {
                ListTabActivity.itemID = 1;
            }
            else
            {
                ListTabActivity.itemID = 0;
            }
            listenerRef.get().onPositionClicked(getAdapterPosition());
        }
    }

}
