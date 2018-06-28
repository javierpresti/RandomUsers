package com.jpresti.randomusers.data.external;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.jpresti.randomusers.data.User;
import com.jpresti.randomusers.users.SimpleUserGridViewAdapter;
import com.jpresti.randomusers.util.NetworkRequester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RandomUserRequester {
    public static final int QUANTITY_RESULTS = 50;
    protected static final String RANDOMUSER_URL = "https://randomuser.me/api/";
    protected static final String TAG = "NetworkRequester";

    protected static NetworkRequester networkRequester;
    protected static RandomUserRequester instance;
    protected static final List<User> USERS = new ArrayList<>();

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

    public void requestUsers(DataListener<List<User>> dataListener) {
        List<User> users = getUsers();
        if (users.isEmpty()) {
            setUpRequest(dataListener);
        } else {
            dataListener.onResponse(users);
        }
    }

    protected void setUpRequest(final DataListener<List<User>> listener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                RandomUserRequester.getUrl(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            getUsers(response);
                            listener.onResponse(getUsers());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(SimpleUserGridViewAdapter.class.getName(), "JSON Error: " + error.getMessage());
                        listener.onError(error.getMessage());
                    }
                }
        );
        jsonObjectRequest.setTag(TAG);
        networkRequester.addToRequestQueue(jsonObjectRequest);
    }

    protected static void getUsers(JSONObject jsonObject) throws JSONException {
        resetUsers();
        JSONArray results = jsonObject.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            addItem(mkUser(results.getJSONObject(i)));
        }
    }

    protected static User mkUser(JSONObject jsonObject) throws JSONException {
        return new User(
                jsonObject.getJSONObject("login").getString("username"),
                jsonObject.getJSONObject("name").getString("first"),
                jsonObject.getJSONObject("name").getString("last"),
                jsonObject.getString("email"),
                jsonObject.getJSONObject("picture").getString("medium"),
                jsonObject.getJSONObject("picture").getString("large"));
    }

    public void requestImage(String imageUrl, final NetworkImageView nImageView, int defaultImage, int errorImage) {
        nImageView.setDefaultImageResId(defaultImage);
        requestImage(imageUrl, nImageView, errorImage);
    }

    public void requestImage(String imageUrl, final NetworkImageView nImageView, int errorImage) {
        nImageView.setErrorImageResId(errorImage);
        nImageView.setImageUrl(imageUrl, networkRequester.getImageLoader());
    }


    public void requestImage(String imageUrl, final DataListener<Bitmap> listener) {
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
                        listener.onError(error.getMessage());
                    }
                }
        );
        networkRequester.addToRequestQueue(imageRequest);
    }

    public void cancelRequests() {
        networkRequester.cancelRequests(TAG);
    }

    public static void resetUsers() {
        getUsers().clear();
    }

    protected static void addItem(User item) {
        getUsers().add(item);
    }

    protected static List<User> getUsers() {
        return USERS;
    }

    public interface DataListener<T> {
        void onResponse(T t);

        void onError(String error);
    }

}
