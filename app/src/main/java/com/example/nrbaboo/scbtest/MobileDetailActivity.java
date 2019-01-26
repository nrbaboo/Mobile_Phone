package com.example.nrbaboo.scbtest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MobileDetailActivity extends AppCompatActivity{

    private TextView mobileName,mobileBrand,mobilePrice,mobileRate,mobileDetail;
    private ViewFlipper mobileImage;
    private String name,price,rate,detail,brand,url,id;
    private JSONArray data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = getIntent().getStringExtra("name");
        brand = getIntent().getStringExtra("brand");
        price = getIntent().getStringExtra("price");
        rate = getIntent().getStringExtra("rate");
        detail = getIntent().getStringExtra("detail");
        url = getIntent().getStringExtra("url");
        id = getIntent().getStringExtra("id");

        mobileName = findViewById(R.id.mobile_name);
        mobileBrand = findViewById(R.id.mobile_brand);
        mobilePrice = findViewById(R.id.mobile_price);
        mobileRate = findViewById(R.id.mobile_rating);
        mobileDetail = findViewById(R.id.mobile_description);
        mobileImage = findViewById(R.id.v_flipper);

        setContent();

        String urlMobile ="https://scb-test-mobile.herokuapp.com/api/mobiles/"+id+"/images/";
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
                Request request = new Request.Builder()
                        .url(urlMobile)
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
                //Log.v("Hope Json Image: ", o.toString());
                try {
                    data = new JSONArray(o.toString());
                    //Log.v("Hope Json Length: ", data.length()+"");
                    for(int i=0;i<data.length();i++)
                    {
                        flipperImages(data.getJSONObject(i).getString("url"));
                    }
                } catch (Exception e) {

                }
            }

        }.execute();
    }
    public void setContent()
    {
        mobileName.setText(name);
        mobileBrand.setText(brand);
        mobilePrice.setText("Price: $"+price);
        mobileRate.setText("Rating: "+rate);
        mobileDetail.setText(detail);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void flipperImages(String imageUrl) {
        ImageView imageView = new ImageView(this);
        Glide.with(this).load(imageUrl)
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher)).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        mobileImage.addView(imageView);
        mobileImage.setFlipInterval(4000);
        mobileImage.setAutoStart(true);

        mobileImage.setInAnimation(this,android.R.anim.slide_in_left);
        mobileImage.setOutAnimation(this,android.R.anim.slide_out_right);

        mobileImage.startFlipping();
    }
}
