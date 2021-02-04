package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.os.Handler;
import android.support.design.widget.TabItem;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.DummyNeighbourGenerator;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.junit.Assert.*;

public class NeighbourFavoriteFragmentTest {

    private NeighbourApiService service;

    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule =
            new ActivityTestRule(ListNeighbourActivity.class);

    @Before
    public void initProfileActivity() {
        service = DI.getNewInstanceApiService();
    }

    @Test
    public void buttonProfileAddFavoriteList() {
        List<Neighbour> neighbours = service.getNeighbours();
        Neighbour neighbourProfile = neighbours.get(0);
        List<Neighbour> neighboursFavorite = service.getNeighboursFavorite();
        assertFalse(neighbourProfile.isFavorite());
        assertEquals(0, neighboursFavorite.size());

        onView(withId(R.id.list_neighbours)).check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.activity_profile)).check(matches(isDisplayed()));

        onView(withId(R.id.floatingActionButton_add_favorite)).perform(click());
        neighbourProfile.setFavorite(true);
        service.addNeighbourToFavorite(neighbourProfile);

        assertTrue(neighboursFavorite.contains(neighbourProfile));
        assertTrue(neighboursFavorite.get(0).isFavorite());
    }

    @Test
    public void fragmentFavoriteShowsOnlyFavoriteList() {

        List<Neighbour> DUMMY_FAVORITE_NEIGHBOURS = Arrays.asList(
                new Neighbour(1, "Caroline", "https://i.pravatar.cc/150?u=a042581f4e29026704d", "Saint-Pierre-du-Mont ; 5km",
                        "+33 6 86 57 90 14",  "Bonjour !Je souhaiterais faire de la marche nordique. Pas initiée, je recherche une ou plusieurs personnes susceptibles de m'accompagner !J'aime les jeux de cartes tels la belote et le tarot.."),
                new Neighbour(2, "Jack", "https://i.pravatar.cc/150?u=a042581f4e29026704e", "Saint-Pierre-du-Mont ; 5km",
                        "+33 6 86 57 90 14",  "Bonjour !Je souhaiterais faire de la marche nordique. Pas initiée, je recherche une ou plusieurs personnes susceptibles de m'accompagner !J'aime les jeux de cartes tels la belote et le tarot..")
        );

        List<Neighbour> neighboursFavoriteSecond = DUMMY_FAVORITE_NEIGHBOURS;
        int ITEMS_COUNT = neighboursFavoriteSecond.size();

        onView(withId(R.id.list_neighbours)).check(matches(isDisplayed()));
        onView(withContentDescription(R.string.myseconditem)).perform(click());

        onView(withId(R.id.list_neighbours_favorite)).check(withItemCount(ITEMS_COUNT));
    }
}