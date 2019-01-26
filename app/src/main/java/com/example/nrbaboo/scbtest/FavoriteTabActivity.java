package com.example.nrbaboo.scbtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class FavoriteTabActivity extends Fragment{

    RecyclerView recyclerView;
    CardMobileFavoriteAdapter cardViewAdapter;
    List<CardMobileFavorite> mobileList;

    Context mContext;
    JSONArray mobileData;

    ArrayList<Integer> favoriteIDList = new ArrayList<Integer>();
    private DBHelper mHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_tab, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        mContext = view.getContext();
        recyclerView.setHasTransientState(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mHelper = new DBHelper(mContext);
        favoriteIDList = mHelper.getFavoritedList();
        setCardView();
        /*Toast.makeText(mContext, "Favorite: " +
                mHelper.getFavoritedList()+"", Toast.LENGTH_SHORT).show();*/
        if(mobileData!=null)
        {
            //first sort
            switch ( ((MainActivity)getActivity()).fmMobileList.sortSelected)
            {
                case 0: {
                    ((MainActivity)getActivity()).fmMobileList.sortLowToHigh();
                    break;
                }
                case 1: {
                    ((MainActivity)getActivity()).fmMobileList.sortHighToLow();
                    break;
                }
                case 2: {
                    ((MainActivity)getActivity()).fmMobileList.sortRating();
                    break;
                }
                default:break;
            }
        }
        return view;
    }
    public void setCardView()
    {
        if(mobileData == null)return;

        mobileList = new ArrayList<>();
        int index=-1,id;
        try {
            for (int i = 0; i < favoriteIDList.size(); i++) {

                for (int j = 0; j < mobileData.length(); j++) {

                    id = mobileData.getJSONObject(j).getInt("id");
                    if (id == favoriteIDList.get(i)) {
                        index = j;
                    }

                }
                mobileList.add(new CardMobileFavorite(
                        favoriteIDList.get(i),
                        mobileData.getJSONObject(index).getString("name"),
                        mobileData.getJSONObject(index).getString("thumbImageURL"),
                        mobileData.getJSONObject(index).getDouble("price"),
                        mobileData.getJSONObject(index).getDouble("rating"),
                        true,
                        mobileData.getJSONObject(index).getString("description"),
                        mobileData.getJSONObject(index).getString("brand")
                ));
            }
            cardViewAdapter = new CardMobileFavoriteAdapter(FavoriteTabActivity.this,mContext, mobileList, new ClickListener() {
                @Override
                public void onPositionClicked(int position) {
                    openMobileDetail(mobileList.get(position));
                }

                @Override
                public void onLongClicked(int position) {

                }
            });

            recyclerView.setAdapter(cardViewAdapter);
        }
        catch (Exception e) { }
        swipeToDelete();

    }
    public void swipeToDelete()
    {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //del from SQlite
                mHelper.delFavorite(mobileList.get(viewHolder.getAdapterPosition()).getId());

                mobileList.remove(viewHolder.getAdapterPosition());
                cardViewAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView);
    }
    public void setMobileData(JSONArray data)
    {
        mobileData = data;
    }
    public void openMobileDetail(CardMobileFavorite selectedMobile)
    {
        Intent intent = new Intent(mContext, MobileDetailActivity.class); // Test show mobile detail

        intent.putExtra("id",String.valueOf(selectedMobile.getId()));
        intent.putExtra("name",selectedMobile.getName());
        intent.putExtra("brand",selectedMobile.getBrand());
        intent.putExtra("price",String.valueOf(selectedMobile.getPrice()));
        intent.putExtra("rate",String.valueOf(selectedMobile.getRate()));
        intent.putExtra("detail",selectedMobile.getDetail());
        intent.putExtra("url",selectedMobile.getImageUrl());

        mContext.startActivity(intent);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }
}
