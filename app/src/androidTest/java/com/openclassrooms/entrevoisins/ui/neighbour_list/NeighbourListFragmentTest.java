package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.utils.ChildViewAction;
import com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class NeighbourListFragmentTest {

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
    public void deleteUserWithSuccess(){
        onView(withId(R.id.list_neighbours)).check(matches(isDisplayed()));

        neighbourList = service.getNeighbours();
        int expectedItem = neighbourList.size();

        onView(ViewMatchers.withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ChildViewAction.clickChildViewWithId(R.id.item_list_delete_button)));

        onView(withId(R.id.list_neighbours)).check(RecyclerViewItemCountAssertion.withItemCount(expectedItem-1));
    }
}
