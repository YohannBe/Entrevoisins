package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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


public class NeighbourFragment extends Fragment {

    private NeighbourApiService mApiService;
    private List<Neighbour> mNeighbours;
    private RecyclerView mRecyclerView;
    protected static String neighbourNameProfile = "NEIGHBOURNAME";
    protected static String neighbourAddressProfile = "NEIGHBOURADRESS";
    protected static String neighbourPhoneProfile = "NEIGHBOURPHONE";
    protected static String neighbourFacebookProfile = "NEIGHBOURFACEBOOK";
    protected static String neighbourAboutMeProfile = "NEIGHBOURABOUTME";
    protected static String neighbourAvatarProfile = "NEIGHBOURAVATAR";
    protected static String neighbourIDProfile = "NEIGHBOURID";


    /**
     * Create and return a new instance
     * @return @{@link NeighbourFragment}
     */
    public static NeighbourFragment newInstance() {
        NeighbourFragment fragment = new NeighbourFragment();
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
        mNeighbours = mApiService.getNeighbours();
        mRecyclerView.setAdapter(new MyNeighbourRecyclerViewAdapter(mNeighbours));
    }

    @Override
    public void onResume() {
        super.onResume();
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

    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
    @Subscribe
    public void onDeleteNeighbour(DeleteNeighbourEvent event) {
        mApiService.deleteNeighbour(event.neighbour);
        initList();
        Toast.makeText(getContext(), R.string.deleted_contact_toast, Toast.LENGTH_SHORT).show();
    }

    /**
     * Fired if the user clicks on a name textview
     * @param event
     */
    @Subscribe
    public void onSeeProfileNeighbour(SeeProfileEvent event) {
        Intent toProfileIntent = new Intent(getContext(), NeighbourProfile.class);
        initIntent(toProfileIntent, event);
        startActivity(toProfileIntent);
    }

    private void initIntent(Intent toProfileIntent, SeeProfileEvent event) {
        toProfileIntent.putExtra(neighbourNameProfile, event.neighbour.getName());
        toProfileIntent.putExtra(neighbourAvatarProfile, event.neighbour.getAvatarUrl());
        toProfileIntent.putExtra(neighbourPhoneProfile, event.neighbour.getPhoneNumber());
        toProfileIntent.putExtra(neighbourAboutMeProfile, event.neighbour.getAboutMe());
        toProfileIntent.putExtra(neighbourAddressProfile, event.neighbour.getAddress());
        toProfileIntent.putExtra(neighbourFacebookProfile, "https://www.facebook.com/"+event.neighbour.getName()+"/");
        toProfileIntent.putExtra(neighbourIDProfile, event.neighbour.getId());

    }


}