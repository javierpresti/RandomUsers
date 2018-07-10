package com.jpresti.randomusers.model;

import com.google.gson.Gson;

public class User {
    protected static Gson gson;

    protected final String mUsername;
    protected final String mFirstName;
    protected final String mLastName;
    protected final String mEmail;
    protected final String mThumbnail;
    protected final String mImage;

    public User(String username, String firstName, String lastName, String email,
                String thumbnail, String image) {
        mUsername = username;
        mFirstName = firstName;
        mLastName = lastName;
        mEmail = email;
        mThumbnail = thumbnail;
        mImage = image;
    }

    protected static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public static User fromJson(String json) {
        return getGson().fromJson(json, User.class);
    }

    public String toJson() {
        return getGson().toJson(this);
    }

    public String getUsername() {
        return mUsername;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public String getImage() {
        return mImage;
    }
}
