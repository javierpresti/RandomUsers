package com.jpresti.randomusers.view.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.jpresti.randomusers.R;
import com.jpresti.randomusers.model.User;
import com.jpresti.randomusers.model.external.RandomUserRequester;
import com.jpresti.randomusers.view.users.UserGridActivity;

/**
 * A fragment representing a single User detail screen.
 * This fragment is either contained in a {@link UserGridActivity}
 * in two-pane mode (on tablets) or a {@link UserDetailActivity}
 * on handsets.
 */
public class UserDetailFragment extends Fragment {
    /**
     * The fragment argument representing the user that this fragment represents.
     */
    private static final String ARGUMENT_USER = "ARGUMENT_USER";
    public static final String BUNDLE_USER = "BUNDLE_USER";

    /**
     * The data content this fragment is presenting.
     */
    private User mUser;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public UserDetailFragment() {
    }

    public static UserDetailFragment newInstance(String userJson) {
        Bundle arguments = new Bundle();
        arguments.putString(UserDetailFragment.ARGUMENT_USER, userJson);
        UserDetailFragment fragment = new UserDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARGUMENT_USER)) {
            // Load the data content specified by the fragment arguments
            setUser(getArguments().getString(ARGUMENT_USER));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_USER, mUser.toJson());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            setUser(savedInstanceState.getString(BUNDLE_USER));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_detail, container, false);

        // Show the data content
        if (mUser != null) {
            NetworkImageView nImageView = getActivity().findViewById(R.id.image_toolbar);
            // nImageView will be null if screen orientation is changed from landscape to portrait,
            // so we keep the state of the image in landscape mode
            if (nImageView != null) {
                RandomUserRequester.getInstance(getContext()).requestImage(mUser.getImage(),
                        nImageView, R.drawable.ic_error_18dp);
            }
            CollapsingToolbarLayout appBarLayout = getActivity().findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mUser.getUsername());
            }

            TextView usernameView = rootView.findViewById(R.id.us_text_username);
            TextView firstNameView = rootView.findViewById(R.id.us_text_firstname);
            TextView lastNameView = rootView.findViewById(R.id.us_text_lastname);
            TextView emailView = rootView.findViewById(R.id.us_text_email);

            usernameView.setText(mUser.getUsername());
            firstNameView.setText(nameToUpperCase(mUser.getFirstName()));
            lastNameView.setText(nameToUpperCase(mUser.getLastName()));
            emailView.setText(mUser.getEmail());
        }

        return rootView;
    }

    protected String nameToUpperCase(String name) {
        String toUpper = name;
        if (name.length() > 0) {
            toUpper = String.valueOf(name.charAt(0)).toUpperCase()
                    + name.substring(1, name.length());
        }
        return toUpper;
    }

    protected void setUser(String jsonUser) {
        mUser = User.fromJson(jsonUser);
    }
}
