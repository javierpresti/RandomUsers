package com.jpresti.randomusers.users;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.jpresti.randomusers.R;
import com.jpresti.randomusers.data.external.RandomUserRequester;
import com.jpresti.randomusers.detail.UserDetailActivity;

/**
 * An activity representing a grid of Users. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a grid of items, which when touched,
 * lead to a {@link UserDetailActivity} representing
 * item details. On tablets, the activity presents the grid of items and
 * item details side-by-side using two vertical panes.
 */
public class UserGridActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
     */
    private boolean mTwoPane;
    protected SimpleUserGridViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_grid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.user_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View gridView = findViewById(R.id.user_grid);
        assert gridView != null;
        setupGridView((GridView) gridView);
    }

    protected void setupGridView(@NonNull GridView gridView) {
        adapter = new SimpleUserGridViewAdapter(this, mTwoPane);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        RandomUserRequester.resetUsers();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.onStop();
    }
}
