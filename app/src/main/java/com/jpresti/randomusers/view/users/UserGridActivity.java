package com.jpresti.randomusers.view.users;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jpresti.randomusers.R;
import com.jpresti.randomusers.model.User;
import com.jpresti.randomusers.model.external.RandomUserRequester;
import com.jpresti.randomusers.view.detail.UserDetailActivity;
import com.jpresti.randomusers.view.detail.UserDetailFragment;

/**
 * An activity representing a grid of Users. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a grid of items, which when touched,
 * lead to a {@link UserDetailActivity} representing
 * item details. On tablets, the activity presents the grid of items and
 * item details side-by-side using two vertical panes.
 */
public class UserGridActivity extends AppCompatActivity
        implements UsersFragment.OnGridFragmentInteractionListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_grid);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        UsersFragment fragment = new UsersFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment)
                .commitNow();

        if (findViewById(R.id.dt_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        RandomUserRequester.resetUsers();
    }

    @Override
    protected void onStop() {
        super.onStop();
        RandomUserRequester.getInstance(this).cancelRequests();
    }

    @Override
    public void onGridFragmentInteraction(User user) {
        if (mTwoPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dt_container, UserDetailFragment.newInstance(user.toJson()))
                    .commit();
        } else {
            startActivity(UserDetailActivity.getStartIntent(this, user.toJson()));
        }
    }
}
