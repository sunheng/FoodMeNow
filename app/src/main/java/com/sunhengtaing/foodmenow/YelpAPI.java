package com.sunhengtaing.foodmenow;

import android.content.Context;
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

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

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
public class YelpAPI extends AsyncTask<String, Void, String>{

  private static final String API_HOST = "api.yelp.com";
//  private static final String DEFAULT_TERM = "dinner";
//  private static final String DEFAULT_LOCATION = "San Francisco, CA";
  private static final int SEARCH_LIMIT = 3;
  private static final String SEARCH_PATH = "/v2/search";
  private static final String BUSINESS_PATH = "/v2/business";

    private TextView mContent;
    private String mConsumerKey;
    private String mConsumerSecret;
    private String mToken;
    private String mTokenSecret;

  /*
   * Update OAuth credentials below from the Yelp Developers API site:
   * http://www.yelp.com/developers/getting_started/api_access
   */
//  private static final String CONSUMER_KEY = "-4fS2y7Rwml9xXowup8Pxw";
//  private static final String CONSUMER_SECRET = "eKssg6pSHmtq1580Op35ae3Ozuw";
//  private static final String TOKEN = "wKUe0xXlbtzgggeIpyvqEGg7qAz-cEPL";
//  private static final String TOKEN_SECRET = "P4faV3XaG6VUfSWy-5_Wq0dVX_Q";

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
  public YelpAPI(TextView content, String consumerKey, String consumerSecret, String token, String tokenSecret) {
    mContent = content;
    mConsumerKey = consumerKey;
    mConsumerSecret = consumerSecret;
    mToken = token;
    mTokenSecret = tokenSecret;
  }

  /**
   * Creates and sends a request to the Search API by term and location.
   * <p>
   * See <a href="http://www.yelp.com/developers/documentation/v2/search_api">Yelp Search API V2</a>
   * for more info.
   * 
   * @param term <tt>String</tt> of the search term to be queried
   * @param location <tt>String</tt> of the location
   * @return <tt>String</tt> JSON Response
   */
  public String searchForBusinessesByLocation(String term, String location) {
    OAuthRequest request = createOAuthRequest(SEARCH_PATH);
    request.addQuerystringParameter("term", term);
    request.addQuerystringParameter("location", location);
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
    protected String doInBackground(String... params) {
        this.service = new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(mConsumerKey).apiSecret(mConsumerSecret).build();
        this.accessToken = new Token(mToken, mTokenSecret);
        String businesses = searchForBusinessesByLocation("food", "atlanta");
        return businesses;
    }

    @Override
    protected void onPostExecute(String result) {
//        mContent.setText(result);
        JSONParser parser = new JSONParser();
        JSONObject response = null;
        try {
          response = (JSONObject) parser.parse(result);
        } catch (ParseException pe) {
          System.out.println("Error: could not parse JSON response:");
          System.out.println(result);
          System.exit(1);
        }

        JSONArray businesses = (JSONArray) response.get("businesses");
        JSONObject firstBusiness = (JSONObject) businesses.get(0);
        String firstBusinessID = firstBusiness.get("id").toString();
        mContent.setText(firstBusinessID);
        System.out.println(String.format(
            "%s businesses found, querying business info for the top result \"%s\" ...",
            businesses.size(), firstBusinessID));

        // Select the first business and display business details
//        String businessResponseJSON = searchByBusinessId(firstBusinessID.toString());
//        System.out.println(String.format("Result for business \"%s\" found:", firstBusinessID));
//        System.out.println(businessResponseJSON);
    }
}
