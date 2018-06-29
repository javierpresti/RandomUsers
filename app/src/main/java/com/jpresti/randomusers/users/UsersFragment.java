package com.jpresti.randomusers.users;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jpresti.randomusers.R;
import com.jpresti.randomusers.data.external.RandomUserRequester;
import com.jpresti.randomusers.data.User;
import com.jpresti.randomusers.util.UiUtility;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnGridFragmentInteractionListener}
 * interface.
 */
public class UsersFragment extends Fragment {

    private OnGridFragmentInteractionListener mListener;
    protected RecyclerView recyclerView;
    protected UserRecyclerViewAdapter adapter;
    protected View loadingPanel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UsersFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGridFragmentInteractionListener) {
            mListener = (OnGridFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnGridFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),
                    UiUtility.calculateNoOfColumns(getResources(), R.dimen.image_size)));
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpRequest();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        recyclerView = null;
    }

    protected void setUpRequest() {
        loadingPanel = getActivity().findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.VISIBLE);
        RandomUserRequester.getInstance(getContext()).requestUsers(
                new RandomUserRequester.DataListener<List<User>>() {
                    @Override
                    public void onResponse(List<User> users) {
                        if (getActivity() != null) {
                            adapter = new UserRecyclerViewAdapter(getContext(), users, mListener);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            loadingPanel.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        if (getActivity() != null) {
                            loadingPanel.setVisibility(View.GONE);
                            final Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.frameLayout),
                                    R.string.usergrid_json_error, Snackbar.LENGTH_INDEFINITE);

                            snackbar.setAction(R.string.usergrid_retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snackbar.dismiss();
                                    setUpRequest();
                                }
                            }).show();
                        }
                    }
                });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     */
    public interface OnGridFragmentInteractionListener {
        void onGridFragmentInteraction(User user);
    }
}
