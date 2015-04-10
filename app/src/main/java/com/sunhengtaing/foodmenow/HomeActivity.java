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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findButtonListener();
    }

    public void findButtonListener() {
        Button btFind = (Button) findViewById(R.id.btFind);
        final Context context = this;
        btFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView content = (TextView) findViewById(R.id.tvContent);
//                content.setText("WOO");
                String consumerKey = getResources().getString(R.string.CONSUMER_KEY);
//                content.setText(consumerKey);
                String consumerSecret = getResources().getString(R.string.CONSUMER_SECRET);
                String token = getResources().getString(R.string.TOKEN);
                String tokenSecret = getResources().getString(R.string.TOKEN_SECRET);
//                YelpAPI yelpApi = new YelpAPI(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
//                String searchResponseJSON = yelpApi.searchForBusinessesByLocation("lunch", "AtlantaGeorgia");
                AsyncTask<String, Void, String> searchResponseJSON = new YelpAPI(content, consumerKey, consumerSecret, token, tokenSecret).execute("Asdf");
//                JSONParser parser = new JSONParser();
//                JSONObject response = null;
//                try {
//                    response = (JSONObject) parser.parse(searchResponseJSON);
//                } catch (ParseException pe) {
//                    System.out.println("Error: could not parse JSON response:");
//                    System.out.println(searchResponseJSON);
//                    System.exit(1);
//                }
//
//                JSONArray businesses = (JSONArray) response.get("businesses");
//                JSONObject firstBusiness = (JSONObject) businesses.get(0);
//                String firstBusinessID = firstBusiness.get("id").toString();
//
//                content.setText(firstBusinessID);
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
