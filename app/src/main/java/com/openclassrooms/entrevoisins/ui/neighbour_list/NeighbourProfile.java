package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.bumptech.glide.Glide;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.FavoriteEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourFragment.neighbourAboutMeProfile;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourFragment.neighbourAddressProfile;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourFragment.neighbourAvatarProfile;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourFragment.neighbourFacebookProfile;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourFragment.neighbourIDProfile;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourFragment.neighbourNameProfile;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourFragment.neighbourPhoneProfile;

public class NeighbourProfile extends AppCompatActivity {

    @BindView(R.id.image_profile_imageView)
    ImageView avatar;
    @BindView(R.id.contact_name_profile_textView)
    TextView name;
    @BindView(R.id.name_title_textView)
    TextView nameTitle;
    @BindView(R.id.contact_location_profile_textView)
    TextView address;
    @BindView(R.id.contact_website_profile_textView)
    TextView facebook;
    @BindView(R.id.contact_phone_profile_textView)
    TextView phone;
    @BindView(R.id.actual_description_profile_textView)
    TextView aboutMe;
    @BindView(R.id.arrow_back_imageButton)
    ImageButton arrowBack;
    @BindView(R.id.floatingActionButton_add_favorite)
    FloatingActionButton favoriteButton;

    Neighbour neighbourProfile;
    private NeighbourApiService mApiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighbour_profile);
        ButterKnife.bind(this);

        initWidget();

    }

    private void initWidget() {
        mApiService = DI.getNeighbourApiService();

        neighbourProfile = getDataBack();

        if (check(neighbourProfile))
            favoriteButton.setBackgroundTintList((ColorStateList.valueOf(666666)));

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!check(neighbourProfile)) {
                    EventBus.getDefault().post(new FavoriteEvent(neighbourProfile, true));
                    if (check(neighbourProfile))
                        Toast.makeText(NeighbourProfile.this, R.string.added_toast, Toast.LENGTH_SHORT).show();
                } else {
                    EventBus.getDefault().post(new FavoriteEvent(neighbourProfile, false));
                    if (!check(neighbourProfile))
                        Toast.makeText(NeighbourProfile.this, R.string.removed_toast , Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean check(Neighbour neighbourProfile) {
        for (int i = 0; i < mApiService.getNeighboursFavorite().size(); i++) {
            if (neighbourProfile.getId() == mApiService.getNeighboursFavorite().get(i).getId())
                return true;
        }
        return false;
    }


    private Neighbour getDataBack() {
        Intent neighbourIntent = getIntent();

        String mName = neighbourIntent.getStringExtra(neighbourNameProfile);
        String mAddress = neighbourIntent.getStringExtra(neighbourAddressProfile);
        String mFacebook = neighbourIntent.getStringExtra(neighbourFacebookProfile);
        String mPhone = neighbourIntent.getStringExtra(neighbourPhoneProfile);
        String mAboutMe = neighbourIntent.getStringExtra(neighbourAboutMeProfile);
        String mAvatar = neighbourIntent.getStringExtra(neighbourAvatarProfile);
        long mId = neighbourIntent.getLongExtra(neighbourIDProfile, 0);

        initText(mName, mAddress, mFacebook, mPhone, mAboutMe, mAvatar);

        return new Neighbour(mId, mName, mAvatar, mAddress, mPhone, mAboutMe);

    }

    private void initText(String mName, String mAddress, String mFacebook, String mPhone, String mAboutMe, String mAvatar) {
        name.setText(mName);
        nameTitle.setText(mName);
        address.setText(mAddress);
        facebook.setText(mFacebook);
        phone.setText(mPhone);
        aboutMe.setText(mAboutMe);
        Glide.with(this).load(mAvatar).placeholder(R.drawable.ic_account).into(avatar);
    }

    /**
     * Fired if the user clicks on the floating button
     *
     * @param event
     */
    @Subscribe
    public void onFavoriteEvent(FavoriteEvent event) {
        if (event.action){
            event.neighbour.setFavorite(true);
            mApiService.addNeighbourToFavorite(event.neighbour);
            favoriteButton.setBackgroundTintList((ColorStateList.valueOf(666666)));
        }
        else{
            event.neighbour.setFavorite(false);
            mApiService.deleteFromFavoriteList(event.neighbour);
            favoriteButton.setBackgroundTintList(null);
        }

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
