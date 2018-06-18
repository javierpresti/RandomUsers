package com.jpresti.randomusers.data;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class UsersContent {

    protected static final List<User> USERS = new ArrayList<>();
    protected static long idCount = 0;

    public static void addItem(User item) {
        getUsers().add(item);
    }

    public static List<User> getUsers() {
        return USERS;
    }

    public static class User {
        protected static Gson gson;

        protected final String id;
        protected final String username;
        protected final String firstName;
        protected final String lastName;
        protected final String email;
        protected final String thumbnail;
        protected final String image;

        public User(String username, String firstName, String lastName, String email,
                    String thumbnail, String image) {
            this.id = String.valueOf(idCount++);
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.thumbnail = thumbnail;
            this.image = image;
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

        public String getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public String getImage() {
            return image;
        }
    }
}
