package com.sunhengtaing.foodmenow;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class HomeActivity extends ActionBarActivity {

    private String mLatitude;
    private String mLongitude;
    private String mTypeOfFood;
    private String mRadius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTypeOfFood = "Lunch";
        mRadius = "1000";
        mLatitude = 33.774018 + "";
        mLongitude = -84.398813 + "";
        findButtonListener();
    }

    public void findButtonListener() {
        Button btFind = (Button) findViewById(R.id.btFind);
        final Context context = this;
        btFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView content = (TextView) findViewById(R.id.tvContent);
                String consumerKey = getResources().getString(R.string.CONSUMER_KEY);
                String consumerSecret = getResources().getString(R.string.CONSUMER_SECRET);
                String token = getResources().getString(R.string.TOKEN);
                String tokenSecret = getResources().getString(R.string.TOKEN_SECRET);
                AsyncTask<String, Void, String> searchResponseJSON = new YelpAPI(consumerKey, consumerSecret, token, tokenSecret, content)
                        .execute(mTypeOfFood, mLatitude, mLongitude, mRadius);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
}
