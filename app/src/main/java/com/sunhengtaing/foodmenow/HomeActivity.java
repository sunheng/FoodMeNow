package com.sunhengtaing.foodmenow;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import java.util.List;


public class HomeActivity extends ActionBarActivity {

    private LocationManager mLocationManager;

    private Location mLocation;
    private String mTypeOfFood;
    private String mRadius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mLocation = getLastKnownLocation();
        mTypeOfFood = "Food";
        mRadius = "10000";
        findButtonListener();
    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
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
                AsyncTask<String, Void, JSONArray> searchResponseJSON = new YelpAPI(consumerKey, consumerSecret, token, tokenSecret, content)
                        .execute(mTypeOfFood, Double.toString(mLocation.getLatitude()), Double.toString(mLocation.getLongitude()), mRadius);
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
