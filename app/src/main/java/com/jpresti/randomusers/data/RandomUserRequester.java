package com.jpresti.randomusers.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.jpresti.randomusers.users.SimpleUserGridViewAdapter;
import com.jpresti.randomusers.util.NetworkRequester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RandomUserRequester {
    public static final int QUANTITY_RESULTS = 50;
    protected static final String RANDOMUSER_URL = "https://randomuser.me/api/";
    protected static final String TAG = "NetworkRequester";

    protected static NetworkRequester networkRequester;
    protected static RandomUserRequester instance;

    protected RandomUserRequester(Context context) {
        networkRequester = NetworkRequester.getInstance(context);
    }

    public static synchronized RandomUserRequester getInstance(Context context) {
        if (instance == null) {
            instance = new RandomUserRequester(context);
        }
        return instance;
    }

    protected static String getUrl() {
        return RANDOMUSER_URL + "?results=" + QUANTITY_RESULTS;
    }

    public void setUpRequest(Context context, final DataListener listener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                RandomUserRequester.getUrl(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            getUsers(response);
                            listener.onResponse();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(SimpleUserGridViewAdapter.class.getName(), "JSON Error: " + error.getMessage());
                        listener.onErrorResponse(error.getMessage());
                    }
                }
        );
        jsonObjectRequest.setTag(TAG);
        networkRequester.addToRequestQueue(context, jsonObjectRequest);
    }

    protected static void getUsers(JSONObject jsonObject) throws JSONException {
        UsersContent.getUsers().clear();
        JSONArray results = jsonObject.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            UsersContent.addItem(mkUser(results.getJSONObject(i)));
        }
    }

    protected static UsersContent.User mkUser(JSONObject jsonObject) throws JSONException {
        return new UsersContent.User(
                jsonObject.getJSONObject("login").getString("username"),
                jsonObject.getJSONObject("name").getString("first"),
                jsonObject.getJSONObject("name").getString("last"),
                jsonObject.getString("email"),
                jsonObject.getJSONObject("picture").getString("medium"),
                jsonObject.getJSONObject("picture").getString("large"));
    }

    public void requestImage(Context context, String imageUrl, final NetworkImageView nImageView, int defaultImage, int errorImage) {
        nImageView.setDefaultImageResId(defaultImage);
        requestImage(context, imageUrl, nImageView, errorImage);
    }

    public void requestImage(Context context, String imageUrl, final NetworkImageView nImageView, int errorImage) {
        nImageView.setErrorImageResId(errorImage);
        nImageView.setImageUrl(imageUrl, networkRequester.getImageLoader(context));
    }


    public void requestImage(Context context, String imageUrl, final BitmapListener listener) {
        ImageRequest imageRequest = new ImageRequest(
                imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        listener.onResponse(response);
                    }
                }, 0, 0, null, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onErrorResponse(error.getMessage());
                    }
                }
        );
        networkRequester.addToRequestQueue(context, imageRequest);
    }

    public void cancelRequests() {
        networkRequester.cancelRequests(TAG);
    }

    public interface DataListener {
        void onResponse();

        void onErrorResponse(String error);
    }

    public interface BitmapListener {
        void onResponse(Bitmap response);

        void onErrorResponse(String error);
    }

}
