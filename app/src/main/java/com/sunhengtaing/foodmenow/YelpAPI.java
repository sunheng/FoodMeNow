package com.sunhengtaing.foodmenow;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.util.Random;

/**
 * Code sample for accessing the Yelp API V2.
 * 
 * This program demonstrates the capability of the Yelp API version 2.0 by using the Search API to
 * query for businesses by a search term and location, and the Business API to query additional
 * information about the top result from the search query.
 * 
 * <p>
 * See <a href="http://www.yelp.com/developers/documentation">Yelp Documentation</a> for more info.
 * 
 */
public class YelpAPI extends AsyncTask<String, Void, JSONArray>{

    private static final String API_HOST = "api.yelp.com";
    private static final int SEARCH_LIMIT = 20;
    private static final String SEARCH_PATH = "/v2/search";
    private static final String BUSINESS_PATH = "/v2/business";

    private String[] mFoodTypes;

    private TextView mContent;
    private String mConsumerKey;
    private String mConsumerSecret;
    private String mToken;
    private String mTokenSecret;
    OAuthService service;
    Token accessToken;


  /**
   * Setup the Yelp API OAuth credentials.
   * 
   * @param consumerKey Consumer key
   * @param consumerSecret Consumer secret
   * @param token Token
   * @param tokenSecret Token secret
   */
  public YelpAPI(String consumerKey, String consumerSecret, String token,
                 String tokenSecret, TextView content) {
    mConsumerKey = consumerKey;
    mConsumerSecret = consumerSecret;
    mToken = token;
    mTokenSecret = tokenSecret;
    mContent = content;
    mFoodTypes = new String[]{"mexican", "asian", "mediterranean"};
  }

  /**
   * Creates and sends a request to the Search API by term and location.
   * <p>
   * See <a href="http://www.yelp.com/developers/documentation/v2/search_api">Yelp Search API V2</a>
   * for more info.
   *
   * @return <tt>String</tt> JSON Response
   */
  public String searchForBusinessesByLocation(String term, String latitude, String longitude, String radius) {
    OAuthRequest request = createOAuthRequest(SEARCH_PATH);
    request.addQuerystringParameter("term", term);
    request.addQuerystringParameter("ll", latitude + "," + longitude);
    request.addQuerystringParameter("radius_filter", radius);
    request.addQuerystringParameter("limit", String.valueOf(SEARCH_LIMIT));
    return sendRequestAndGetResponse(request);
  }

  /**
   * Creates and sends a request to the Business API by business ID.
   * <p>
   * See <a href="http://www.yelp.com/developers/documentation/v2/business">Yelp Business API V2</a>
   * for more info.
   * 
   * @param businessID <tt>String</tt> business ID of the requested business
   * @return <tt>String</tt> JSON Response
   */
  public String searchByBusinessId(String businessID) {
    OAuthRequest request = createOAuthRequest(BUSINESS_PATH + "/" + businessID);
    return sendRequestAndGetResponse(request);
  }

  /**
   * Creates and returns an {@link OAuthRequest} based on the API endpoint specified.
   * 
   * @param path API endpoint to be queried
   * @return <tt>OAuthRequest</tt>
   */
  private OAuthRequest createOAuthRequest(String path) {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://" + API_HOST + path);
    return request;
  }

  /**
   * Sends an {@link OAuthRequest} and returns the {@link Response} body.
   * 
   * @param request {@link OAuthRequest} corresponding to the API request
   * @return <tt>String</tt> body of API response
   */
  private String sendRequestAndGetResponse(OAuthRequest request) {
    System.out.println("Querying " + request.getCompleteUrl() + " ...");
    this.service.signRequest(this.accessToken, request);
    Response response = request.send();
    return response.getBody();
  }

    @Override
    protected JSONArray doInBackground(String... params) {
        this.service = new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(mConsumerKey).apiSecret(mConsumerSecret).build();
        this.accessToken = new Token(mToken, mTokenSecret);
        String term = params[0];
        String latitude = params[1];
        String longitude = params[2];
        String radius = params[3];
        JSONParser parser = new JSONParser();
        JSONArray businesses = new JSONArray();
        for (String foodType : mFoodTypes) {
            String responseString = searchForBusinessesByLocation(foodType, latitude, longitude, radius);
            JSONObject response = null;
            try {
                response = (JSONObject) parser.parse(responseString);
            } catch (ParseException pe) {
                System.out.println("Error: could not parse JSON response:");
                System.out.println(responseString);
                System.exit(1);
            }
            System.out.println("Returning total Businesses: " + businesses.size());

            businesses.addAll((JSONArray) response.get("businesses"));
        }
        System.out.println("Returning total Businesses: " + businesses.size());

        return businesses;
    }

    @Override
    protected void onPostExecute(JSONArray businessesJSONArray) {
        Random rand = new Random();
        int randomIndex = rand.nextInt(businessesJSONArray.size());
        JSONObject randomBusiness = (JSONObject) businessesJSONArray.get(randomIndex);
        String randomBusinessName = randomBusiness.get("name").toString();
        JSONArray locations = (JSONArray)((JSONObject)randomBusiness.get("location")).get("address");
        String randomBusinessLocation = locations.get(0).toString();
        StringBuilder contentText = new StringBuilder();
        contentText.append(randomBusinessName + "\n");
        contentText.append(randomBusinessLocation);
//        mContent.setText(firstBusinessID + randomIndex);
        mContent.setText(contentText.toString());
//        System.out.println("Returning total Businesses: " + businessesJSONArray.size());

    }
}
