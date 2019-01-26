package com.example.nrbaboo.scbtest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ListTabActivity extends Fragment {

    RecyclerView recyclerView;
    CardMobileAdapter cardViewAdapter;
    List<CardMobile> mobileList;

    Context mContext;

    ImageView btnSort;
    String[] sortList ={"Price low to high",
                        "Price high to low",
                        "Rating 5-1" };
    int sortSelected = 0;

    static int itemID=0;

    private DBHelper mHelper;
    FavoriteMobile favorite = new FavoriteMobile();

    ArrayList<Integer> favoriteIDList = new ArrayList<Integer>();

    JSONArray mobileData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_tab, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        mContext = view.getContext();
        recyclerView.setHasTransientState(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mobileList = new ArrayList<>();
        mHelper = new DBHelper(mContext);
        favoriteIDList = mHelper.getFavoritedList();

        setCardView();

        btnSort = (ImageView)((MainActivity)getActivity()).findViewById(R.id.btn_sort);
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortDialog();
            }
        });
        if(mobileData!=null) {
            switch (((MainActivity) getActivity()).fmMobileList.sortSelected) {
                case 0: {
                    ((MainActivity) getActivity()).fmMobileList.sortLowToHigh();
                    break;
                }
                case 1: {
                    ((MainActivity) getActivity()).fmMobileList.sortHighToLow();
                    break;
                }
                case 2: {
                    ((MainActivity) getActivity()).fmMobileList.sortRating();
                    break;
                }
                default:
                    break;
            }
        }
        return view;
    }
    public void showSortDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setSingleChoiceItems(sortList, sortSelected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sortSelected = which;
                /*Toast.makeText(mContext, "Select " +
                       sortSelected, Toast.LENGTH_SHORT).show();*/
                switch (which)
                {
                    case 0: {
                        sortLowToHigh();
                        break;
                    }
                    case 1: {
                        sortHighToLow();
                        break;
                    }
                    case 2: {
                        sortRating();
                        break;
                    }
                    default:break;
                }
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    public void setMobileData(JSONArray data)
    {
        mobileData = data;
    }
    public void setCardView() {

        if(mobileData==null)return;
        int mobileCount = mobileData.length();

        Log.w("7078","chk Length: "+mobileCount+"");
        int indexFavorite = 0;
        boolean favoriteChk;

        try{
            for (int i = 0; i < mobileCount; i++) {
                favoriteChk = false;
                for (int j = indexFavorite; j < favoriteIDList.size(); j++) {
                    if (favoriteIDList.get(j) == i + 1) {
                        favoriteChk = true;
                        indexFavorite++;
                        break;
                    }
                }
                 mobileList.add(new CardMobile(
                        mobileData.getJSONObject(i).getInt("id"),
                        mobileData.getJSONObject(i).getString("name"),
                        mobileData.getJSONObject(i).getString("description"),
                        mobileData.getJSONObject(i).getString("thumbImageURL"),
                        mobileData.getJSONObject(i).getDouble("price"),
                        mobileData.getJSONObject(i).getDouble("rating"),
                        favoriteChk,
                        mobileData.getJSONObject(i).getString("brand")
                ));
            }

            cardViewAdapter = new CardMobileAdapter(ListTabActivity.this,mContext,mobileList,new ClickListener() {
                @Override
                public void onPositionClicked(int position) {
                    switch (itemID)
                    {
                        case 0: {
                            openMobileDetail(mobileList.get(position));
                            break;
                        }
                        case 1: {
                            cardViewAdapter.setFavorite(position);
                       /*Toast.makeText(mContext, "Favorite: " +
                               position+"", Toast.LENGTH_SHORT).show();*/
                            if(mobileList.get(position).isFavorite())
                            {
                                favorite.setId(mobileList.get(position).getId());
                                mHelper.addFavorite(favorite);

                                /*Log.w("909",mHelper.getFavoritedList()+"");
                                Toast.makeText(mContext, "Favorite: " +
                                        mHelper.getFavoritedList()+"", Toast.LENGTH_SHORT).show();*/
                            }
                            else
                            {
                                mHelper.delFavorite(mobileList.get(position).getId());
                               /* Log.w("909",mHelper.getFavoritedList()+"");
                                Toast.makeText(mContext, "Favorite: " +
                                        mHelper.getFavoritedList()+"", Toast.LENGTH_SHORT).show();*/
                            }
                            break;
                        }
                        default:break;
                    }

                }
                @Override
                public void onLongClicked(int position) {

                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setAdapter(cardViewAdapter);
        }
        catch (Exception e)
        {
            //Log.e("Err","Set cardview mobile");
        }

    }
    public void sortLowToHigh()
    {
        Collections.sort(mobileList,new Comparator<CardMobile>() {
            @Override
            public int compare(CardMobile lhs, CardMobile rhs) {
            return Double.compare(lhs.getPrice(),rhs.getPrice());
            }
        });
        cardViewAdapter.notifyDataSetChanged();
        //Sort favorite
        Collections.sort(((MainActivity)getActivity()).fmFavoriteMobile.mobileList,new Comparator<CardMobileFavorite>() {
            @Override
            public int compare(CardMobileFavorite lhs, CardMobileFavorite rhs) {
                return Double.compare(lhs.getPrice(),rhs.getPrice());
            }
        });
        ((MainActivity)getActivity()).fmFavoriteMobile.cardViewAdapter.notifyDataSetChanged();
    }
    public void sortHighToLow()
    {
        Collections.sort(mobileList,new Comparator<CardMobile>() {
            @Override
            public int compare(CardMobile lhs, CardMobile rhs) {
                return Double.compare(rhs.getPrice(),lhs.getPrice());
            }
        });
        cardViewAdapter.notifyDataSetChanged();

        //Sort favorite
        Collections.sort(((MainActivity)getActivity()).fmFavoriteMobile.mobileList,new Comparator<CardMobileFavorite>() {
            @Override
            public int compare(CardMobileFavorite lhs, CardMobileFavorite rhs) {
                return Double.compare(rhs.getPrice(),lhs.getPrice());
            }
        });
        ((MainActivity)getActivity()).fmFavoriteMobile.cardViewAdapter.notifyDataSetChanged();
    }
    public void sortRating()
    {
        Collections.sort(mobileList,new Comparator<CardMobile>() {
            @Override
            public int compare(CardMobile lhs, CardMobile rhs) {
                return Double.compare(rhs.getRate(),lhs.getRate());
            }
        });
        cardViewAdapter.notifyDataSetChanged();

        //Sort favorite
        Collections.sort(((MainActivity)getActivity()).fmFavoriteMobile.mobileList,new Comparator<CardMobileFavorite>() {
            @Override
            public int compare(CardMobileFavorite lhs, CardMobileFavorite rhs) {
                return Double.compare(rhs.getRate(),lhs.getRate());
            }
        });
        ((MainActivity)getActivity()).fmFavoriteMobile.cardViewAdapter.notifyDataSetChanged();
    }
    public void openMobileDetail(CardMobile selectedMobile)
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
