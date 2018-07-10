package com.jpresti.randomusers.view.users;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;
import com.jpresti.randomusers.R;
import com.jpresti.randomusers.model.User;
import com.jpresti.randomusers.model.external.RandomUserRequester;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link User}
 * image and makes a call to the specified {@link UsersFragment.OnGridFragmentInteractionListener}.
 */
public class UserRecyclerViewAdapter
        extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    protected Context mContext;
    private final List<User> mValues;
    private final UsersFragment.OnGridFragmentInteractionListener mListener;

    public UserRecyclerViewAdapter(Context context, List<User> items,
                                   UsersFragment.OnGridFragmentInteractionListener listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        RandomUserRequester.getInstance(mContext)
                .requestImage(holder.mItem.getThumbnail(), (NetworkImageView) holder.mImageView,
                        R.drawable.ic_loading_18dp, R.drawable.ic_error_18dp);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onGridFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = view.findViewById(R.id.image_grid_user);
        }
    }
}
