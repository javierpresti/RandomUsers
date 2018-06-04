package com.jpresti.randomusers.data;

import android.os.Parcel;
import android.os.Parcelable;

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

    public static class User implements Parcelable {
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


        // ********************************** //
        // ************* Parcel ************* //
        // ********************************** //
        private User(Parcel source) {
            this.id = source.readString();
            this.username = source.readString();
            this.firstName = source.readString();
            this.lastName = source.readString();
            this.email = source.readString();
            this.thumbnail = source.readString();
            this.image = source.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(getId());
            dest.writeString(getUsername());
            dest.writeString(getFirstName());
            dest.writeString(getLastName());
            dest.writeString(getEmail());
            dest.writeString(getThumbnail());
            dest.writeString(getImage());
        }

        public static final Creator<User> CREATOR = new Creator<User>() {
            @Override
            public User[] newArray(int size) {
                return new User[size];
            }

            @Override
            public User createFromParcel(Parcel source) {
                return new User(source);
            }
        };
    }
}
