package com.example.nrbaboo.scbtest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import org.json.JSONArray;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    JSONArray mobileData;

    public ListTabActivity fmMobileList;
    public FavoriteTabActivity fmFavoriteMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        fmMobileList = new ListTabActivity();
        fmFavoriteMobile = new FavoriteTabActivity();
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(fmMobileList, "Mobile List");
        adapter.addFragment(fmFavoriteMobile, "Favorite List");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url("https://scb-test-mobile.herokuapp.com/api/mobiles/")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Cache-Control", "no-cache")
                        .build();

                Response response = null;

                try{
                    response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        return response.body().string();
                    }else
                        return response;

                }catch (IOException e){
                    //Log.v("Error in okHTTP:",e.toString());
                    e.printStackTrace();
                }

                return response;
            }

            @Override
            protected void onPostExecute(Object o) {

                //Log.v("String",o.toString());
                try {
                    mobileData = new JSONArray(o.toString());
                    //Log.w("7080","Hope: "+ mobileData.length());
                    fmMobileList.setMobileData(mobileData);
                    fmMobileList.setCardView();
                    fmFavoriteMobile.setMobileData(mobileData);
                    fmFavoriteMobile.setCardView();
                    fmMobileList.sortLowToHigh();

                }catch (Exception e) {
                    //Log.v("error", e.toString());
                }
            }

        }.execute();
    }


}
