package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.res.ColorStateList;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabItem;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.DummyNeighbourGenerator;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.utils.ChildViewAction;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;
import com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion;

import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.xabaras.android.espresso.recyclerviewchildactions.RecyclerViewChildActions;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasBackground;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.junit.Assert.*;

public class NeighbourFavoriteFragmentTest {

    private NeighbourApiService service;
    private static List<Neighbour> neighbourList;

    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule =
            new ActivityTestRule(ListNeighbourActivity.class);

    @Before
    public void initProfileActivity() {
        service = DI.getNewInstanceApiService();
    }

    @Test
    public void fragmentFavoriteShowsOnlyFavoriteList() {

        int Excepted_ITEMS_COUNT = 3;

        onView(withId(R.id.list_neighbours)).check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.activity_profile)).check(matches(isDisplayed()));
        onView(withId(R.id.floatingActionButton_add_favorite))
                .perform(click());

        pressBack();

        onView(withId(R.id.list_neighbours)).check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.floatingActionButton_add_favorite))
                .perform(click());
        pressBack();

        onView(withId(R.id.list_neighbours)).check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(withId(R.id.floatingActionButton_add_favorite))
                .perform(click());
        pressBack();

        onView(withId(R.id.container))
                .perform(swipeLeft());

        onView(ViewMatchers.withId(R.id.list_neighbours_favorite)).check(withItemCount(Excepted_ITEMS_COUNT));
    }

    @Test
    public void deleteUserWithSuccess(){


        onView(withId(R.id.list_neighbours)).check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.activity_profile)).check(matches(isDisplayed()));
        onView(withId(R.id.floatingActionButton_add_favorite))
                .perform(click());

        neighbourList = service.getNeighboursFavorite();
        int expectedItem = neighbourList.size();

        pressBack();

        onView(withId(R.id.container))
                .perform(swipeLeft());

        onView(ViewMatchers.withId(R.id.list_neighbours_favorite))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ChildViewAction.clickChildViewWithId(R.id.item_list_delete_button)));

        onView(withId(R.id.list_neighbours_favorite)).check(RecyclerViewItemCountAssertion.withItemCount(expectedItem-1));
    }
}