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
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import static com.openclassrooms.entrevoisins.ui.neighbour_list.MyNeighbourRecyclerViewAdapter.favoriteString;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.MyNeighbourRecyclerViewAdapter.positionString;

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

        getDataBack();

        if (neighbourProfile.isFavorite())
            favoriteButton.setBackgroundTintList((ColorStateList.valueOf(666666)));

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /** check if the profile was already set as favorite, if so
            - profile is set to false
            - the ui is updated, the floating button will have no tint
            - a toast message will inform the user on what happened
            else
            - profile is set to true
            - the ui is updated, the floating button will have no tint
            - a toast message will inform the user on what happened
         */
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (neighbourProfile.isFavorite()) {
                    neighbourProfile.setFavorite(false);
                    favoriteButton.setBackgroundTintList(null);
                    Toast.makeText(NeighbourProfile.this, R.string.removed_toast, Toast.LENGTH_SHORT).show();
                } else {
                    neighbourProfile.setFavorite(true);
                    favoriteButton.setBackgroundTintList((ColorStateList.valueOf(666666)));
                    Toast.makeText(NeighbourProfile.this, R.string.added_toast, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /** get the data back from the intent in the recyclerview adapter
        get back the position in the array
        get back if the user tried to see the profile from the regular list or from the favorite list with the boolean
        check if the demand come from the regular list or from the favorite list and depending on the result we will fetch the neighbour from
        one array or the other
        set up the facebook address
        and init the component*/
    private void getDataBack() {
        Intent neighbourIntent = getIntent();
        int position = neighbourIntent.getIntExtra(positionString, 0);
        boolean favorite = neighbourIntent.getBooleanExtra(favoriteString, false);
        if (favorite)
            neighbourProfile = mApiService.getNeighboursFavorite().get(position);
        else
            neighbourProfile = mApiService.getNeighbours().get(position);
        neighbourProfile.setFacebookAddress("https://www.facebook.com/" + neighbourProfile.getName() + "/");
        initText(neighbourProfile.getName(), neighbourProfile.getAddress(), neighbourProfile.getFacebookAddress(), neighbourProfile.getPhoneNumber(),
                neighbourProfile.getAboutMe(), neighbourProfile.getAvatarUrl());
    }

    /** simply initialise the components*/
    private void initText(String mName, String mAddress, String mFacebook, String mPhone, String mAboutMe, String mAvatar) {
        name.setText(mName);
        nameTitle.setText(mName);
        address.setText(mAddress);
        facebook.setText(mFacebook);
        phone.setText(mPhone);
        aboutMe.setText(mAboutMe);
        Glide.with(this).load(mAvatar).placeholder(R.drawable.ic_account).into(avatar);
    }
}
