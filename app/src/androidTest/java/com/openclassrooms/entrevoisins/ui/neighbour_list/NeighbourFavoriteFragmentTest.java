package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.os.Handler;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
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

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
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
        List<Neighbour> neighbours = service.getNeighbours();
        Neighbour neighbourProfile = neighbours.get(0);
        List<Neighbour> neighboursFavorite = service.getNeighboursFavorite();
        int ITEMS_COUNT = neighboursFavorite.size();

        onView(withId(R.id.list_neighbours)).check(matches(isDisplayed()));
        onView(withId(R.id.tabItem2)).perform(click());

        onView(ViewMatchers.withId(R.id.list_neighbours_favorite)).check(withItemCount(ITEMS_COUNT));
        assertFalse(neighbourProfile.isFavorite());
        assertEquals(0, neighboursFavorite.size());

        onView(withId(R.id.tabItem)).perform(click());

        neighbourProfile.setFavorite(true);
        service.addNeighbourToFavorite(neighbourProfile);

        onView(withId(R.id.tabItem2)).perform(click());
        onView(ViewMatchers.withId(R.id.list_neighbours_favorite)).check(withItemCount(ITEMS_COUNT + 1));
        assertTrue(neighboursFavorite.contains(neighbourProfile));
        for (int i = 0; i < neighboursFavorite.size(); i++)
            assertTrue(neighboursFavorite.get(i).isFavorite());
    }
}