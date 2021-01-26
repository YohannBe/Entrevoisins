package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.events.FavoriteEvent;
import com.openclassrooms.entrevoisins.events.SeeProfileEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourFragment.neighbourAboutMeProfile;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourFragment.neighbourAddressProfile;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourFragment.neighbourAvatarProfile;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourFragment.neighbourFacebookProfile;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourFragment.neighbourIDProfile;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourFragment.neighbourNameProfile;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourFragment.neighbourPhoneProfile;


public class NeighbourFavoriteFragment extends Fragment {

    private NeighbourApiService mApiService;
    private List<Neighbour> mNeighbours;
    private RecyclerView mRecyclerView;


    /**
     * Create and return a new instance
     *
     * @return @{@link NeighbourFavoriteFragment}
     */
    public static NeighbourFavoriteFragment newInstance() {
        NeighbourFavoriteFragment fragment = new NeighbourFavoriteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getNeighbourApiService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_neighbour_list, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return view;
    }

    /**
     * Init the List of neighbours
     */
    private void initList() {
        mNeighbours = mApiService.getNeighboursFavorite();
        mRecyclerView.setAdapter(new MyNeighbourRecyclerViewAdapter(mNeighbours));
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    private void initIntent(Intent toProfileIntent, SeeProfileEvent event) {
        toProfileIntent.putExtra(neighbourNameProfile, event.neighbour.getName());
        toProfileIntent.putExtra(neighbourAvatarProfile, event.neighbour.getAvatarUrl());
        toProfileIntent.putExtra(neighbourPhoneProfile, event.neighbour.getPhoneNumber());
        toProfileIntent.putExtra(neighbourAboutMeProfile, event.neighbour.getAboutMe());
        toProfileIntent.putExtra(neighbourAddressProfile, event.neighbour.getAddress());
        toProfileIntent.putExtra(neighbourFacebookProfile, "https://www.facebook.com/" + event.neighbour.getName() + "/");
        toProfileIntent.putExtra(neighbourIDProfile, event.neighbour.getId());
    }

    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
    @Subscribe
    public void onDeleteFavoriteNeighbour(DeleteNeighbourEvent event) {
        mApiService.deleteNeighbour(event.neighbour);
        initList();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}