package com.jpresti.randomusers.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.NetworkImageView;
import com.jpresti.randomusers.R;
import com.jpresti.randomusers.data.RandomUserRequester;
import com.jpresti.randomusers.data.UsersContent;
import com.jpresti.randomusers.detail.UserDetailActivity;
import com.jpresti.randomusers.detail.UserDetailFragment;

public class SimpleUserGridViewAdapter extends BaseAdapter {

    private final UserGridActivity mParentActivity;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            UsersContent.User user = (UsersContent.User) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(UserDetailFragment.ARG_USER, user);
                UserDetailFragment fragment = new UserDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.user_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, UserDetailActivity.class);
                intent.putExtra(UserDetailFragment.ARG_USER, user);

                context.startActivity(intent);
            }
        }
    };

    public SimpleUserGridViewAdapter(UserGridActivity parent, boolean twoPane) {
        mParentActivity = parent;
        mTwoPane = twoPane;
        if (UsersContent.getUsers().isEmpty()) {
            setUpRequest();
        }
    }

    protected void setUpRequest() {
        RandomUserRequester.getInstance(mParentActivity)
                .setUpRequest(mParentActivity, new RandomUserRequester.DataListener() {
                    @Override
                    public void onResponse() {
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onErrorResponse(String error) {
                        final Snackbar snackbar = Snackbar.make(mParentActivity.findViewById(R.id.frameLayout),
                                R.string.usergrid_json_error, Snackbar.LENGTH_INDEFINITE);

                        snackbar.setAction(R.string.usergrid_retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                                setUpRequest();
                            }
                        }).show();
                    }
                });
    }

    @Override
    public int getCount() {
        return UsersContent.getUsers().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NetworkImageView nImageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            nImageView = new NetworkImageView(mParentActivity);
            int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96,
                    mParentActivity.getResources().getDisplayMetrics());
            nImageView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
        } else {
            nImageView = (NetworkImageView) convertView;
        }
        UsersContent.User user = UsersContent.getUsers().get(position);
        RandomUserRequester.getInstance(mParentActivity).requestImage(mParentActivity,
                user.getThumbnail(), nImageView, R.drawable.ic_loading_18dp, R.drawable.ic_error_18dp);
        nImageView.setTag(user);
        nImageView.setOnClickListener(mOnClickListener);
        return nImageView;
    }

    public void onStop() {
        RandomUserRequester.getInstance(mParentActivity).cancelRequests();
    }

}