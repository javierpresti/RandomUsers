package com.jpresti.randomusers.detail;

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
import com.jpresti.randomusers.data.RandomUserRequester;
import com.jpresti.randomusers.data.UsersContent;
import com.jpresti.randomusers.users.UserGridActivity;

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
    public static final String ARG_USER = "arg_user";
    public static final String STATE_USER = "state_user";

    /**
     * The data content this fragment is presenting.
     */
    private UsersContent.User user;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public UserDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_USER)) {
            // Load the data content specified by the fragment arguments
            setUser((UsersContent.User) getArguments().getParcelable(ARG_USER));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_USER, user);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            setUser((UsersContent.User) savedInstanceState.getParcelable(STATE_USER));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_detail, container, false);

        // Show the data content
        if (user != null) {
            NetworkImageView nImageView = getActivity().findViewById(R.id.toolbar_image);
            /* nImageView will be null if screen orientation is changed from landscape to portrait,
             * so we keep the state of the image in landscape mode
             */
            if (nImageView != null) {
                RandomUserRequester.getInstance(getContext()).requestImage(
                        getContext(), user.getImage(), nImageView, R.drawable.ic_error_18dp);
            }
            final CollapsingToolbarLayout appBarLayout = getActivity().findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(user.getUsername());
            }

            ((TextView) rootView.findViewById(R.id.user_username)).setText(user.getUsername());
            ((TextView) rootView.findViewById(R.id.user_firstname)).setText(nameToUpperCase(user.getFirstName()));
            ((TextView) rootView.findViewById(R.id.user_lastname)).setText(nameToUpperCase(user.getLastName()));
            ((TextView) rootView.findViewById(R.id.user_email)).setText(user.getEmail());
        }

        return rootView;
    }

    protected String nameToUpperCase(String name) {
        String toUpper = name;
        if (name.length() > 0) {
            toUpper = String.valueOf(name.charAt(0)).toUpperCase() + name.substring(1, name.length());
        }
        return toUpper;
    }

    protected void setUser(UsersContent.User user) {
        this.user = user;
    }

}
